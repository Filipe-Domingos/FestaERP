package br.com.rdantasnunes.festaerp.modelo;

import static br.com.rdantasnunes.festaerp.dao.OfyService.ofy;

public abstract class SuperEntity<T> {

	protected T get(Class<T> clazz, Long id){
		return ofy().load().type(clazz).id(id).now();
	}
}
