package br.com.rdantasnunes.festaerp.dao;

import static br.com.rdantasnunes.festaerp.dao.OfyService.ofy;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.googlecode.objectify.Key;

import br.com.rdantasnunes.festaerp.idao.ProdutoDao;
import br.com.rdantasnunes.festaerp.modelo.Produto;

@Singleton
public class ProdutoDAOImpl implements ProdutoDao{

	@Inject
	private Logger log;
	
	@Override
	public List<Produto> find() {
		log.info("Finding all produtos");
		
		//checks if the produtos are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Produto> produtos = (List<Produto>) syncCache.get( "PRODUTOS" );
		
		if (produtos == null) {
			log.info("Not found in cache");
			produtos = ofy().load().type(Produto.class).list();
		} else {
			log.info("Using cache!");
		}
		
	    if (produtos != null) {
	    	log.info("Returning " + produtos.size() + " produtos");
	    }
	    return produtos;
	}

	@Override
	public Produto find(Long id) {
		return ofy().load().type(Produto.class).id(id).now();
	}

	@Override
	public Long insert(Produto produto) {
		log.info("Inserting a new produto");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "PRODUTOS" );
		
		Key<Produto> key = ofy().save().entity(produto).now();
		return key.getId();
	}

	@Override
	public void delete(Produto produto) {
		log.info("Deleting a new produto");
		ofy().delete().entity(produto).now();
	}
}
