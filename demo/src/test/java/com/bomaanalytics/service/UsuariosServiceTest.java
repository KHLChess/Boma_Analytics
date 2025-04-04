package com.bomaanalytics.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;
import com.bomaanalytics.exceptions.UserAlreadyExistException;
import com.bomaanalytics.repository.UsuariosRepository;

@ExtendWith(MockitoExtension.class)
public class UsuariosServiceTest {

	@InjectMocks
	private UsuariosService usuariosService;

	@Mock
	private UsuariosRepository usuariosRepository;
	
	@Mock
	private PasswordEncoder passwordEncoder;

	private Usuarios usuario;

	@BeforeEach
    void setUp() {
		
		MockitoAnnotations.openMocks(this);
		
        usuario = new Usuarios();
        usuario.setId(1L);
        usuario.setRfc("ABC123456789");
        usuario.setNombreUsuario("admin");
        usuario.setTipoUsuario(Rol.ADMIN);
        usuario.setPassword("securepassword");
    }

    @Test
    void testListUsuarios() {
        List<Usuarios> usuariosList = new ArrayList<>();
        usuariosList.add(usuario);

        when(usuariosRepository.findAll()).thenReturn(usuariosList);

        List<Usuarios> result = usuariosService.listUsuarios();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(usuariosRepository, times(1)).findAll();
    }

    @Test
    void testListUsuarios_Empty() {
        when(usuariosRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(UserAlreadyExistException.class, () -> usuariosService.listUsuarios());
        verify(usuariosRepository, times(1)).findAll();
    }

    @Test
    void testFindUserByRFC() {
        when(usuariosRepository.findUserByRFC("ABC123456789")).thenReturn(Optional.of(usuario));

        Usuarios result = usuariosService.findUserByRFC("ABC123456789");

        assertNotNull(result);
        assertEquals("ABC123456789", result.getRfc());
        verify(usuariosRepository, times(1)).findUserByRFC("ABC123456789");
    }

    @Test
    void testFindUserByRFC_NotFound() {
        when(usuariosRepository.findUserByRFC("XYZ987654321")).thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistException.class, () -> usuariosService.findUserByRFC("XYZ987654321"));
        verify(usuariosRepository, times(1)).findUserByRFC("XYZ987654321");
    }

    @Test
    void testRegisterAdmin() {
        when(usuariosRepository.findUserByRFC(usuario.getRfc())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(usuario.getPassword())).thenReturn("encodedPassword");
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(usuario);

        Usuarios result = usuariosService.registerAdmin(usuario);

        assertNotNull(result);
        assertEquals(Rol.ADMIN, result.getTipoUsuario());
        verify(usuariosRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    void testRegisterAdmin_UserAlreadyExists() {
        when(usuariosRepository.findUserByRFC(usuario.getRfc())).thenReturn(Optional.of(usuario));

        assertThrows(UserAlreadyExistException.class, () -> usuariosService.registerAdmin(usuario));
        verify(usuariosRepository, times(0)).save(any(Usuarios.class));
    }

    @Test
    void testUpdatedUser() {
        Usuarios updatedUsuario = new Usuarios();
        updatedUsuario.setRfc("ABC123456789");
        updatedUsuario.setNombreUsuario("updatedAdmin");
        updatedUsuario.setPassword("newPassword");
        updatedUsuario.setTipoUsuario(Rol.ADMIN);

        when(usuariosRepository.findUserByRFC("ABC123456789")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(usuariosRepository.save(any(Usuarios.class))).thenReturn(updatedUsuario);

        Usuarios result = usuariosService.updatedUser(updatedUsuario);

        assertNotNull(result);
        assertEquals("updatedAdmin", result.getNombreUsuario());
        verify(usuariosRepository, times(1)).save(any(Usuarios.class));
    }

    @Test
    void testUpdatedUser_NotFound() {
        Usuarios usuarioNoExistente = new Usuarios();
        usuarioNoExistente.setRfc("XYZ987654321"); // Asegurar que es el mismo que se usará en la prueba

        when(usuariosRepository.findUserByRFC(usuarioNoExistente.getRfc())).thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistException.class, () -> usuariosService.updatedUser(usuarioNoExistente));

        verify(usuariosRepository, times(1)).findUserByRFC(usuarioNoExistente.getRfc());
    }

    @Test
    void testDeleteUser() {
        when(usuariosRepository.findUserByRFC("ABC123456789")).thenReturn(Optional.of(usuario));
        doNothing().when(usuariosRepository).delete(usuario);

        usuariosService.deleteUser("ABC123456789");

        verify(usuariosRepository, times(1)).delete(usuario);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(usuariosRepository.findUserByRFC("XYZ987654321")).thenReturn(Optional.empty());

        assertThrows(UserAlreadyExistException.class, () -> usuariosService.deleteUser("XYZ987654321"));
        verify(usuariosRepository, times(0)).delete(any(Usuarios.class));
    }
}
