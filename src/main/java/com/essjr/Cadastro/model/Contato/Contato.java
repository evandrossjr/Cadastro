package com.essjr.Cadastro.model.Contato;


import com.essjr.Cadastro.model.Cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Contato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+\\s+[A-Za-zÀ-ÖØ-öø-ÿ].*$",
            message = "Preencha o nome completo"
    )
    @Column(nullable = false)
    private String nomeCompleto;

    @Pattern(
            regexp = "^(\\d{10,11})?$",
            message = "Preencha um telefone válido (com DDD)"
    )
    @Column(nullable = true)
    private String telefone;

    @Pattern(
            regexp = "^(\\d{10,11})?$",
            message = "Preencha um telefone válido (com DDD)"
    )
    @Column(nullable = true)
    private String telefoneAdicional;

    private String email;

    private String emailAdicional;

    //CONSTRUTOR
    public Contato() {
    }

    public Contato(Long id, String nomeCompleto, String telefone, String telefoneAdicional, String email, String emailAdicional) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
        this.telefoneAdicional = telefoneAdicional;
        this.email = email;
        this.emailAdicional = emailAdicional;
    }

    // GETTERS E SETTERS


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null) {
            this.telefone = telefone.replaceAll("\\D", "");
        } else {
            this.telefone = null;
        }
    }

    public String getTelefoneAdicional() {
        return telefoneAdicional;
    }

    public void setTelefoneAdicional(String telefoneAdicional) {
        if (telefoneAdicional != null) {
            this.telefoneAdicional = telefoneAdicional.replaceAll("\\D", "");
        } else {
            this.telefoneAdicional = null;
        }    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailAdicional() {
        return emailAdicional;
    }

    public void setEmailAdicional(String emailAdicional) {
        this.emailAdicional = emailAdicional;
    }
}
