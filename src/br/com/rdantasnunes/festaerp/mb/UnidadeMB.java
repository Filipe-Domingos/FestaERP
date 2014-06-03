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

import br.com.rdantasnunes.festaerp.dao.UnidadeDAOImpl;
import br.com.rdantasnunes.festaerp.idao.UnidadeDao;
import br.com.rdantasnunes.festaerp.modelo.Unidade;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Unidade</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>UnidadeMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 */
@ManagedBean
@SessionScoped
public class UnidadeMB implements Serializable {
	
	private static Logger log = Logger.getLogger(UnidadeMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	private UnidadeDao dao;
	
	/**
	 * Referencia para a unidade utiliza na inclusao (nova) ou edicao.
	 */
	private Unidade unidade;
	
	/**
	 * Informacao utilizada na edicao da unidade, quando a selecao de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantem as unidades apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que ja foram removidos ou que ainda nao foram incluidos, devido a replicacao dos dados.
	 * 
	 * Dessa forma esse hashmap mantem um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Unidade> unidades;
	
	public UnidadeMB() {
		dao = new UnidadeDAOImpl();
		fillUnidades();
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de unidades.
	 */
	public DataModel<Unidade> getDmUnidades() {
		if(unidades == null || unidades.values() == null){
			return new ListDataModel<Unidade>(new ArrayList<Unidade>());
		}else{
			return new ListDataModel<Unidade>(new ArrayList<Unidade>(unidades.values()));
		}
	}
	
	private void fillUnidades() {
		try {
			List<Unidade> qryUnidades = new ArrayList<Unidade>(dao.find());
			unidades = new HashMap<Long, Unidade>();
			for (Unidade m: qryUnidades) {
				unidades.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de unidades ("+unidades.size()+")");
			
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de unidades.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.unidade"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	/**
	 * Operacao acionada pela tela de listagem, atraves do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillUnidades();
	}
	
	/**
	 * Operacao acionada toda a vez que a tela de listagem for carregada.
	 */
	public void reset() {
		unidade = null;
		idSelecionado = null;
	}
	
	/**
	 * Acao executada quando a pagina de inclusao de unidades for carregada.
	 */
	public void incluir(){
		unidade = new Unidade();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Acao executada quando a pagina de edicao de unidades for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		unidade = unidades.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operacao acionada pela tela de inclusao ou edicao, atraves do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusao/edicao foi realizada vai para listagem, senao permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(unidade);
			unidades.put(unidade.getId(), unidade);
		} catch(Exception ex) {
			log.error("Erro ao salvar unidade.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.unidade"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Salvour unidade "+unidade.getId());
		return "listaUnidade?faces-redirect=true";
	}
	
	/**
	 * Operacao acionada pela tela de edicao, atraves do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusao for realizada vai para a listagem, senao permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.delete(unidade);
			unidades.remove(unidade.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover unidade.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.unidade"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Removeu unidade "+unidade.getId());
		return "listaUnidade?faces-redirect=true";
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

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}
}
