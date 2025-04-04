package com.bomaanalytics.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bomaanalytics.domain.Campanias;
import com.bomaanalytics.domain.Campanias.Status;
import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.exceptions.CampaniaAlredyExistException;
import com.bomaanalytics.repository.CampaniaRepository;
import com.bomaanalytics.repository.UsuariosRepository;

@Service
public class CampaniasService {
    
	private final CampaniaRepository campaniasRepository;
    private final UsuariosRepository usuarioRepository;

    public CampaniasService(CampaniaRepository campaniasRepository, UsuariosRepository usuarioRepository) {
		super();
		this.campaniasRepository = campaniasRepository;
		this.usuarioRepository = usuarioRepository;
	}

	public List<Campanias> getAllCampanias() {
    	List<Campanias> findCampanias = campaniasRepository.findAll();
    	
    	if(!findCampanias.isEmpty()) {
    		return findCampanias;
    	}else {
    		throw new CampaniaAlredyExistException("Lista Vacia");
    	}
    }
	
	public List<Campanias> getCampaniasByStatus(Status status){
		
		List<Campanias> campanias = null;
		
		if(status.equals(Status.ACTIVA)) {
			campanias = campaniasRepository.findByStatusCampania(Status.ACTIVA);
			if(!campanias.isEmpty()){
				return campanias;
			}else {
				throw new CampaniaAlredyExistException("Campania no encontrada");
			}
		}
		
		if(status.equals(Status.PAUSADA)) {
			campanias = campaniasRepository.findByStatusCampania(Status.PAUSADA);
			if(!campanias.isEmpty()){
				return campanias;
			}else {
				throw new CampaniaAlredyExistException("Campania no encontrada");
			}
		}
		
		if(status.equals(Status.FINALIZADA)) {
			campanias = campaniasRepository.findByStatusCampania(Status.FINALIZADA);
			if(!campanias.isEmpty()){
				return campanias;
			}else {
				throw new CampaniaAlredyExistException("Campania no encontrada");
			}
		}
		
		return campanias;
		
	}

    public Optional<Campanias> getCampaniaByNombre(String nombreCampania) {
        List<Campanias> campanias = campaniasRepository.findByNombreCampania(nombreCampania);
        return campanias.isEmpty() ? Optional.empty() : Optional.of(campanias.get(0));
    }

    public Campanias createCampania(Campanias campania) {
        validateCampania(campania);
        
        Usuarios usuarioActual = obtenerUsuarioAutenticado();

        if (!campaniasRepository.findByNombreCampania(campania.getNombreCampania()).isEmpty()) {
            throw new CampaniaAlredyExistException("El nombre de la campaña ya existe");
        }
        
        campania.setStatusCampania(Status.ACTIVA);
        campania.setUsuarios(usuarioActual);

        return campaniasRepository.save(campania);
    }

    public Campanias updateCampania(String nombreCampania, Campanias campaniaDetails) {
        validateCampania(campaniaDetails);
        
        Usuarios usuarioActual = obtenerUsuarioAutenticado();

        Campanias existingCampania = campaniasRepository.findByNombreCampania(nombreCampania)
                .stream().findFirst()
                .orElseThrow(() -> new CampaniaAlredyExistException("Campaña no encontrada"));

        if (!existingCampania.getNombreCampania().equals(campaniaDetails.getNombreCampania()) &&
            !campaniasRepository.findByNombreCampania(campaniaDetails.getNombreCampania()).isEmpty()) {
            throw new CampaniaAlredyExistException("El nombre de la campaña ya existe");
        }

        existingCampania.setIdCampania(existingCampania.getIdCampania());
        existingCampania.setDescripcion(campaniaDetails.getDescripcion());
        existingCampania.setFechaInicio(campaniaDetails.getFechaInicio());
        existingCampania.setFechaFinal(campaniaDetails.getFechaFinal());
        existingCampania.setPresupuesto(campaniaDetails.getPresupuesto());
        existingCampania.setTipoCanal(campaniaDetails.getTipoCanal());
        existingCampania.setResultadoEsperado(campaniaDetails.getResultadoEsperado());
        
        if(campaniaDetails.getStatusCampania().equals(Status.ACTIVA)) {
        	existingCampania.setStatusCampania(Status.ACTIVA);
        }
        
        if(campaniaDetails.getStatusCampania().equals(Status.PAUSADA)) {
        	existingCampania.setStatusCampania(Status.PAUSADA);
        }
        
        if(campaniaDetails.getStatusCampania().equals(Status.FINALIZADA)) {
        	existingCampania.setStatusCampania(Status.FINALIZADA);
        }
        
        existingCampania.setUsuarios(usuarioActual);
        

        return campaniasRepository.save(existingCampania);
    }

    public void deleteCampania(String nombreCampania) {
        Campanias campania = campaniasRepository.findByNombreCampania(nombreCampania)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Campaña no encontrada"));

        campaniasRepository.delete(campania);
    }
    
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        List<Campanias> allCampaigns = campaniasRepository.findAll();
        long totalCampaigns = allCampaigns.size();
        long activeCampaigns = allCampaigns.stream().filter(c -> "ACTIVA".equals(c.getStatusCampania())).count();
        long pausedCampaigns = allCampaigns.stream().filter(c -> "PAUSADA".equals(c.getStatusCampania())).count();
        long finishedCampaigns = allCampaigns.stream().filter(c -> "FINALIZADA".equals(c.getStatusCampania())).count();

        double totalBudget = allCampaigns.stream().mapToDouble(Campanias::getPresupuesto).sum();
        double avgBudget = totalCampaigns > 0 ? totalBudget / totalCampaigns : 0;

        metrics.put("totalCampañas", totalCampaigns);
        metrics.put("campañasActivas", activeCampaigns);
        metrics.put("campañasPausadas", pausedCampaigns);
        metrics.put("campañasFinalizadas", finishedCampaigns);
        metrics.put("presupuestoTotal", totalBudget);
        metrics.put("presupuestoPromedio", avgBudget);

        return metrics;
    }
    
    public Double calcularAlcanceEstimado(Campanias campania) {
        // Simulación de lógica para calcular el alcance estimado
        return campania.getPresupuesto() * 100; // Ejemplo simple basado en presupuesto
    }
    
    public int calcularConversiones(Campanias campania) {
        // Simulación: Conversiones pueden depender del presupuesto o alcance
        return (int) (calcularAlcanceEstimado(campania) * 0.02); 
    }

    public double calcularCostoPorConversion(Campanias campania) {
        int conversiones = calcularConversiones(campania);
        return conversiones > 0 ? (double) campania.getPresupuesto() / conversiones : 0;
    }

    private void validateCampania(Campanias campania) {
        if (!StringUtils.hasText(campania.getNombreCampania())) {
            throw new IllegalArgumentException("El nombre de la campaña es obligatorio");
        }
        if (campania.getFechaInicio() == null || campania.getFechaFinal() == null) {
            throw new IllegalArgumentException("Las fechas de inicio y final son obligatorias");
        }
        if (campania.getFechaInicio().after(campania.getFechaFinal())) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha final");
        }
        if (campania.getPresupuesto() == null || campania.getPresupuesto() <= 0) {
            throw new IllegalArgumentException("El presupuesto debe ser mayor a 0");
        }
    }
    
    private Usuarios obtenerUsuarioAutenticado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return usuarioRepository.findUserByRFC(username)
                    .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
        } else {
            throw new RuntimeException("No se pudo obtener el usuario autenticado");
        }
    }
}

