package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

/**
 * 
 * Created on 14/05/2014
 * 
 * @author Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 * Entidade Produto usada para representar os dados dos produtos vendidos
 * 
 */
@Entity
public class Produto extends SuperEntity<Produto> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	@Index
	private String codigoBarra;
	
	@Index
	private String descricao;
	
	private Float quantidade;
	
	private Float valor;
	
	private Key<Unidade> unidade_id;
	
	@Ignore
	private Unidade unidade;

	public Produto() {
		super();
	}

	public Produto(String descricao, String codigoBarra, Float quantidade, Float valor,
			Unidade unidade) {
		super();
		this.codigoBarra = codigoBarra;
		this.descricao = descricao;
		this.quantidade = quantidade;
		this.valor = valor;
		setUnidade(unidade);
	}
	
	public Produto(Long id, String descricao, String codigoBarra, Float quantidade, Float valor,
			Unidade unidade) {
		super();
		this.id = id;
		this.codigoBarra = codigoBarra;
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

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
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
			this.unidade = (Unidade)get(Unidade.class,unidade_id.getId());
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
				+ ((codigoBarra == null) ? 0 : codigoBarra.hashCode());
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
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
		if (codigoBarra == null) {
			if (other.codigoBarra != null)
				return false;
		} else if (!codigoBarra.equals(other.codigoBarra))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getDescricao();
	}
}
