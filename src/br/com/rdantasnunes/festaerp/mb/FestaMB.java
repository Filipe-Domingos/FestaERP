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

import br.com.rdantasnunes.festaerp.dao.FestaDAOImpl;
import br.com.rdantasnunes.festaerp.idao.FestaDao;
import br.com.rdantasnunes.festaerp.modelo.Festa;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Festa</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>FestaMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 */
@ManagedBean
@SessionScoped
public class FestaMB implements Serializable {
	
	private static Logger log = Logger.getLogger(FestaMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	private FestaDao dao;
	
	/**
	 * Referencia para a festa utiliza na inclusao (nova) ou edicao.
	 */
	private Festa festa;
	
	/**
	 * Informacao utilizada na edicao da festa, quando a selecao de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantem as festas apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que ja foram removidos ou que ainda nao foram incluidos, devido a replicacao dos dados.
	 * 
	 * Dessa forma esse hashmap mantem um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Festa> festas;
	
	public FestaMB() {
		dao = new FestaDAOImpl();
		fillFestas();
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de festas.
	 */
	public DataModel<Festa> getDmFestas() {
		if(festas == null || festas.values() == null){
			return new ListDataModel<Festa>(new ArrayList<Festa>());
		}else{
			return new ListDataModel<Festa>(new ArrayList<Festa>(festas.values()));
		}
	}
	
	private void fillFestas() {
		try {
			List<Festa> qryFestas = new ArrayList<Festa>(dao.find());
			festas = new HashMap<Long, Festa>();
			for (Festa m: qryFestas) {
				festas.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de festas ("+festas.size()+")");
			
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de festas.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.festa"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	/**
	 * Operacao acionada pela tela de listagem, atraves do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillFestas();
	}
	
	/**
	 * Operacao acionada toda a vez que a tela de listagem for carregada.
	 */
	public void reset() {
		festa = null;
		idSelecionado = null;
	}
	
	/**
	 * Acao executada quando a pagina de inclusao de festas for carregada.
	 */
	public void incluir(){
		festa = new Festa();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Acao executada quando a pagina de edicao de festas for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		festa = festas.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operacao acionada pela tela de inclusao ou edicao, atraves do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusao/edicao foi realizada vai para listagem, senao permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(festa);
			festas.put(festa.getId(), festa);
		} catch(Exception ex) {
			log.error("Erro ao salvar festa.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.festa"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Salvour festa "+festa.getId());
		return "listaFesta?faces-redirect=true";
	}
	
	/**
	 * Operacao acionada pela tela de edicao, atraves do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusao for realizada vai para a listagem, senao permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.delete(festa);
			festas.remove(festa.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover festa.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.festa"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Removeu festa "+festa.getId());
		return "listaFesta?faces-redirect=true";
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

	public Festa getFesta() {
		return festa;
	}

	public void setFesta(Festa festa) {
		this.festa = festa;
	}
}
