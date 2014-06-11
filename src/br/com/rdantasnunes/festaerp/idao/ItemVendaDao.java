package br.com.rdantasnunes.festaerp.idao;

import br.com.rdantasnunes.festaerp.modelo.ItemVenda;

/**
 * 
 * Created on 16/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * Interface DAO (Data Access Object) da entidade Venda. Deve ser usada para declarar funcoes especificas 
 * da referida entidade.
 * PS: Esta interface herda da interface pai os metodos: List<T> find() / T find(Long id) / Long insert(T t) / void delete(T t)
 * 
 */
public interface ItemVendaDao extends Dao<ItemVenda> {

}
