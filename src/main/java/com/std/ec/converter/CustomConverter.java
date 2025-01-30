package com.std.ec.converter;

import com.std.ec.entity.TipoCombustible;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "customConverter")
public class CustomConverter implements Converter<TipoCombustible> {

    @Override
    public TipoCombustible getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new TipoCombustible(Long.valueOf(value), null, null, null); // Ajustar según tu constructor
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, TipoCombustible object) {
        if (object == null || object.getIdTipoCombustible() == null) {
            return "";
        }
        return String.valueOf(object.getIdTipoCombustible());
    }
}
