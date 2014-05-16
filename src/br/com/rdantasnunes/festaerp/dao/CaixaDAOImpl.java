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

import br.com.rdantasnunes.festaerp.idao.CaixaDao;
import br.com.rdantasnunes.festaerp.modelo.Caixa;

@Singleton
public class CaixaDAOImpl implements CaixaDao{

	@Inject
	private Logger log;
	
	@Override
	public List<Caixa> find() {
		log.info("Finding all caixas");
		
		//checks if the caixas are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Caixa> caixas = (List<Caixa>) syncCache.get( "CAIXAS" );
		
		if (caixas == null) {
			log.info("Not found in cache");
			caixas = ofy().load().type(Caixa.class).list();
		} else {
			log.info("Using cache!");
		}
		
	    if (caixas != null) {
	    	log.info("Returning " + caixas.size() + " caixas");
	    }
	    return caixas;
	}

	@Override
	public Caixa find(Long id) {
		return ofy().load().type(Caixa.class).id(id).now();
	}

	@Override
	public Long insert(Caixa caixa) {
		log.info("Inserting a new caixa");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "CAIXAS" );
		
		Key<Caixa> key = ofy().save().entity(caixa).now();
		return key.getId();
	}

	@Override
	public void delete(Caixa caixa) {
		log.info("Deleting a new caixa");
		ofy().delete().entity(caixa).now();
	}
}
