package com.essjr.Cadastro.dtos;

import com.essjr.Cadastro.model.Cliente;
import com.essjr.Cadastro.model.EmailCadastro;
import com.essjr.Cadastro.model.Telefone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


import java.time.LocalDate;
import java.util.List;

public class ClienteDTO {

    private Long id;

    @NotBlank
    @Pattern(
            regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+\\s+[A-Za-zÀ-ÖØ-öø-ÿ].*$",
            message = "Preencha o nome completo"
    )
    private String nomeCompleto;

    @NotEmpty
    private List<@Pattern(
            regexp = "^\\(?\\d{2}\\)?\\s?(9?\\d{4})-?\\d{4}$",
            message = "Preencha o telefone no formato (DD) XXXXX-XXXX"
    ) String> telefones;

    @NotEmpty
    private List<@Email String> emails;

    private LocalDate dataRegistro;

    private List<ContatoDTO> contatos;

    public ClienteDTO() {
    }

    public static ClienteDTO fromEntity(Cliente cliente){
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNomeCompleto(cliente.getNomeCompleto());
        dto.setTelefones(cliente.getTelefones().stream().map(Telefone::getNumero).toList());
        dto.setEmails(cliente.getEmails().stream().map(EmailCadastro::getEndereco).toList());
        dto.setDataRegistro(cliente.getDataRegistro());

        if (cliente.getContatos() != null){
            dto.setContatos(cliente.getContatos().stream().map(ContatoDTO::fromEntity).toList());

        }

        return dto;
    }


    //Getters & Setters
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

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public LocalDate getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public List<ContatoDTO> getContatos() {
        return contatos;
    }

    public void setContatos(List<ContatoDTO> contatos) {
        this.contatos = contatos;
    }
}
