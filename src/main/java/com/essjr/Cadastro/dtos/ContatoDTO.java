package com.essjr.Cadastro.dtos;

import com.essjr.Cadastro.model.Contato;
import com.essjr.Cadastro.model.EmailCadastro;
import com.essjr.Cadastro.model.Telefone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public class ContatoDTO {

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

    private Long clienteId;

    public static ContatoDTO fromEntity(Contato contato){
        ContatoDTO dto = new ContatoDTO();
        dto.setId(contato.getId());
        dto.setNomeCompleto(contato.getNomeCompleto());
        dto.setTelefones(contato.getTelefones().stream().map(Telefone::getNumero).toList());
        dto.setEmails(contato.getEmails().stream().map(EmailCadastro::getEndereco).toList());

        if  (contato.getCliente() != null) {
            dto.setClienteId(contato.getCliente().getId());
        }

        return dto;
    }

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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}
