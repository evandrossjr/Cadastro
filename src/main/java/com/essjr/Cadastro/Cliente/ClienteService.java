package com.essjr.Cadastro.Cliente;


import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Cliente.mapper.ClienteMapper;
import com.essjr.Cadastro.Contato.Contato;
import com.essjr.Cadastro.Contato.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContatoRepository contatoRepository;

    public List<ClienteDTO> findAll(){

        return clienteRepository.findAll().stream().map(ClienteMapper::toDTO).toList();
    }

    public ClienteDTO findById(Long id){
        Cliente cliente = clienteRepository.findById(id).
                orElseThrow(()-> new ResourceAccessException("Cliente não encontrado"));
        return ClienteMapper.toDTO(cliente);
    }

    public ClienteDTO insert(ClienteDTO dto){
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Cliente obj = ClienteMapper.toEntity(dto);
        obj.setDataRegistro(LocalDate.now());

        obj.getContatos().clear();

        if (dto.contatosIds() != null && !dto.contatosIds().isEmpty()) {
            // Busca todos os contatos da lista de IDs
            List<Contato> contatosSelecionados = contatoRepository.findAllById(dto.contatosIds());

            // Adiciona os contatos encontrados ao cliente
            // (Assumindo que você tem um 'Set<Contato> contatos' na sua entidade Cliente)
            obj.setContatos(new HashSet<>(contatosSelecionados));
        }

        Cliente clienteSalvo = clienteRepository.save(obj);
        return ClienteMapper.toDTO(clienteSalvo);
    }

    public void delete(Long id){
        clienteRepository.deleteById(id);
    }

    @Transactional
    public ClienteDTO update(Long id, ClienteDTO dto) {


        Optional<Cliente> duplicata = clienteRepository.findByEmailAndIdNot(dto.email(), id);
        if (duplicata.isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado por outro usuário.");
        }

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado: " + id));

        cliente.setNomeCompleto(dto.nomeCompleto());
        cliente.setEmail(dto.email());
        cliente.setEmailAdicional(dto.emailAdicional());
        cliente.setTelefone(dto.telefone());
        cliente.setTelefoneAdicional(dto.telefoneAdicional());

        cliente.getContatos().clear();

        if (dto.contatosIds() != null && !dto.contatosIds().isEmpty()) {
            List<Contato> contatosSelecionados = contatoRepository.findAllById(dto.contatosIds());
            cliente.setContatos(new HashSet<>(contatosSelecionados));
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return ClienteMapper.toDTO(clienteSalvo);
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNomeCompleto(obj.getNomeCompleto());
        entity.setTelefone(obj.getTelefone());
        entity.setTelefoneAdicional(obj.getTelefoneAdicional());
        entity.setEmail(obj.getEmail());
        entity.setEmailAdicional(obj.getEmailAdicional());

    }







}
