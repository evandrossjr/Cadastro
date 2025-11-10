package com.essjr.Cadastro.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;



public class EditarUsuarioDTO {

    // (Você pode querer desabilitar a edição de e-mail
    // ou adicionar validação extra se ele for alterado)
    @NotBlank
    @Email
    private String email;

    @NotBlank(message = "A senha atual é obrigatória.")
    private String password; // Senha Atual

    // A nova senha é opcional.
    // O usuário só preenche se quiser mudar.
    private String newPassword;

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

