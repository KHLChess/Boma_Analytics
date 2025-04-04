package com.bomaanalytics.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.bomaanalytics.domain.Campanias;
import com.bomaanalytics.domain.Campanias.Status;
import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;
import com.bomaanalytics.repository.CampaniaRepository;
import com.bomaanalytics.repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
public class CampaniasServiceTest {

	@Mock
	private CampaniaRepository campaniaRepository;

	@Mock
	private UsuariosRepository usuarioRepository;

	@InjectMocks
	private CampaniasService campaniasService;

	private Usuarios usuario;
	
	private Campanias campania1, campania2, campania3;

	@BeforeEach
	void setUp() {

		// Configurar usuario
		usuario = new Usuarios();
		usuario.setId(1L);
		usuario.setRfc("TEST123");
		usuario.setNombreUsuario("admin");
		usuario.setTipoUsuario(Rol.ADMIN);
		usuario.setPassword("securepassword");

		// Configurar campaña
		campania1 = new Campanias(1L, "Campaña 1", "Descripción 1", new Date(), new Date(), new Date(), new Date(),
				"Tipo 1", 1000.0, "TV", "1000 visitas", "Ninguna", usuario, Campanias.Status.ACTIVA);
		campania2 = new Campanias(2L, "Campaña 2", "Descripción 2", new Date(), new Date(), new Date(), new Date(),
				"Tipo 2", 2000.0, "Redes", "2000 visitas", "Ninguna", usuario, Campanias.Status.PAUSADA);
		campania3 = new Campanias(3L, "Campaña 3", "Descripción 3", new Date(), new Date(), new Date(), new Date(),
				"Tipo 3", 2000.0, "Redes", "2000 visitas", "Ninguna", usuario, Campanias.Status.FINALIZADA);

	}
	
	private void mockSecurityContext() {
        UserDetails userDetails = new User("TEST123", "password", new ArrayList<>());
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(mock(org.springframework.security.core.Authentication.class));
        when(securityContext.getAuthentication().getPrincipal()).thenReturn(userDetails);
    }

	@Test
	public void testGetAllCampanias() {
		lenient().when(campaniaRepository.findAll()).thenReturn(Collections.emptyList());

		List<Campanias> campanias = campaniasService.getAllCampanias();

		assertNotNull(campanias);
		assertTrue(campanias.isEmpty()); // Asegura que se maneje bien la lista vacía
	}

    @Test
    void testGetCampaniasByStatus() {
    	when(campaniaRepository.findByStatusCampania(Status.ACTIVA))
        .thenReturn(List.of(campania1));
    	
	    List<Campanias> resultado = campaniasService.getCampaniasByStatus(Status.ACTIVA);
	   
	    assertNotNull(resultado, "La lista de campañas no debería ser nula");
	    assertFalse(resultado.isEmpty(), "La lista de campañas no debería estar vacía");
	    assertEquals(1, resultado.size(), "La lista debería contener exactamente una campaña");
	    assertEquals(Status.ACTIVA, resultado.get(0).getStatusCampania(), "El estado de la campaña no coincide");
	}

	@Test
    void testGetCampaniasByStatus_Pausada() {

        when(campaniaRepository.findByStatusCampania(Status.PAUSADA))
            .thenReturn(List.of(campania2));
       
        List<Campanias> resultado = campaniasService.getCampaniasByStatus(Status.PAUSADA);
                
        assertNotNull(resultado, "La lista de campañas no debería ser nula");
        assertFalse(resultado.isEmpty(), "La lista de campañas no debería estar vacía");
        assertEquals(1, resultado.size(), "La lista debería contener exactamente una campaña");
        assertEquals(Status.PAUSADA, resultado.get(0).getStatusCampania(), "El estado de la campaña no coincide");
    }
	
	@Test
    void testGetCampaniasByStatus_Finalizada() {

        when(campaniaRepository.findByStatusCampania(Status.FINALIZADA))
            .thenReturn(List.of(campania3));
        
        List<Campanias> resultado = campaniasService.getCampaniasByStatus(Status.FINALIZADA);       
        
        assertNotNull(resultado, "La lista de campañas no debería ser nula");
        assertFalse(resultado.isEmpty(), "La lista de campañas no debería estar vacía");
        assertEquals(1, resultado.size(), "La lista debería contener exactamente una campaña");
        assertEquals(Status.FINALIZADA, resultado.get(0).getStatusCampania(), "El estado de la campaña no coincide");
    }
	
	@Test
    void testGetCampaniaByNombre() {
        when(campaniaRepository.findByNombreCampania("Campaña 1")).thenReturn(Collections.singletonList(campania1));
        Optional<Campanias> result = campaniasService.getCampaniaByNombre("Campaña 1");
        assertTrue(result.isPresent());
        assertEquals("Campaña 1", result.get().getNombreCampania());
    }
	
	@Test
    void testCreateCampania() {
        mockSecurityContext();
        when(usuarioRepository.findUserByRFC("TEST123")).thenReturn(Optional.of(usuario));
        when(campaniaRepository.findByNombreCampania("Campaña 1")).thenReturn(Collections.emptyList());
        when(campaniaRepository.save(any(Campanias.class))).thenReturn(campania1);
        
        Campanias result = campaniasService.createCampania(campania1);
        assertNotNull(result);
        assertEquals(Status.ACTIVA, result.getStatusCampania());
    }
	
	@Test
    void testGetDashboardMetrics() {
        when(campaniaRepository.findAll()).thenReturn(Collections.singletonList(campania1));
        Map<String, Object> metrics = campaniasService.getDashboardMetrics();
        assertNotNull(metrics);
        assertEquals(1L, metrics.get("totalCampañas"));
    }

	@Test
    void testCalcularAlcanceEstimado() {
        double alcance = campaniasService.calcularAlcanceEstimado(campania1);
        assertEquals(100000.0, alcance);
    }
	
	@Test
    void testCalcularConversiones() {
        int conversiones = campaniasService.calcularConversiones(campania1);
        assertEquals(2000, conversiones);
    }
	
	@Test
    void testCalcularCostoPorConversion() {
        double costoPorConversion = campaniasService.calcularCostoPorConversion(campania1);
        assertEquals(0.5, costoPorConversion);
    }

}
