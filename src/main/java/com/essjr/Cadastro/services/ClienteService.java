package com.essjr.Cadastro.services;


import com.essjr.Cadastro.dtos.ClienteDTO;
import com.essjr.Cadastro.exceptions.ResourceNotFoundException;
import com.essjr.Cadastro.model.Cliente;
import com.essjr.Cadastro.model.Contato;
import com.essjr.Cadastro.model.EmailCadastro;
import com.essjr.Cadastro.model.Telefone;
import com.essjr.Cadastro.repositories.ClienteRepository;
import com.essjr.Cadastro.repositories.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
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

    @Transactional
    public ClienteDTO save(ClienteDTO dto){
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(dto.getNomeCompleto());
        cliente.setDataRegistro(dto.getDataRegistro());

        if (dto.getTelefones() != null) {
            Set<Telefone> tels = dto.getTelefones().stream().map(num -> {
                Telefone telefone = new Telefone();
                telefone.setNumero(num);
                telefone.setPessoa(cliente);
                return telefone;
            }).collect(Collectors.toSet());
            cliente.setTelefones(tels);
        }

        if (dto .getEmails() != null) {
            Set<EmailCadastro> emls = dto.getEmails().stream().map(eml -> {
                EmailCadastro email = new EmailCadastro();
                email.setEndereco(eml);
                email.setPessoa(cliente);
                return email;
            }).collect(Collectors.toSet());
            cliente.setEmails(emls);
        }

        //Dados do Contato
        if (dto.getContatos() != null) {
            Set<Contato> contatos = dto.getContatos().stream().map(ctt -> {
                Contato contato = new Contato();
                contato.setNomeCompleto((ctt.getNomeCompleto()));
                contato.setCliente(cliente);

                if (ctt.getTelefones() != null) {
                    Set<Telefone> telefones = ctt.getTelefones().stream().map(num -> {
                        Telefone telefone = new Telefone();
                        telefone.setNumero(num);
                        telefone.setPessoa(contato);
                        return telefone;
                    }).collect(Collectors.toSet());
                    contato.setTelefones(telefones);
                }

                if (ctt.getEmails() != null) {
                    Set<EmailCadastro> emails = ctt.getEmails().stream().map(eml -> {
                        EmailCadastro email = new EmailCadastro();
                        email.setEndereco(eml);
                        email.setPessoa(contato);
                        return email;
                    }).collect(Collectors.toSet());
                    contato.setEmails(emails);
                }

                return contato;
            }).collect(Collectors.toSet());

            cliente.setContatos(contatos);
        }

        clienteRepository.save(cliente);

        return ClienteDTO.fromEntity(cliente);
    }



    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado na nossa base de dados."));

        cliente.setNomeCompleto(dto.getNomeCompleto());


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
