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

import br.com.rdantasnunes.festaerp.idao.UnidadeDao;
import br.com.rdantasnunes.festaerp.modelo.Unidade;

@Singleton
public class UnidadeDAOImpl implements UnidadeDao, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Override
	public List<Unidade> find() {
		////log.info("Finding all unidades");
		
		//checks if the unidades are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Unidade> unidades = (List<Unidade>) syncCache.get( "UNIDADES" );
		
		if (unidades == null) {
			////log.info("Not found in cache");
			unidades = ofy().load().type(Unidade.class).list();
		} else {
			////log.info("Using cache!");
		}
		
	    if (unidades != null) {
	    	////log.info("Returning " + unidades.size() + " unidades");
	    }
	    return unidades;
	}

	@Override
	public Unidade find(Long id) {
		return ofy().load().type(Unidade.class).id(id).now();
	}

	@Override
	public Long save(Unidade unidade) {
		////log.info("Inserting a new unidade");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "UNIDADES" );
		
		Key<Unidade> key = ofy().save().entity(unidade).now();
		return key.getId();
	}

	@Override
	public void delete(Unidade unidade) {
		//log.info("Deleting a new unidade");
		ofy().delete().entity(unidade).now();
	}
}
