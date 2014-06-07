package br.com.rdantasnunes.festaerp.mb;


import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.rdantasnunes.festaerp.dao.UsuarioDAOImpl;
import br.com.rdantasnunes.festaerp.idao.UsuarioDao;
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
public class LoginMB implements Serializable {
	
	//private static Logger log = Logger.getLogger(ProdutoMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	private UsuarioDao dao;
	
	/**
	 * Referencia para o usuario utilizado para acessar o sistema.
	 */
	private Usuario usuario;

	public LoginMB() {
		super();
		dao = new UsuarioDAOImpl();
		usuario = new Usuario();
	}

	public String entrar() {
		try {
			
			if(!dao.logar(usuario)){
				addMessage(getMessageFromI18N("msg.erro.invalido.usuario"), null,FacesMessage.SEVERITY_ERROR);
				return "";
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			//log.error("Erro ao logar.", ex);
			return "";
		}
		/*http://www.coderanch.com/t/541659/JSF/java/JSF-redirect-page
		http://javafree.uol.com.br/topic-849951-Acesso-restrito-a-paginas.html
		http://fernandofranzini.wordpress.com/2009/09/09/autenticacao-e-autorizacao/*/
		System.out.println("entrar\n");
		HttpSession s = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		s.setAttribute("usuario", usuario);
		
		return "/produto/listaProduto.jsf?faces-redirect=true";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * @param key
	 * @return Recupera a mensagem do arquivo properties <code>ResourceBundle</code>.
	 */
	private String getMessageFromI18N(String key) {
		ResourceBundle bundle = ResourceBundle.getBundle("messages_labels", getCurrentInstance().getViewRoot().getLocale());
		return bundle.getString(key);
	}
	
	/**
	 * Adiciona um mensagem no contexto do Faces (<code>FacesContext</code>).
	 * @param summary
	 * @param detail
	 */
	private void addMessage(String summary, String detail, Severity tipoMensagem) {
		if(detail == null){
			detail = "";
		}
		getCurrentInstance().addMessage(null, new FacesMessage(tipoMensagem, summary, summary.concat("<br/>").concat(detail)));
	}
	
}