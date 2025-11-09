package com.essjr.Cadastro.model.Contato.mapper;

import com.essjr.Cadastro.model.Contato.Contato;
import com.essjr.Cadastro.model.Contato.dtos.ContatoDTO;

public class ContatoMapper {

    // Entidade → DTO
    public static ContatoDTO toDTO(Contato contato) {
        return new ContatoDTO(
                contato.getId(),
                contato.getNomeCompleto(),
                contato.getTelefone(),
                contato.getTelefoneAdicional(),
                contato.getEmail(),
                contato.getEmailAdicional()
        );
    }

    // DTO → Entidade
    public static Contato toEntity(ContatoDTO dto) {
        Contato contato = new Contato();
        contato.setId(dto.id());
        contato.setNomeCompleto(dto.nomeCompleto());
        contato.setTelefone(dto.telefone());
        contato.setTelefoneAdicional(dto.telefoneAdicional());
        contato.setEmail(dto.email());
        contato.setEmailAdicional(dto.emailAdicional());
        return contato;
    }
}
