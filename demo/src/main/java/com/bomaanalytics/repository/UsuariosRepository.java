package com.bomaanalytics.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long>{
	
	@Query("SELECT u FROM Usuarios u WHERE u.rfc = :rfc")
	public Optional<Usuarios> findUserByRFC(@Param("rfc") String rfc);
	
	public List<Usuarios> findByTipoUsuario(Rol tipoUsuario);
}
