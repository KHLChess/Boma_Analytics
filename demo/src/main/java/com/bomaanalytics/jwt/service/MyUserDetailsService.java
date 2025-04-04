package com.bomaanalytics.jwt.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.repository.UsuariosRepository;


@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuariosRepository;

    public MyUserDetailsService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String rfc) throws UsernameNotFoundException {
        return usuariosRepository.findUserByRFC(rfc)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + rfc));
    }
}