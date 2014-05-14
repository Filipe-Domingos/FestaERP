package br.com.rdantasnunes.festaerp.modelo;

import java.io.Serializable;
import java.util.Date;

public class Venda implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long festa_id;
	
	private Long produto_id;
	
	private Long caixa_id;
	
	private Long comanda_id;//uma comanda pode ser atribuida a uma mesa, e então o consumo da mesa será lançado na comanda.
	
	private Date dataVenda;

	private Float quantidade;
	
	private Float valor;
}
