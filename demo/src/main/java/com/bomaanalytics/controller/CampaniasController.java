package com.bomaanalytics.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.bomaanalytics.domain.Campanias;
import com.bomaanalytics.service.CampaniasService;

@RestController
@RequestMapping("/campanias")
public class CampaniasController {
    private final CampaniasService campaniasService;

    public CampaniasController(CampaniasService campaniasService) {
        this.campaniasService = campaniasService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllCampanias() {
        List<Campanias> request = campaniasService.getAllCampanias();
    	try {
			if(!request.isEmpty()) {
				return new ResponseEntity<Object>(request, HttpStatus.OK);
			}else {
				return new ResponseEntity<Object>(request, HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<Object>("message: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/find-by-name")
    public ResponseEntity<?> getCampaniaByNombre(@RequestBody Campanias campania) {
        Optional<Campanias> foundCampania = campaniasService.getCampaniaByNombre(campania.getNombreCampania());
        if (foundCampania.isPresent()) {
            return ResponseEntity.ok(foundCampania.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaña no encontrada");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCampania(@RequestBody Campanias campania) throws IllegalArgumentException {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(campaniasService.createCampania(campania));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateCampania(@RequestBody Campanias campania) throws IllegalArgumentException {
        try {
            return ResponseEntity.ok(campaniasService.updateCampania(campania.getNombreCampania(), campania));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCampania(@RequestBody Campanias campania) {
        try {
            campaniasService.deleteCampania(campania.getNombreCampania());
            return ResponseEntity.ok("Campaña eliminada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/filter")
    public ResponseEntity<?> getCampaniasByEstado(@RequestBody Campanias estado) {
        return ResponseEntity.ok(campaniasService.getCampaniasByStatus(estado.getStatusCampania()));
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardMetrics() {
        return ResponseEntity.ok(campaniasService.getDashboardMetrics());
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/landing")
    public ResponseEntity<?> getLandingData(@RequestBody Campanias requestCampania) {
        Optional<Campanias> campania = campaniasService.getCampaniaByNombre(requestCampania.getNombreCampania());

        if (campania.isPresent()) {
            Campanias campaniaData = campania.get();

            // Construcción del response con métricas clave
            Map<String, Object> response = new HashMap<>();
            response.put("nombre", campaniaData.getNombreCampania());
            response.put("descripcion", campaniaData.getDescripcion());
            response.put("fecha_creacion", campaniaData.getFechaCreacion());
            response.put("estado", campaniaData.getStatusCampania());
            response.put("presupuesto", campaniaData.getPresupuesto());
            
            // Cálculo de métricas
            response.put("alcance_estimado", campaniasService.calcularAlcanceEstimado(campaniaData));
            response.put("conversiones", campaniasService.calcularConversiones(campaniaData));
            response.put("costo_por_conversion", campaniasService.calcularCostoPorConversion(campaniaData));

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaña no encontrada");
        }
    }
}

