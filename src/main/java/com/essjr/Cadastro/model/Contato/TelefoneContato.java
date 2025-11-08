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
            regexp = "^\\(?\\d{2}\\)?\\s?(9?\\d{4})-?\\d{4}$",
            message = "Preencha o telefone no formato (DD) XXXXX-XXXX"
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
        this.numero = numero;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }
}
