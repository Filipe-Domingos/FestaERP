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

import br.com.rdantasnunes.festaerp.idao.FestaDao;
import br.com.rdantasnunes.festaerp.modelo.Festa;

@Singleton
public class FestaDAOImpl implements FestaDao, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Override
	public List<Festa> find() {
		//log.info("Finding all festas");
		
		//checks if the festas are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Festa> festas = (List<Festa>) syncCache.get( "FESTAS" );
		
		if (festas == null) {
			//log.info("Not found in cache");
			festas = ofy().load().type(Festa.class).list();
		} else {
			//log.info("Using cache!");
		}
		
	    if (festas != null) {
	    	//log.info("Returning " + festas.size() + " festas");
	    }
	    return festas;
	}

	@Override
	public Festa find(Long id) {
		return ofy().load().type(Festa.class).id(id).now();
	}

	@Override
	public Long save(Festa festa) {
		//log.info("Inserting a new festa");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "FESTAS" );
		
		Key<Festa> key = ofy().save().entity(festa).now();
		return key.getId();
	}

	@Override
	public void delete(Festa festa) {
		//log.info("Deleting a new festa");
		ofy().delete().entity(festa).now();
	}
}
