package br.com.rdantasnunes.festaerp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.rdantasnunes.festaerp.dao.UnidadeDAOImpl;
import br.com.rdantasnunes.festaerp.idao.UnidadeDao;
import br.com.rdantasnunes.festaerp.modelo.Unidade;


@FacesConverter("unidadeConverter")
public class UnidadeConverter implements Converter {
 
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				UnidadeDao dao = new UnidadeDAOImpl();
				return dao.find().get(Integer.parseInt(value));
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
            return String.valueOf(((Unidade) object).getId());
        }
        else {
            return null;
        }
    }  
} 