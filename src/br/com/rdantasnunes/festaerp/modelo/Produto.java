package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;

import javax.persistence.Transient;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Produto extends SuperEntity<Produto> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Index
	private String descricao;
	
	private Float quantidade;
	
	private Float valor;
	
	private Key<Unidade> unidade_id;
	@Transient private Unidade unidade;

	public Produto() {
		super();
	}

	public Produto(String descricao, Float quantidade, Float valor,
			Unidade unidade) {
		super();
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.valor = valor;
		setUnidade(unidade);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public Unidade getUnidade() {
		if(this.unidade == null && unidade_id != null){
			this.unidade = unidade.get(Unidade.class,unidade_id.getId());
		}
		return this.unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
		if(unidade != null && unidade.getId() != null){
			this.unidade_id = Key.create(Unidade.class,unidade.getId());
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result
				+ ((unidade_id == null) ? 0 : unidade_id.hashCode());
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
		Produto other = (Produto) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (unidade_id == null) {
			if (other.unidade_id != null)
				return false;
		} else if (!unidade_id.equals(other.unidade_id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
}
