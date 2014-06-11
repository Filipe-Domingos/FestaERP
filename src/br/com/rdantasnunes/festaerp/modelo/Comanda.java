package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;
import java.util.Date;

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
 * Entidade Comanda usada para representar a comanda de um dado cliente ou mesa.
 * 
 */
@Entity
public class Comanda extends SuperEntity<Comanda> implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	private Long id;
	
	private Long numeroMesa;
	
	private String identificacaoResponsavel; //pode ser usado RG, telefone, cpf ou o que o estabelecimento quiser.
	
	private String nomePessoa;
	
	private String telefone;
	
	private Date dataAberturaDaComanda;
	
	private Key<Festa> festa_id;
	@Ignore private Festa festa;
	
	private Key<Usuario> usuario_id;
	@Ignore private Usuario usuario;
	
	@Index
	private String cartaoBar; //A pessoa ao entrar recebe um cartao de consumação com um código de barras que será usado p registrar os débitos.

	public Comanda() {
		super();
	}

	public Comanda(Festa festa, Usuario usuario, Long numeroMesa, String identificacaoResponsavel, String nomePessoa, 
			String telefone, Date dataAberturaDaComanda,String cartaoBar) {
		super();
		this.numeroMesa = numeroMesa;
		this.identificacaoResponsavel = identificacaoResponsavel;
		this.nomePessoa = nomePessoa;
		this.telefone = telefone;
		this.dataAberturaDaComanda = dataAberturaDaComanda;
		this.cartaoBar = cartaoBar;
		setFesta(festa);
		setUsuario(usuario);
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

	public Festa getFesta() {
		if(this.festa == null && festa_id != null){
			this.festa = (Festa)get(Festa.class,festa_id.getId());
		}
		return this.festa;
	}

	public void setFesta(Festa festa) {
		this.festa = festa;
		if(festa != null && festa.getId() != null){
			this.festa_id = Key.create(Festa.class,festa.getId());
		}
	}

	public Usuario getUsuario() {
		if(this.usuario == null && usuario_id != null){
			this.usuario = (Usuario)get(Usuario.class,usuario_id.getId());
		}
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		if(usuario != null && usuario.getId() != null){
			this.usuario_id = Key.create(Usuario.class,usuario.getId());
		}
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataAberturaDaComanda() {
		return dataAberturaDaComanda;
	}

	public void setDataAberturaDaComanda(Date dataConsumo) {
		this.dataAberturaDaComanda = dataConsumo;
	}

	public String getCartaoBar() {
		return cartaoBar;
	}

	public void setCartaoBar(String cartaoBar) {
		this.cartaoBar = cartaoBar;
	}

	public String getNomePessoa() {
		return nomePessoa;
	}

	public void setNomePessoa(String nomePessoa) {
		this.nomePessoa = nomePessoa;
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
