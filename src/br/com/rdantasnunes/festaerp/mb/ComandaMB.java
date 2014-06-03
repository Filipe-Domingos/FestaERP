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

import br.com.rdantasnunes.festaerp.dao.ComandaDAOImpl;
import br.com.rdantasnunes.festaerp.idao.ComandaDao;
import br.com.rdantasnunes.festaerp.modelo.Comanda;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Comanda</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>ComandaMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * 
 */
@ManagedBean
@SessionScoped
public class ComandaMB implements Serializable {
	
	private static Logger log = Logger.getLogger(ComandaMB.class);
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Referencia do componente de persistencia.
	 */
	private ComandaDao dao;
	
	/**
	 * Referencia para a comanda utiliza na inclusao (nova) ou edicao.
	 */
	private Comanda comanda;
	
	/**
	 * Informacao utilizada na edicao da comanda, quando a selecao de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantem as comandas apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que ja foram removidos ou que ainda nao foram incluidos, devido a replicacao dos dados.
	 * 
	 * Dessa forma esse hashmap mantem um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Comanda> comandas;
	
	public ComandaMB() {
		dao = new ComandaDAOImpl();
		fillComandas();
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de comandas.
	 */
	public DataModel<Comanda> getDmComandas() {
		if(comandas == null || comandas.values() == null){
			return new ListDataModel<Comanda>(new ArrayList<Comanda>());
		}else{
			return new ListDataModel<Comanda>(new ArrayList<Comanda>(comandas.values()));
		}
	}
	
	private void fillComandas() {
		try {
			List<Comanda> qryComandas = new ArrayList<Comanda>(dao.find());
			comandas = new HashMap<Long, Comanda>();
			for (Comanda m: qryComandas) {
				comandas.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de comandas ("+comandas.size()+")");
			
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de comandas.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.comanda"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
		}
	}
	
	/**
	 * Operacao acionada pela tela de listagem, atraves do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillComandas();
	}
	
	/**
	 * Operacao acionada toda a vez que a tela de listagem for carregada.
	 */
	public void reset() {
		comanda = null;
		idSelecionado = null;
	}
	
	/**
	 * Acao executada quando a pagina de inclusao de comandas for carregada.
	 */
	public void incluir(){
		comanda = new Comanda();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Acao executada quando a pagina de edicao de comandas for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		comanda = comandas.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operacao acionada pela tela de inclusao ou edicao, atraves do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusao/edicao foi realizada vai para listagem, senao permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.save(comanda);
			comandas.put(comanda.getId(), comanda);
		} catch(Exception ex) {
			log.error("Erro ao salvar comanda.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.comanda"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Salvour comanda "+comanda.getId());
		return "listaComanda?faces-redirect=true";
	}
	
	/**
	 * Operacao acionada pela tela de edicao, atraves do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusao for realizada vai para a listagem, senao permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.delete(comanda);
			comandas.remove(comanda.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover comanda.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.comanda"), ex.getMessage(),FacesMessage.SEVERITY_ERROR);
			return "";
		}
		log.debug("Removeu comanda "+comanda.getId());
		return "listaComanda?faces-redirect=true";
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

	public Comanda getComanda() {
		return comanda;
	}

	public void setComanda(Comanda comanda) {
		this.comanda = comanda;
	}
}
