package com.std.ec.converter;

import com.std.ec.entity.Rol;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "rolConverter")
public class RolConverter implements Converter<Rol> {

    @Override
    public Rol getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return new Rol(Long.valueOf(value), null);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Rol object) {
        if (object == null || object.getIdRol() == null) {
            return "";
        }
        return String.valueOf(object.getIdRol());
    }
}
