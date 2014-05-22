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

import br.com.rdantasnunes.festaerp.idao.ComandaDao;
import br.com.rdantasnunes.festaerp.modelo.Comanda;

@Singleton
public class ComandaDAOImpl implements ComandaDao, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Override
	public List<Comanda> find() {
		log.info("Finding all comandas");
		
		//checks if the comandas are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Comanda> comandas = (List<Comanda>) syncCache.get( "COMANDAS" );
		
		if (comandas == null) {
			log.info("Not found in cache");
			comandas = ofy().load().type(Comanda.class).list();
		} else {
			log.info("Using cache!");
		}
		
	    if (comandas != null) {
	    	log.info("Returning " + comandas.size() + " comandas");
	    }
	    return comandas;
	}

	@Override
	public Comanda find(Long id) {
		return ofy().load().type(Comanda.class).id(id).now();
	}

	@Override
	public Long insert(Comanda comanda) {
		log.info("Inserting a new comanda");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "COMANDAS" );
		
		Key<Comanda> key = ofy().save().entity(comanda).now();
		return key.getId();
	}

	@Override
	public void delete(Comanda comanda) {
		log.info("Deleting a new comanda");
		ofy().delete().entity(comanda).now();
	}
}
