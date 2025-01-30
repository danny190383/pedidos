package com.std.ec.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.inject.Inject;
import java.io.IOException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.std.ec.controller.UserSessionBean;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{
	
	@Inject
	@Lazy
	private UserSessionBean userSessionBean;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        userSessionBean.setUsername(user.getUsername());
        userSessionBean.setRole(authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse("ROLE_USER"));
        userSessionBean.cargarMenuModulosAccesos();
        userSessionBean.cambiarModuloAction(null);
        response.sendRedirect(request.getContextPath() + "/home.xhtml");
    }
}
