package br.com.rdantasnunes.festaerp.modelo;

import static br.com.rdantasnunes.festaerp.dao.OfyService.ofy;

/**
 * 
 * Created on 15/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * Super classe das entidades usada para prover recursos e comportamento comum a todas as entidades da aplicacao.
 * 
 */
public abstract class SuperEntity<T> {

	protected Object get(Class<?> clazz, Long id){
		return ofy().load().type(clazz).id(id).now();
	}
}
