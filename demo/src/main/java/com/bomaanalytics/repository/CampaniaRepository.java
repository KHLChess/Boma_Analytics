package com.bomaanalytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bomaanalytics.domain.Campanias;
import com.bomaanalytics.domain.Campanias.Status;

public interface CampaniaRepository extends JpaRepository<Campanias, Long>{
	
	List<Campanias> findByNombreCampania(String nombreCampania);

	List<Campanias> findByStatusCampania(Status estado);
}
