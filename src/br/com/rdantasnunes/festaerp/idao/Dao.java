package br.com.rdantasnunes.festaerp.idao;

import java.util.List;

/**
 * 
 * Created on 16/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * @param <T> Classe generica usada como ref. na implementacao desta interface.
 * 
 * O objetivo desta interface eh ser uma referencia com as funcoes basicas de uma 
 * interface DAO (Data Access Object) do sistema. As funcoes especificas de cada 
 * DAO de entidade devem ser declaradas nas interfaces especificas de cada DAO correspondente.
 * 
 */
public interface Dao<T> {

	/**
	 * List all objects of this class
	 * @return All objects
	 */
	public List<T> find();
	
	/**
	 * Find a specific object in datastore
	 * @param Id from object to be sought
	 */
	public T find(Long id);
	
	/**
	 * Insert a new object into datastore
	 * @param objects from generic class to be inserted
	 * @return the generated id
	 */
	public Long save(T t );
	
	/**
	 * Remove a specific object from the datastore
	 * @param Object to be removed
	 */
	public void delete(T t);
	
}
