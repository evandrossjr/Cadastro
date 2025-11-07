package com.essjr.Cadastro.model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Contato extends Pessoa{


    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    public Contato() {
    }

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Telefone> telefones = new HashSet<>();

    @OneToMany(mappedBy = "contato", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EmailCadastro> emails = new HashSet<>();


    //Getters & Setters
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public Set<Telefone> getTelefones() {
        return telefones;
    }

    @Override
    public void setTelefones(Set<Telefone> telefones) {
        this.telefones = telefones;
    }

    @Override
    public Set<EmailCadastro> getEmails() {
        return emails;
    }

    @Override
    public void setEmails(Set<EmailCadastro> emails) {
        this.emails = emails;
    }
}
