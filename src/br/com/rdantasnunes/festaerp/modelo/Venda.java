package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Transient;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

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
public class Venda extends SuperEntity<Venda> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private Key<Festa> festa_id;
	@Transient private Festa festa;
	
	private Key<Produto> produto_id;
	@Transient private Produto produto;
	
	private Key<Caixa> caixa_id;
	@Transient private Caixa caixa;
	
	private Key<Usuario> usuario_id;
	@Transient private Usuario usuario;
	
	private Key<Comanda> comanda_id;//uma comanda pode ser atribuida a uma mesa, e entao o consumo da mesa sera lancado na comanda.
	@Transient private Comanda comanda;
	
	@Index
	private Date dataVenda;

	private Float quantidade;
	
	private Float valor;

	public Venda() {
		super();
	}

	public Venda(Festa festa, Produto produto, Caixa caixa,
			Usuario usuario, Comanda comanda, Date dataVenda, Float quantidade,
			Float valor) {
		super();
		setFesta(festa);
		setProduto(produto);
		setCaixa(caixa);
		setUsuario(usuario);
		setComanda(comanda);
		this.dataVenda = dataVenda;
		this.quantidade = quantidade;
		this.valor = valor;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Festa getFesta() {
		if(this.festa == null && festa_id != null){
			this.festa = festa.get(Festa.class,festa_id.getId());
		}
		return this.festa;
	}

	public void setFesta(Festa festa) {
		this.festa = festa;
		if(festa != null && festa.getId() != null){
			this.festa_id = Key.create(Festa.class,festa.getId());
		}
	}

	public Produto getProduto() {
		if(this.produto == null && produto_id != null){
			this.produto = produto.get(Produto.class,produto_id.getId());
		}
		return this.produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
		if(produto != null && produto.getId() != null){
			this.produto_id = Key.create(Produto.class,produto.getId());
		}
	}

	public Caixa getCaixa() {
		if(this.caixa == null && caixa_id != null){
			this.caixa = caixa.get(Caixa.class,caixa_id.getId());
		}
		return this.caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
		if(caixa != null && caixa.getId() != null){
			this.caixa_id = Key.create(Caixa.class,caixa.getId());
		}
	}

	public Usuario getUsuario() {
		if(this.usuario == null && usuario_id != null){
			this.usuario = usuario.get(Usuario.class,usuario_id.getId());
		}
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		if(usuario != null && usuario.getId() != null){
			this.usuario_id = Key.create(Usuario.class,usuario.getId());
		}
	}

	public Comanda getComanda() {
		if(this.comanda == null && comanda_id != null){
			this.comanda = comanda.get(Comanda.class,comanda_id.getId());
		}
		return this.comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
		if(comanda != null && comanda.getId() != null){
			this.comanda_id = Key.create(Comanda.class,comanda.getId());
		}
	}

	public Date getDataVenda() {
		return dataVenda;
	}

	public void setDataVenda(Date dataVenda) {
		this.dataVenda = dataVenda;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((comanda_id == null) ? 0 : comanda_id.hashCode());
		result = prime * result
				+ ((dataVenda == null) ? 0 : dataVenda.hashCode());
		result = prime * result
				+ ((festa_id == null) ? 0 : festa_id.hashCode());
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
		Venda other = (Venda) obj;
		if (comanda_id == null) {
			if (other.comanda_id != null)
				return false;
		} else if (!comanda_id.equals(other.comanda_id))
			return false;
		if (dataVenda == null) {
			if (other.dataVenda != null)
				return false;
		} else if (!dataVenda.equals(other.dataVenda))
			return false;
		if (festa_id == null) {
			if (other.festa_id != null)
				return false;
		} else if (!festa_id.equals(other.festa_id))
			return false;
		return true;
	}
	
	
}
