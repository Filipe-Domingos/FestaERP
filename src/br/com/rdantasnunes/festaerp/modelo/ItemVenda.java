package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;

/**
 * 
 * Created on 14/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * Entidade Venda usada para representar os dados da venda.
 * 
 */
@Entity
public class ItemVenda extends SuperEntity<ItemVenda> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private Key<Produto> produto_id;
	@Ignore private Produto produto;
	
	private Key<Comanda> comanda_id;//uma comanda pode ser atribuida a uma mesa, e entao o consumo da mesa sera lancado na comanda.
	@Ignore private Comanda comanda;
	
	private Float quantidade;
	
	private Float valor; //o valor vai ficar registrado para que a casa possa fazer promoções do tipo até 23hs tal bebida é um preço e depois disso é outro preço.
	
	private Date horaConsumo;

	public ItemVenda() {
		super();
	}

	public ItemVenda(Produto produto, Comanda comanda, Date horaConsumo, Float quantidade, Float valor) {
		super();
		setProduto(produto);
		setComanda(comanda);
		this.quantidade = quantidade;
		this.valor = valor;
		this.horaConsumo = horaConsumo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		if(this.produto == null && produto_id != null){
			this.produto = (Produto)get(Produto.class,produto_id.getId());
		}
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		if(produto != null && produto.getId() != null){
			this.produto_id = Key.create(Produto.class,produto.getId());
		}
	}

	public Comanda getComanda() {
		if(this.comanda == null && comanda_id != null){
			this.comanda = (Comanda)get(Comanda.class,comanda_id.getId());
		}
		return this.comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
		if(comanda != null && comanda.getId() != null){
			this.comanda_id = Key.create(Comanda.class,comanda.getId());
		}
	}

	public Float getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Float quantidade) {
		this.quantidade = quantidade;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Date getHoraConsumo() {
		return horaConsumo;
	}

	public void setHoraConsumo(Date horaConsumo) {
		this.horaConsumo = horaConsumo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comanda_id == null) ? 0 : comanda_id.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((produto_id == null) ? 0 : produto_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ItemVenda other = (ItemVenda) obj;
		if (comanda_id == null) {
			if (other.comanda_id != null)
				return false;
		} else if (!comanda_id.equals(other.comanda_id))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (produto_id == null) {
			if (other.produto_id != null)
				return false;
		} else if (!produto_id.equals(other.produto_id))
			return false;
		return true;
	}
}
