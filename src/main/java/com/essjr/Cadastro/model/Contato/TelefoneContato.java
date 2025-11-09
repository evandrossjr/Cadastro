package com.essjr.Cadastro.model.Contato;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "telefoneContato_tb")
public class TelefoneContato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(
            regexp = "^(\\d{10,11})?$",
            message = "Preencha um telefone válido (com DDD)"
    )
    @Column(nullable = false)
    private String numero;

    @ManyToOne
    @JoinColumn(name = "contato_id")
    private Contato contato;


    // GETTERS E SETTERS

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        if (numero != null) {
            // remove tudo que não é número antes de persistir
            this.numero = numero.replaceAll("\\D", "");
        } else {
            this.numero = null;
        }
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }
}
