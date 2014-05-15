package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Venda implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Long id;
	
	private Long festa_id;
	
	private Long produto_id;
	
	private Long caixa_id;
	
	private Long usuario_id;
	
	private Long comanda_id;//uma comanda pode ser atribuida a uma mesa, e então o consumo da mesa será lançado na comanda.
	
	@Index
	private Date dataVenda;

	private Float quantidade;
	
	private Float valor;

	public Venda(Long festa_id, Long produto_id, Long caixa_id,
			Long usuario_id, Long comanda_id, Date dataVenda, Float quantidade,
			Float valor) {
		super();
		this.festa_id = festa_id;
		this.produto_id = produto_id;
		this.caixa_id = caixa_id;
		this.usuario_id = usuario_id;
		this.comanda_id = comanda_id;
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

	public Long getFesta_id() {
		return festa_id;
	}

	public void setFesta_id(Long festa_id) {
		this.festa_id = festa_id;
	}

	public Long getProduto_id() {
		return produto_id;
	}

	public void setProduto_id(Long produto_id) {
		this.produto_id = produto_id;
	}

	public Long getCaixa_id() {
		return caixa_id;
	}

	public void setCaixa_id(Long caixa_id) {
		this.caixa_id = caixa_id;
	}

	public Long getUsuario_id() {
		return usuario_id;
	}

	public void setUsuario_id(Long usuario_id) {
		this.usuario_id = usuario_id;
	}

	public Long getComanda_id() {
		return comanda_id;
	}

	public void setComanda_id(Long comanda_id) {
		this.comanda_id = comanda_id;
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
