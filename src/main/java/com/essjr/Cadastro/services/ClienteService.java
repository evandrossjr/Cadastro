package com.essjr.Cadastro.services;


import com.essjr.Cadastro.dtos.ClienteDTO;
import com.essjr.Cadastro.exceptions.ResourceNotFoundException;
import com.essjr.Cadastro.model.Cliente;
import com.essjr.Cadastro.model.Contato;
import com.essjr.Cadastro.repositories.ClienteRepository;
import com.essjr.Cadastro.repositories.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        return ClienteDTO.fromEntity(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getAll(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(ClienteDTO::fromEntity).collect(Collectors.toList());
    }






}
