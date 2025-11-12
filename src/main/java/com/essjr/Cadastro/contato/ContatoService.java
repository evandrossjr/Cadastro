package com.essjr.Cadastro.contato;

import com.essjr.Cadastro.cliente.Cliente;
import com.essjr.Cadastro.cliente.ClienteRepository;
import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import com.essjr.Cadastro.contato.mapper.ContatoMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;

@Service
public class ContatoService {


    private final ContatoRepository contatoRepository;
    private final ClienteRepository clienteRepository;

    public ContatoService(ContatoRepository contatoRepository, ClienteRepository clienteRepository) {
        this.contatoRepository = contatoRepository;
        this.clienteRepository = clienteRepository;
    }


    public List<ContatoDTO> findAll(){

        return contatoRepository.findAll().stream().map(ContatoMapper::toDTO).toList();
    }

    public ContatoDTO findById(Long id){
        Contato contato = contatoRepository.findById(id).
                orElseThrow(()-> new ResourceAccessException("Contato não encontrado"));
        return ContatoMapper.toDTO(contato);
    }

    public ContatoDTO insert(ContatoDTO dto){
        if (contatoRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        Contato obj = ContatoMapper.toEntity(dto);

        Contato contatoSalvo = contatoRepository.save(obj);
        return ContatoMapper.toDTO(contatoSalvo);
    }

    @Transactional
    public void delete(Long id){
        Contato contato = contatoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contato não encontrado: " + id));

        List<Cliente> clientesAssociados = clienteRepository.findByContatosContains(contato);

        for (Cliente cliente : clientesAssociados) {
            cliente.getContatos().remove(contato);
        }

        contatoRepository.delete(contato);
    }

    public ContatoDTO update(Long id, ContatoDTO dto){

        Contato entity = contatoRepository
                .findById(id).orElseThrow(() -> new EntityNotFoundException("Contato não encontrado com o: " + id));

        Contato updates = ContatoMapper.toEntity(dto);
        updateData(entity, updates);

        Contato updated = contatoRepository.save(entity);
        return ContatoMapper.toDTO(updated);

    }

    private void updateData(Contato entity, Contato obj) {
        entity.setNomeCompleto(obj.getNomeCompleto());
        entity.setEmail(obj.getEmail());
        entity.setEmailAdicional(obj.getEmailAdicional());
        entity.setTelefone(obj.getTelefone());
        entity.setTelefoneAdicional(obj.getTelefoneAdicional());


    }

    public List<ContatoDTO> findByNomeCompleto(String nomeCompleto) {
        return contatoRepository.findByNomeCompleto(nomeCompleto).stream().map(ContatoMapper::toDTO).toList();
    }

    public List<ContatoDTO> findAllByIds(List<Long> ids) {
        // Busca as entidades e converte para DTO
        return contatoRepository.findAllById(ids).stream()
                .map(ContatoMapper::toDTO)
                .toList();
    }

}
