package br.com.rdantasnunes.festaerp.idao;

import br.com.rdantasnunes.festaerp.modelo.Usuario;

/**
 * 
 * Created on 16/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * Interface DAO (Data Access Object) da entidade Usuario. Deve ser usada para declarar funcoes especificas 
 * da referida entidade.
 * PS: Esta interface herda da interface pai os metodos: List<T> find() / T find(Long id) / Long insert(T t) / void delete(T t)
 * 
 */
public interface UsuarioDao extends Dao<Usuario> {

}
