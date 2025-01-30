package com.std.ec.config.security;

import com.std.ec.config.filter.AutorizacionFilter;
import com.std.ec.service.RolService;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserCredentialsSecurity userCredentialsSecurity;
    @Autowired
    private AutorizacionFilter autorizacionFilter;
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userCredentialsSecurity)
                .passwordEncoder(passwordEncoder());
        return builder.build();
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http, MvcRequestMatcher.Builder mvc, RolService rolService) {
        try {
            http.csrf(AbstractHttpConfigurer::disable);

            http.authorizeHttpRequests(authorize -> {

               authorize.requestMatchers(new AntPathRequestMatcher("/assets/**")).permitAll()
		                .requestMatchers(mvc.pattern("/login.xhtml")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**")).permitAll()
                        .anyRequest()
                        .authenticated();
                })

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )

                .formLogin(formLogin -> formLogin
                        .loginPage("/login.xhtml").permitAll()
                        .failureUrl("/login.xhtml?error=true")
                        .defaultSuccessUrl("/home.xhtml")
                        .usernameParameter("nombreUsuario")
                        .passwordParameter("clave")
                        .successHandler(successHandler)
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.xhtml")
                        .deleteCookies("JSESSIONID"))

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/403.xhtml") 
                )

                .addFilterAfter(autorizacionFilter, UsernamePasswordAuthenticationFilter.class)
            ;
            return http.build();
        } catch (Exception ex) {
            throw new BeanCreationException("Wrong spring security configuration", ex);
        }
    }
}
