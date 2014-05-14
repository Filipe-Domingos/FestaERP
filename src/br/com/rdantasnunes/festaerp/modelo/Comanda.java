package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;

public class Comanda implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private Long numeroMesa;
	
	private String identificacaoResponsavel; //pode ser usado RG, telefone, cpf ou o que o estabelecimento quiser.

	public Comanda(Long numeroMesa, String identificacaoResponsavel) {
		super();
		this.numeroMesa = numeroMesa;
		this.identificacaoResponsavel = identificacaoResponsavel;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroMesa() {
		return numeroMesa;
	}

	public void setNumeroMesa(Long numero) {
		this.numeroMesa = numero;
	}

	public String getIdentificacaoResponsavel() {
		return identificacaoResponsavel;
	}

	public void setIdentificacaoResponsavel(String identificacaoResponsavel) {
		this.identificacaoResponsavel = identificacaoResponsavel;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((identificacaoResponsavel == null) ? 0
						: identificacaoResponsavel.hashCode());
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
		Comanda other = (Comanda) obj;
		if (identificacaoResponsavel == null) {
			if (other.identificacaoResponsavel != null)
				return false;
		} else if (!identificacaoResponsavel
				.equals(other.identificacaoResponsavel))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return identificacaoResponsavel;
	}
}
