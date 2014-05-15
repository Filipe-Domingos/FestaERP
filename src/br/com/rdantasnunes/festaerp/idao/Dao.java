package br.com.rdantasnunes.festaerp.idao;

import java.util.List;

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
	public Long insert(T t );
	
	/**
	 * Remove a specific object from the datastore
	 * @param Object to be removed
	 */
	public void delete(T t);
	
}
