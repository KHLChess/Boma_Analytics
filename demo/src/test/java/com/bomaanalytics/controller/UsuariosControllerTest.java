package com.bomaanalytics.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;
import com.bomaanalytics.jwt.service.AuthService;
import com.bomaanalytics.service.UsuariosService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class UsuariosControllerTest {
    
    @InjectMocks
    private UsuariosController usuariosController;
    
    @Mock
    private AuthService authService;
    
    @Mock
    private UsuariosService usuariosService;
    
    private MockMvc mockMvc;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    private Usuarios usuario;

    @BeforeEach
    void setUp() {
    	
    	MockitoAnnotations.openMocks(this);
    	
        mockMvc = MockMvcBuilders.standaloneSetup(usuariosController).build();
        
        usuario = new Usuarios();
    	usuario.setId(1L);
		usuario.setRfc("ABC123");
		usuario.setNombreUsuario("admin");
		usuario.setTipoUsuario(Rol.ADMIN);
		usuario.setPassword("password");
    }
    
    @Test
    void testLogin() throws Exception {
        when(authService.login("ABC123", "password")).thenReturn("fake-token");
        
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("rfc", "ABC123", "password", "password"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-token"));
    }
    
    @Test
    @WithMockUser(roles = "SUPER_ADMIN")  // Simula un usuario autenticado con permisos
    void testGetAllUsers() throws Exception {
    	
        when(usuariosService.listUsuarios()).thenReturn(List.of(usuario));
        
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rfc").value("ABC123"));
    }
    
    @Test
    void testGetAllUsers_NotFound() throws Exception {
        when(usuariosService.listUsuarios()).thenReturn(Collections.emptyList());
        
        mockMvc.perform(get("/auth/users"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testRegister() throws Exception {
        Usuarios usuario = new Usuarios();
        usuario.setRfc("ABC123");
        usuario.setTipoUsuario(Rol.ADMIN);
        when(usuariosService.registerAdmin(any())).thenReturn(usuario);
        
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rfc").value("ABC123"));
    }
    
    @Test
    void testUpdateUser() throws Exception {
        Usuarios usuario = new Usuarios();
        usuario.setRfc("ABC123");
        when(usuariosService.updatedUser(any())).thenReturn(usuario);
        
        mockMvc.perform(put("/auth/updated-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rfc").value("ABC123"));
    }
    
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(usuariosService).deleteUser("ABC123");
        
        mockMvc.perform(delete("/auth/delete-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("rfc", "ABC123"))))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario eliminado correctamente"));
    }

}
