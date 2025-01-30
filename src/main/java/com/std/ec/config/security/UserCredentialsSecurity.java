package com.std.ec.config.security;

import com.std.ec.entity.Usuario;
import com.std.ec.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCredentialsSecurity implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (usuario != null) {
            grantedAuthorities = usuario.getRoles().stream()
                    .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                    .collect(Collectors.toList());
            return new User(
                    usuario.getUsername(),
                    usuario.getPassword(),
                    usuario.isEstado(),
                    true,
                    true,
                    true,
                    grantedAuthorities
            );
        }
        return null;
    }

}
