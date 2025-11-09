package com.essjr.Cadastro.Cliente;


import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Cliente.mapper.ClienteMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

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

        Cliente clienteSalvo = clienteRepository.save(obj);
        return ClienteMapper.toDTO(clienteSalvo);
    }

    public void delete(Long id){
        clienteRepository.deleteById(id);
    }

    public ClienteDTO update(Long id, ClienteDTO dto){
        try{
            Cliente entity = clienteRepository
                    .findById(id).orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com o: " + id));

            Cliente updates = ClienteMapper.toEntity(dto);
            updateData(entity, updates);

            Cliente updated = clienteRepository.save(entity);
            return ClienteMapper.toDTO(updated);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar o Cliente: " + e.getMessage(), e);
        }
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNomeCompleto(obj.getNomeCompleto());
        entity.setTelefone(obj.getTelefone());
        entity.setTelefoneAdicional(obj.getTelefoneAdicional());
        entity.setEmail(obj.getEmail());
        entity.setEmailAdicional(obj.getEmailAdicional());

    }







}
