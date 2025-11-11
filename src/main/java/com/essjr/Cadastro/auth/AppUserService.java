package com.essjr.Cadastro.auth;

import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.AppUserRepository;
import com.essjr.Cadastro.appUser.dtos.AppUserRegistrationDTO;
import com.essjr.Cadastro.appUser.enums.AppUserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    // --- CORREÇÃO AQUI ---
    // Receba o PasswordEncoder como um parâmetro (Injeção de Dependência)
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder; // Use o bean injetado
    }

    public void registerUser(AppUserRegistrationDTO userDTO, AppUserRole role) {
        if (appUserRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalStateException("Email já cadastrado.");
        }

        AppUser newUser = new AppUser();
        newUser.setName(userDTO.name());
        newUser.setEmail(userDTO.email());
        // Agora isso usa o bean correto
        newUser.setPasswordHash(passwordEncoder.encode(userDTO.password()));
        newUser.setRole(role);

        appUserRepository.save(newUser);
    }
}
