package com.essjr.Cadastro.services;


import com.essjr.Cadastro.model.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.model.Cliente.Cliente;
import com.essjr.Cadastro.model.Cliente.EmailCliente;
import com.essjr.Cadastro.model.Cliente.TelefoneCliente;
import com.essjr.Cadastro.model.Contato.Contato;
import com.essjr.Cadastro.model.Contato.EmailContato;
import com.essjr.Cadastro.model.Contato.TelefoneContato;
import com.essjr.Cadastro.model.Contato.dtos.ContatoDTO;
import com.essjr.Cadastro.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }






}
