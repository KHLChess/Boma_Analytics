package com.bomaanalytics.jwt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.jwt.util.JwtUtil;
import com.bomaanalytics.repository.UsuariosRepository;

@Service
public class AuthService {

	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

	public AuthService(UsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil,
			AuthenticationManager authenticationManager) {
		super();
		this.usuariosRepository = usuariosRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
	}
	
	
	public String login(String rfc, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(rfc, password));
        UserDetails userDetails = userDetailsService.loadUserByUsername(rfc);
        return jwtUtil.generateToken(userDetails);
    }
	
	public Usuarios registerUser(Usuarios usuario) {
        if (usuariosRepository.findUserByRFC(usuario.getRfc()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuariosRepository.save(usuario);
    }	
}
