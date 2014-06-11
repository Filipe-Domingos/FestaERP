package br.com.rdantasnunes.festaerp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.rdantasnunes.festaerp.dao.CaixaDAOImpl;
import br.com.rdantasnunes.festaerp.idao.CaixaDao;
import br.com.rdantasnunes.festaerp.modelo.Caixa;


@FacesConverter("caixaConverter")
public class CaixaConverter implements Converter {
 
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		System.out.println("caixaConverter getAsObject");
		try {
			if (value != null && value.trim().length() > 0) {
				CaixaDao dao = new CaixaDAOImpl();
				return dao.find().get(Integer.parseInt(value));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
    	System.out.println("caixaConverter getAsString");
        if(object != null) {
            return String.valueOf(((Caixa) object).getId());
        }
        else {
            return null;
        }
    }  
} 