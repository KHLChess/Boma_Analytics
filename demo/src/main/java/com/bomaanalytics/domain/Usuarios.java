package com.bomaanalytics.domain;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class Usuarios implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String rfc;
	private String nombreUsuario;
	
	@Enumerated(EnumType.STRING)
	private Rol tipoUsuario;
	private String password;
	
	@OneToMany(mappedBy = "usuarios", cascade = CascadeType.ALL)
	private Set<Campanias> campanias;
	
	public enum Rol {
		SUPER_ADMIN, ADMIN;
	}
	
	public Usuarios() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Usuarios(Long id, String rfc, String nombreUsuario, Rol tipoUsuario, String password,
			Set<Campanias> campanias) {
		super();
		this.id = id;
		this.rfc = rfc;
		this.nombreUsuario = nombreUsuario;
		this.tipoUsuario = tipoUsuario;
		this.password = password;
		this.campanias = campanias;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public Rol getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(Rol tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Campanias> getCampanias() {
		return campanias;
	}

	public void setCampanias(Set<Campanias> campanias) {
		this.campanias = campanias;
	}

	@Override
	public String toString() {
		return "Usuarios [id=" + id + ", rfc=" + rfc + ", nombreUsuario=" + nombreUsuario + ", tipoUsuario="
				+ tipoUsuario + ", password=" + password + ", campanias=" + campanias + ", getId()=" + getId()
				+ ", getRfc()=" + getRfc() + ", getNombreUsuario()=" + getNombreUsuario() + ", getTipoUsuario()="
				+ getTipoUsuario() + ", getPassword()=" + getPassword() + ", getCampanias()=" + getCampanias()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return List.of(new SimpleGrantedAuthority("ROLE_" + tipoUsuario.name()));
	}

    @Override
    public String getUsername() {
        return rfc;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
	
}
