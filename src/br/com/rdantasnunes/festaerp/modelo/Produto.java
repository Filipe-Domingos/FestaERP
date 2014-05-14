package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;

public class Produto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String descricao;
	
	private Float quantidade;
	
	private Float valor;
	
	private Long unidade_id;

	public Produto(String descricao, Float quantidade, Float valor,
			Long unidade) {
		super();
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.valor = valor;
		this.unidade_id = unidade;
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

	public Long getUnidade() {
		return unidade_id;
	}

	public void setUnidade(Long unidade) {
		this.unidade_id = unidade;
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
