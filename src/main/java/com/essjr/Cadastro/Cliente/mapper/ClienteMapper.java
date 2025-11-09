package com.essjr.Cadastro.Cliente.mapper;

import com.essjr.Cadastro.Cliente.Cliente;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;

public class ClienteMapper {


    // Entidade → DTO
    public static ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(
                cliente.getId(),
                cliente.getNomeCompleto(),
                cliente.getTelefone(),
                cliente.getTelefoneAdicional(),
                cliente.getEmail(),
                cliente.getEmailAdicional(),
                cliente.getDataRegistro()
        );
    }

    // DTO → Entidade
    public static Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.id());
        cliente.setNomeCompleto(dto.nomeCompleto());
        cliente.setTelefone(dto.telefone());
        cliente.setTelefoneAdicional(dto.telefoneAdicional());
        cliente.setEmail(dto.email());
        cliente.setEmailAdicional(dto.emailAdicional());
        cliente.setDataRegistro(dto.dataRegistro());
        return cliente;
    }
}
