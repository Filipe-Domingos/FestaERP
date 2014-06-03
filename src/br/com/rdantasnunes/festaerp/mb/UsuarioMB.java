package br.com.rdantasnunes.festaerp.mb;


import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.webapp.FacesServlet;

import org.apache.log4j.Logger;

import br.com.rdantasnunes.festaerp.modelo.Usuario;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Produto</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>ProdutoMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 *  
 */
@ManagedBean
@SessionScoped
public class UsuarioMB implements Serializable {
	
	private static Logger log = Logger.getLogger(ProdutoMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	//private UsuarioDao dao;
	
	/**
	 * Referencia para o usuario utilizado para acessar o sistema.
	 */
	private Usuario usuario;

	public UsuarioMB() {
		super();
		//dao = new UsuarioDAOImpl();
		usuario = new Usuario();
	}

	public String entrar() {
		/*try {
			
			dao.logar(usuario);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Erro ao logar.", ex);
			return "";
		}
		
		http://www.coderanch.com/t/541659/JSF/java/JSF-redirect-page
		http://javafree.uol.com.br/topic-849951-Acesso-restrito-a-paginas.html
		http://fernandofranzini.wordpress.com/2009/09/09/autenticacao-e-autorizacao/
		*/
		
		
		return "listaProduto?faces-redirect=true";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}