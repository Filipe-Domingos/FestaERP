package br.com.rdantasnunes.festaerp.mb;

import static javax.faces.context.FacesContext.getCurrentInstance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.apache.log4j.Logger;

import br.com.rdantasnunes.festaerp.dao.ProdutoDAOImpl;
import br.com.rdantasnunes.festaerp.idao.ProdutoDao;
import br.com.rdantasnunes.festaerp.modelo.Produto;

/**
 * Componente atua como um intermediario das telas do cadastro e os componentes de negocio (<code>DAO</code>) da entidade <code>Produto</code>.
 * 
 * <p>Trata-se de um <code>Managed Bean</code>, ou seja, as instancia dessa classe sao controladas pelo <code>JSF</code>. Para cada sessao de usuario serao criado um objeto <code>ProdutoMB</code>.</p>
 * 
 * <p>Esse componente atua com um papel parecido com o <code>Controller</code> de outros frameworks <code>MVC</code>, ele resolve o fluxo de navegacao e liga os componentes visuais com os dados.</p>
 * 
 * @author  Rodrigo Dantas Nunes - http://www.linkedin.com/in/rdantasnunes - rdantasnunes(at)gmail(dot)com
 * Criado com base na classe MB de Yaw Tecnologia
 *  
 */
@ManagedBean
@SessionScoped
public class ProdutoMB implements Serializable {
	
	private static Logger log = Logger.getLogger(ProdutoMB.class);
	
	private static final long serialVersionUID = 1L;

	/**
	 * Referencia do componente de persistencia.
	 */
	private ProdutoDao dao;
	
	/**
	 * Referencia para a produto utiliza na inclusao (nova) ou edicao.
	 */
	private Produto produto;
	
	/**
	 * Informacao utilizada na edicao da produto, quando a selecao de um registro na listagem ocorrer.
	 */
	private Long idSelecionado;
	
	/**
	 * Mantem as produtos apresentadas na listagem indexadas pelo id.
	 * <strong>Importante:</strong> a consulta (query) no DataStore do App Engine pode retornar <i>dados antigos</i>, 
	 * que ja foram removidos ou que ainda nao foram incluidos, devido a replicacao dos dados.
	 * 
	 * Dessa forma esse hashmap mantem um espelho do datastore para minizar o impacto desse modelo do App Engine.
	 */
	private Map<Long, Produto> produtos;
	
	public ProdutoMB() {
		dao = new ProdutoDAOImpl();
		fillProdutos();
	}
	
	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public void setIdSelecionado(Long idSelecionado) {
		this.idSelecionado = idSelecionado;
	}
	
	public Long getIdSelecionado() {
		return idSelecionado;
	}
	
	/**
	 * @return <code>DataModel</code> para carregar a lista de produtos.
	 */
	public DataModel<Produto> getDmProdutos() {
		if(produtos == null || produtos.values() == null){
			return new ListDataModel<Produto>(new ArrayList<Produto>());
		}else{
			return new ListDataModel<Produto>(new ArrayList<Produto>(produtos.values()));
		}
	}
	
	private void fillProdutos() {
		try {
			List<Produto> qryProdutos = new ArrayList<Produto>(dao.find());
			produtos = new HashMap<Long, Produto>();
			for (Produto m: qryProdutos) {
				produtos.put(m.getId(), m);
			}
			
			log.debug("Carregou a lista de produtos ("+produtos.size()+")");
		} catch(Exception ex) {
			log.error("Erro ao carregar a lista de produtos.", ex);
			addMessage(getMessageFromI18N("msg.erro.listar.produto"), ex.getMessage());
		}
		
	}
	
	/**
	 * Acao executada quando a pagina de inclusao de produtos for carregada.
	 */
	public void incluir(){
		produto = new Produto();
		log.debug("Pronto pra incluir");
	}
	
	/**
	 * Acao executada quando a pagina de edicao de produtos for carregada.
	 */
	public void editar() {
		if (idSelecionado == null) {
			return;
		}
		produto = produtos.get(idSelecionado);
		log.debug("Pronto pra editar");
	}

	/**
	 * Operacao acionada pela tela de inclusao ou edicao, atraves do <code>commandButton</code> <strong>Salvar</strong>.
	 * @return Se a inclusao/edicao foi realizada vai para listagem, senao permanece na mesma tela.
	 */
	public String salvar() {
		try {
			dao.insert(produto);
			produtos.put(produto.getId(), produto);
		} catch(Exception ex) {
			log.error("Erro ao salvar produto.", ex);
			addMessage(getMessageFromI18N("msg.erro.salvar.produto"), ex.getMessage());
			return "";
		}
		log.debug("Salvour produto "+produto.getId());
		return "listaProdutos";
	}
	
	/**
	 * Operacao acionada pela tela de listagem, atraves do <code>commandButton</code> <strong>Atualizar</strong>. 
	 */
	public void atualizar() {
		fillProdutos();
	}
	
	/**
	 * Operacao acionada toda a vez que a  tela de listagem for carregada.
	 */
	public void reset() {
		produto = null;
		idSelecionado = null;
	}
	
	/**
	 * Operacao acionada pela tela de edicao, atraves do <code>commandButton</code> <strong>Excluir</strong>.
	 * @return Se a exclusao for realizada vai para a listagem, senao permanece na mesma tela.
	 */
	public String remover() {
		try {
			dao.delete(produto);
			produtos.remove(produto.getId());
		} catch(Exception ex) {
			log.error("Erro ao remover produto.", ex);
			addMessage(getMessageFromI18N("msg.erro.remover.produto"), ex.getMessage());
			return "";
		}
		log.debug("Removeu produto "+produto.getId());
		return "listaProdutos";
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
	private void addMessage(String summary, String detail) {
		if(detail == null){
			detail = "";
		}
		getCurrentInstance().addMessage(null, new FacesMessage(summary, summary.concat("<br/>").concat(detail)));
	}
}
