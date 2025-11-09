package com.essjr.Cadastro.Cliente.mapper;

import com.essjr.Cadastro.Cliente.Cliente;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Contato.Contato;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClienteMapper {


    // Entidade → DTO
    public static ClienteDTO toDTO(Cliente cliente) {

        Set<Contato> contatosDaEntidade = cliente.getContatos();

        List<Long> idsDosContatos = new ArrayList<>();
        if (contatosDaEntidade != null) {
            idsDosContatos = contatosDaEntidade.stream()
                    .map(Contato::getId) // Pega o ID de cada contato
                    .toList();
        }
            return new ClienteDTO(
                    cliente.getId(),
                    cliente.getNomeCompleto(),
                    cliente.getEmail(),
                    cliente.getEmailAdicional(),
                    cliente.getTelefone(),
                    cliente.getTelefoneAdicional(),
                    cliente.getDataRegistro(),
                    idsDosContatos

            );
        }

    // DTO → Entidade
    public static Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.id());
        cliente.setNomeCompleto(dto.nomeCompleto());
        cliente.setEmail(dto.email());
        cliente.setEmailAdicional(dto.emailAdicional());
        cliente.setTelefone(dto.telefone());
        cliente.setTelefoneAdicional(dto.telefoneAdicional());
        return cliente;
    }
}
