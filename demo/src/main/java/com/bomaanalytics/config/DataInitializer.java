package com.bomaanalytics.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.bomaanalytics.domain.Usuarios;
import com.bomaanalytics.domain.Usuarios.Rol;
import com.bomaanalytics.repository.UsuariosRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuariosRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataInitializer(UsuariosRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) {
        Rol superAdminTypeUser = Rol.SUPER_ADMIN;

        if (usuarioRepository.findByTipoUsuario(superAdminTypeUser).isEmpty()) {
            Usuarios superAdmin = new Usuarios();
            superAdmin.setNombreUsuario("admin");
            superAdmin.setPassword(passwordEncoder.encode("12345")); // Puedes cambiar esta contraseña
            superAdmin.setTipoUsuario(Rol.SUPER_ADMIN);
            superAdmin.setRfc("ROOT25");

            usuarioRepository.save(superAdmin);
            System.out.println("Super administrador creado correctamente.");
        } else {
            System.out.println("Ya existe un super administrador.");
        }
    }
}
