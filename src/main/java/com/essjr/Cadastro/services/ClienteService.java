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


    @Transactional(readOnly = true)
    public Optional<ClienteDTO> findById(Long id){
        Optional<ClienteDTO> cliente = clienteRepository.findById(id).map(ClienteDTO::fromEntity);

        return cliente;
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getAll(){
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream().map(ClienteDTO::fromEntity).collect(Collectors.toList());
    }


    @Transactional
    public ClienteDTO save(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(dto.getNomeCompleto());
        cliente.setDataRegistro(LocalDate.now());

        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(num -> {
                TelefoneCliente telefone = new TelefoneCliente();
                telefone.setNumero(num);
                telefone.setCliente(cliente);
                cliente.getTelefones().add(telefone);
            });
        }

        if (dto.getEmails() != null) {
            dto.getEmails().forEach(eml -> {
                EmailCliente email = new EmailCliente();
                email.setEndereco(eml);
                email.setCliente(cliente);
                cliente.getEmails().add(email);
            });
        }

        if (dto.getContatos() != null) {
            dto.getContatos().forEach(ctt -> {
                Contato contato = new Contato();
                contato.setNomeCompleto(ctt.getNomeCompleto());
                contato.setCliente(cliente);

                if (ctt.getTelefones() != null) {
                    ctt.getTelefones().forEach(num -> {
                        TelefoneContato telefone = new TelefoneContato();
                        telefone.setNumero(num);
                        telefone.setContato(contato);
                        contato.getTelefones().add(telefone);
                    });
                }

                if (ctt.getEmails() != null) {
                    ctt.getEmails().forEach(eml -> {
                        EmailContato email = new EmailContato();
                        email.setEndereco(eml);
                        email.setContato(contato);
                        contato.getEmails().add(email);
                    });
                }
                cliente.getContatos().add(contato);
            });

        }
        clienteRepository.save(cliente);

        return ClienteDTO.fromEntity(cliente);
    }



    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado na nossa base de dados."));

        if (dto.getNomeCompleto() != null && !dto.getNomeCompleto().isBlank()) {
            cliente.setNomeCompleto(dto.getNomeCompleto());
        }

        if (dto.getTelefones() != null) {
            dto.getTelefones().forEach(num -> {
                boolean exists = cliente.getTelefones().stream().anyMatch(t -> t.getNumero().equals(num));
                if (!exists) {
                    TelefoneCliente telefoneNovo = new TelefoneCliente();
                    telefoneNovo.setNumero(num);
                    telefoneNovo.setCliente(cliente);
                    cliente.getTelefones().add(telefoneNovo);
                }
            });
        }

        if (dto.getEmails() != null) {
            dto.getEmails().forEach(eml -> {
                boolean exists = cliente.getEmails().stream().anyMatch(e -> e.getEndereco().equals(eml));
                if (!exists) {
                    EmailCliente emailNovo = new EmailCliente();
                    emailNovo.setEndereco(eml);
                    emailNovo.setCliente(cliente);
                    cliente.getEmails().add(emailNovo);
                }
            });
        }


        if (dto.getContatos() != null) {
            for (ContatoDTO ctDTO : dto.getContatos()) {
                Contato contato = cliente.getContatos().stream()
                        .filter(c -> c.getNomeCompleto().equals(ctDTO.getNomeCompleto()))
                        .findFirst().orElseGet(() -> {
                            Contato contatoNovo = new Contato();
                            contatoNovo.setNomeCompleto(ctDTO.getNomeCompleto());
                            contatoNovo.setCliente(cliente);
                            cliente.getContatos().add(contatoNovo);
                            return contatoNovo;
                        });

                if (ctDTO.getTelefones() != null) {
                    for (String num : ctDTO.getTelefones()) {
                        boolean existsTel = contato.getTelefones().stream().anyMatch(t -> t.getNumero().equals(num));
                        if (!existsTel) {
                            TelefoneContato telefoneNovo = new TelefoneContato();
                            telefoneNovo.setNumero(num);
                            telefoneNovo.setContato(contato);
                            contato.getTelefones().add(telefoneNovo);
                        }
                    }
                }

                if (ctDTO.getEmails() != null) {
                    for (String eml : ctDTO.getEmails()) {
                        boolean existsEmail = contato.getEmails().stream().anyMatch(t -> t.getEndereco().equals(eml));
                        if (!existsEmail) {
                            EmailContato emailNovo = new EmailContato();
                            emailNovo.setEndereco(eml);
                            emailNovo.setContato(contato);
                            contato.getEmails().add(emailNovo);
                        }
                    }
                }

            }
        }

        return ClienteDTO.fromEntity(cliente);

    }

    @Transactional
    public void delete(Long id) {
        if(!clienteRepository.existsById(id)){
            throw new EntityNotFoundException("Cliente não encontrado na nossa base de dados.");
        }

        clienteRepository.deleteById(id);
    }




}
