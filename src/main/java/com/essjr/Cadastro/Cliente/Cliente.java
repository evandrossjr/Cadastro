package com.essjr.Cadastro.Cliente;

import com.essjr.Cadastro.Contato.Contato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Cliente {


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

    @CreationTimestamp // <-- Use isso
    @Column(name = "data_registro", nullable = false, updatable = false)
    private LocalDate dataRegistro = LocalDate.now();

    @ManyToMany // (Ou @OneToMany, dependendo da sua regra de negócio)
    @JoinTable(
            name = "cliente_contato",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "contato_id")
    )
    private Set<Contato> contatos = new HashSet<>();

    //CONSTRUTOR
    public Cliente() {
    }

    public Cliente(Long id, String nomeCompleto, String telefone, String telefoneAdicional, String email, String emailAdicional, LocalDate dataRegistro) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.telefone = telefone;
        this.telefoneAdicional = telefoneAdicional;
        this.email = email;
        this.emailAdicional = emailAdicional;
        this.dataRegistro = dataRegistro;
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

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public Set<Contato> getContatos() {
        return contatos;
    }

    public void setContatos(Set<Contato> contatos) {
        this.contatos = contatos;
    }
}
