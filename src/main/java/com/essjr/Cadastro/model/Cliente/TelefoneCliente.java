package com.essjr.Cadastro.model.Cliente;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "telefoneCliente_tb")
public class TelefoneCliente {

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
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;


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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
