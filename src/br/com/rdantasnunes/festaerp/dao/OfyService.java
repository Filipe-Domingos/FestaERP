package br.com.rdantasnunes.festaerp.dao;

import br.com.rdantasnunes.festaerp.modelo.Caixa;
import br.com.rdantasnunes.festaerp.modelo.Comanda;
import br.com.rdantasnunes.festaerp.modelo.Festa;
import br.com.rdantasnunes.festaerp.modelo.Produto;
import br.com.rdantasnunes.festaerp.modelo.Unidade;
import br.com.rdantasnunes.festaerp.modelo.Usuario;
import br.com.rdantasnunes.festaerp.modelo.ItemVenda;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {

	static {
		factory().register(Caixa.class);
		factory().register(Comanda.class);
		factory().register(Festa.class);
		factory().register(Produto.class);
		factory().register(Unidade.class);
		factory().register(Usuario.class);
		factory().register(ItemVenda.class);
	}

	/**
	 * Return the ofy correct transaction context.
	 * 
	 * @return Objectify
	 */
	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	/**
	 * Return the ofy factory.
	 * 
	 * @return ObjectifyFactory
	 */
	public static ObjectifyFactory factory() {
		return ObjectifyService.factory();
	}
}
