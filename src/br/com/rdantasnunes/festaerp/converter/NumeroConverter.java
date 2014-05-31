package br.com.rdantasnunes.festaerp.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter("numeroConverter")
public class NumeroConverter implements Converter {
 
	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		try {
			if (value != null && value.trim().length() > 0) {
				value = value.replace(",", ".");
				return Float.parseFloat(value);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}
 
    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if(object != null) {
        	String num = String.valueOf(object);
        	num = num.replace(".", ",");
            return num;
        }
        else {
            return null;
        }
    }  
} 