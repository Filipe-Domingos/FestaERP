package br.com.rdantasnunes.festaerp.dao;

import static br.com.rdantasnunes.festaerp.dao.OfyService.ofy;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;

import br.com.rdantasnunes.festaerp.idao.ItemVendaDao;
import br.com.rdantasnunes.festaerp.modelo.ItemVenda;

@Singleton
public class ItemVendaDAOImpl implements ItemVendaDao, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Override
	public List<ItemVenda> find() {
		log.info("Finding all vendas");
		
		//checks if the vendas are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<ItemVenda> vendas = (List<ItemVenda>) syncCache.get( "ITEM_VENDAS" );
		
		if (vendas == null) {
			log.info("Not found in cache");
			vendas = ofy().load().type(ItemVenda.class).list();
		} else {
			log.info("Using cache!");
		}
		
	    if (vendas != null) {
	    	log.info("Returning " + vendas.size() + " vendas");
	    }
	    return vendas;
	}

	@Override
	public ItemVenda find(Long id) {
		return ofy().load().type(ItemVenda.class).id(id).now();
	}

	@Override
	public Long save(ItemVenda venda) {
		log.info("Inserting a new venda");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "ITEM_VENDAS" );
		
		Key<ItemVenda> key = ofy().save().entity(venda).now();
		return key.getId();
	}

	@Override
	public void delete(ItemVenda venda) {
		log.info("Deleting a new venda");
		ofy().delete().entity(venda).now();
	}
}
