package com.std.ec.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class FacesUtils {
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }

    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }

}
