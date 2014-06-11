package br.com.rdantasnunes.festaerp.mb;


import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.log4j.Logger;

import br.com.rdantasnunes.festaerp.dao.CaixaDAOImpl;
import br.com.rdantasnunes.festaerp.dao.UsuarioDAOImpl;
import br.com.rdantasnunes.festaerp.idao.UsuarioDao;
import br.com.rdantasnunes.festaerp.modelo.Caixa;
import br.com.rdantasnunes.festaerp.modelo.Usuario;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Usuario</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>UsuarioMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 */
@ManagedBean
@SessionScoped
public class UsuarioMB implements Serializable {
	
	private static Logger log = Logger.getLogger(UsuarioMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	private UsuarioDao dao;
	
	/**
	 * Referencia para a usuario utiliza na inclusao (nova) ou edicao.
	 */
	private Usuario usuario;
	
	/**
	 * Atributo usado para confirmar a senha do usuário.
	 */
	private String confirmaSenha;
	
	/**
	 * Informacao utilizada na edicao da usuario, quando a selecao de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Listagem dos caixas disponiveis no sistema
	 */
	private List<Caixa> caixas;
	
	/**
	 * Id do Caixa deste usuario.
	 */
	private Long caixaId;
	
	/**
	 * Mantem as usuarios apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que ja foram removidos ou que ainda nao foram incluidos, devido a replicacao dos dados.
	 * 
	 * Dessa forma esse hashmap mantem um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Usuario> usuarios;
	
	public UsuarioMB() {
		dao = new UsuarioDAOImpl();
		fillUsuarios();
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de usuarios.
	 */
	public DataModel<Usuario> getDmUsuarios() {
		if(usuarios == null || usuarios.values() == null){
			return new ListDataModel<Usuario>(new ArrayList<Usuario>());
		}else{
			return new ListDataModel<Usuario>(new ArrayList<Usuario>(usuarios.values()));
		}
	}
	
	private void fillUsuarios() {
		try {
			List<Usuario> qryUsuarios = new ArrayList<Usuario>(dao.find());
			usuarios = new HashMap<Long, Usuario>();
			for (Usuario m: qryUsuarios) {
				usuarios.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de usuarios ("+usuarios.size()+")");
			
			caixas = new CaixaDAOImpl().find();
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de usuarios.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.usuario"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	/**
	 * Operacao acionada pela tela de listagem, atraves do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillUsuarios();
	}
	
	/**
	 * Operacao acionada toda a vez que a tela de listagem for carregada.
	 */
	public void reset() {
		usuario = null;
		caixaId = null;
		idSelecionado = null;
		confirmaSenha = null;
	}
	
	/**
	 * Acao executada quando a pagina de inclusao de usuarios for carregada.
	 */
	public void incluir(){
		usuario = new Usuario();
		caixaId = null;
		confirmaSenha = null;
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Acao executada quando a pagina de edicao de usuarios for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		usuario = usuarios.get(idSelecionado);
		if (usuario == null){
			confirmaSenha = null;
		}else{
			if(usuario.getCaixa() != null)
				caixaId = usuario.getCaixa().getId();
			
			confirmaSenha = usuario.getSenha();
		}
		log.debug("Pronto pra editar");
	}
	
	/**
	 * Operacao acionada pela tela de inclusao ou edicao, atraves do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusao/edicao foi realizada vai para listagem, senao permanece na mesma tela.
	 */
	public String salvar() {
		try {
			if(confirmaSenha == null || !usuario.getSenha().equals(confirmaSenha)){
				addMessage(getMessageFromI18N("msg.erro.senha_nao_confere.usuario"), null,FacesMessage.SEVERITY_ERROR);
				return "";
			}
			if(caixaId != null){
				Caixa caixa = new CaixaDAOImpl().find(caixaId);
				usuario.setCaixa(caixa);
				caixaId = null;
			}
			dao.save(usuario);
			usuarios.put(usuario.getId(), usuario);
		} catch(Exception ex) {
			ex.printStackTrace();
			log.error("Erro ao salvar usuario.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.usuario"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Salvour usuario "+usuario.getId());
		return "listaUsuario?faces-redirect=true";
	}
	
	/**
	 * Operacao acionada pela tela de edicao, atraves do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusao for realizada vai para a listagem, senao permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.delete(usuario);
			usuarios.remove(usuario.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover usuario.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.usuario"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Removeu usuario "+usuario.getId());
		return "listaUsuario?faces-redirect=true";
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
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public List<Caixa> getCaixas() {
		return caixas;
	}

	public void setCaixas(List<Caixa> caixas) {
		this.caixas = caixas;
	}

	public Long getCaixaId() {
		return caixaId;
	}

	public void setCaixaId(Long caixaId) {
		this.caixaId = caixaId;
	}
}