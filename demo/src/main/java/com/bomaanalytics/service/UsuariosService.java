package com.bomaanalytics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;
import com.bomaanalytics.exceptions.UserAlreadyExistException;
import com.bomaanalytics.repository.UsuariosRepository;

@Service
public class UsuariosService {

	@Autowired
	UsuariosRepository ur;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	

	
	public List<Usuarios> listUsuarios() {
		List<Usuarios> list = ur.findAll();
		
		if(!list.isEmpty()) {
			return list;
		} else {
			throw new UserAlreadyExistException("Lista Vacia");
		}
		
	}

	public Usuarios findUserByRFC(String rfc) {
		Optional<Usuarios> findUser = ur.findUserByRFC(rfc);
		
		if(findUser.isPresent()) {
			return findUser.get();
		}else {
			throw new UserAlreadyExistException("Usuario no encontrado");
		}
	}

	public Usuarios registerAdmin(Usuarios user) {
		
		if(ur.findUserByRFC(user.getRfc()).isPresent()) {
			throw new UserAlreadyExistException("El usuario ya existe");
		}
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setTipoUsuario(Rol.ADMIN);
		return ur.save(user);
	}

	
	public Usuarios updatedUser(Usuarios user) {
		
		Optional<Usuarios> findUser = ur.findUserByRFC(user.getRfc());		
		Usuarios existingUser = new Usuarios();
		
		if(findUser.isPresent()) {
			existingUser.setId(findUser.get().getId());
			existingUser.setNombreUsuario(user.getNombreUsuario());
			existingUser.setRfc(user.getRfc());
			existingUser.setTipoUsuario(user.getTipoUsuario());
			existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
			existingUser.setTipoUsuario(Rol.ADMIN);
			
			return ur.save(existingUser);
		}else {
			throw new UserAlreadyExistException("No existe el usuario con RFC " + user.getRfc());
		}
		
	}

	public void deleteUser(String rfc) {
		Optional<Usuarios> findUser = ur.findUserByRFC(rfc);
		
		if(findUser.isPresent()) {
			ur.delete(findUser.get());
		} else {
			throw new UserAlreadyExistException("Usuario no encontrado");
		}
	}
}
