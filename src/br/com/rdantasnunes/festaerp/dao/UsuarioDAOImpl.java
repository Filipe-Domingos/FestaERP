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

import br.com.rdantasnunes.festaerp.idao.UsuarioDao;
import br.com.rdantasnunes.festaerp.modelo.Usuario;

@Singleton
public class UsuarioDAOImpl implements UsuarioDao, Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private Logger log;
	
	@Override
	public List<Usuario> find() {
		//log.info("Finding all usuarios");
		
		//checks if the usuarios are in the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		@SuppressWarnings("unchecked")
		List<Usuario> usuarios = (List<Usuario>) syncCache.get( "USUARIOS" );
		
		if (usuarios == null) {
			//log.info("Not found in cache");
			usuarios = ofy().load().type(Usuario.class).list();
		} else {
			//log.info("Using cache!");
		}
		
	    if (usuarios != null) {
	    	//log.info("Returning " + usuarios.size() + " usuarios");
	    }
	    return usuarios;
	}

	@Override
	public Usuario find(Long id) {
		return ofy().load().type(Usuario.class).id(id).now();
	}

	@Override
	public Long save(Usuario usuario) {
		//log.info("Inserting a new usuario");
		
		//invalidates the cache
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
		syncCache.delete( "USUARIOS" );
		
		Key<Usuario> key = ofy().save().entity(usuario).now();
		return key.getId();
	}

	@Override
	public void delete(Usuario usuario) {
		//log.info("Deleting a new usuario");
		ofy().delete().entity(usuario).now();
	}
}
