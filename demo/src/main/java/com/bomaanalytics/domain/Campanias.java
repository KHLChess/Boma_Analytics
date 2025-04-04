package com.bomaanalytics.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "campanias")
public class Campanias {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idCampania;
	private String nombreCampania;
	private String descripcion;
	private Date fechaCreacion;
	private Date fechaInicio;
	private Date fechaActualizacion;
	private Date fechaFinal;	
	private String tipoCampania;
	private Double presupuesto;
	private String tipoCanal; //tipo de transmision que se va a presentar, ya sea por redes sociales, tv, etc
	private String resultadoEsperado; //KPIs que se espera alcanzar
	private String observaciones;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "id_usuario", nullable = false)
	private Usuarios usuarios;
	
	@Enumerated(EnumType.STRING)
	private Status statusCampania; //indica si se encuentra activo, inactivo o completada
	
	public enum Status {
		ACTIVA, PAUSADA, FINALIZADA
	}
	
	public Campanias() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Campanias(Long idCampania, String nombreCampania, String descripcion, Date fechaCreacion, Date fechaInicio,
			Date fechaActualizacion, Date fechaFinal, String tipoCampania, Double presupuesto, String tipoCanal,
			String resultadoEsperado, String observaciones, Usuarios usuarios, Status statusCampania) {
		super();
		this.idCampania = idCampania;
		this.nombreCampania = nombreCampania;
		this.descripcion = descripcion;
		this.fechaCreacion = fechaCreacion;
		this.fechaInicio = fechaInicio;
		this.fechaActualizacion = fechaActualizacion;
		this.fechaFinal = fechaFinal;
		this.tipoCampania = tipoCampania;
		this.presupuesto = presupuesto;
		this.tipoCanal = tipoCanal;
		this.resultadoEsperado = resultadoEsperado;
		this.observaciones = observaciones;
		this.usuarios = usuarios;
		this.statusCampania = statusCampania;
	}

	public Long getIdCampania() {
		return idCampania;
	}

	public void setIdCampania(Long idCampania) {
		this.idCampania = idCampania;
	}

	public String getNombreCampania() {
		return nombreCampania;
	}

	public void setNombreCampania(String nombreCampania) {
		this.nombreCampania = nombreCampania;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getTipoCampania() {
		return tipoCampania;
	}

	public void setTipoCampania(String tipoCampania) {
		this.tipoCampania = tipoCampania;
	}

	public Double getPresupuesto() {
		return presupuesto;
	}

	public void setPresupuesto(Double presupuesto) {
		this.presupuesto = presupuesto;
	}

	public String getTipoCanal() {
		return tipoCanal;
	}

	public void setTipoCanal(String tipoCanal) {
		this.tipoCanal = tipoCanal;
	}

	public String getResultadoEsperado() {
		return resultadoEsperado;
	}

	public void setResultadoEsperado(String resultadoEsperado) {
		this.resultadoEsperado = resultadoEsperado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Usuarios getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(Usuarios usuarios) {
		this.usuarios = usuarios;
	}

	public Status getStatusCampania() {
		return statusCampania;
	}

	public void setStatusCampania(Status statusCampania) {
		this.statusCampania = statusCampania;
	}

	@Override
	public String toString() {
		return "Campanias [idCampania=" + idCampania + ", nombreCampania=" + nombreCampania + ", descripcion="
				+ descripcion + ", fechaCreacion=" + fechaCreacion + ", fechaInicio=" + fechaInicio
				+ ", fechaActualizacion=" + fechaActualizacion + ", fechaFinal=" + fechaFinal + ", tipoCampania="
				+ tipoCampania + ", presupuesto=" + presupuesto + ", tipoCanal=" + tipoCanal + ", resultadoEsperado="
				+ resultadoEsperado + ", observaciones=" + observaciones + ", usuarios=" + usuarios
				+ ", statusCampania=" + statusCampania + ", getIdCampania()=" + getIdCampania()
				+ ", getNombreCampania()=" + getNombreCampania() + ", getDescripcion()=" + getDescripcion()
				+ ", getFechaCreacion()=" + getFechaCreacion() + ", getFechaInicio()=" + getFechaInicio()
				+ ", getFechaActualizacion()=" + getFechaActualizacion() + ", getFechaFinal()=" + getFechaFinal()
				+ ", getTipoCampania()=" + getTipoCampania() + ", getPresupuesto()=" + getPresupuesto()
				+ ", getTipoCanal()=" + getTipoCanal() + ", getResultadoEsperado()=" + getResultadoEsperado()
				+ ", getObservaciones()=" + getObservaciones() + ", getUsuarios()=" + getUsuarios()
				+ ", getStatusCampania()=" + getStatusCampania() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
	
}
