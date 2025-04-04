package com.bomaanalytics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.jwt.service.AuthService;
import com.bomaanalytics.service.UsuariosService;

@RestController
@RequestMapping("/auth")
public class UsuariosController {

	private AuthService authService;
	private UsuariosService usuariosService;

	public UsuariosController(AuthService authService, UsuariosService usuariosService) {
		super();
		this.authService = authService;
		this.usuariosService = usuariosService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
		String rfc = credentials.get("rfc");
		String password = credentials.get("password");
		String token = authService.login(rfc, password);
		return ResponseEntity.ok(Map.of("token", token));
	}

	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<Usuarios> request = usuariosService.listUsuarios();
		try {
			if (!request.isEmpty()) {
				return new ResponseEntity<Object>(request, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(request, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Object>("message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Usuarios usuario) {
		Usuarios request = usuariosService.registerAdmin(usuario);

		try {
			if (request != null) {
				return new ResponseEntity<Object>(request, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(request, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Object>("message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@PutMapping("/updated-users")
	public ResponseEntity<?> updateUser(@RequestBody Usuarios usuario) {
		
		Usuarios request = usuariosService.updatedUser(usuario);

		try {
			if (request != null) {
				return new ResponseEntity<Object>(request, HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(request, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Object>("message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
	@DeleteMapping("/delete-users")
	public ResponseEntity<?> deleteUser(@RequestBody Map<String, String> credentials) {
		String rfc = credentials.get("rfc");
		usuariosService.deleteUser(rfc);
		return ResponseEntity.ok("Usuario eliminado correctamente");
	}
}
