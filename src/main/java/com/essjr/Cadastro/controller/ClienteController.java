package com.essjr.Cadastro.controller;

import com.essjr.Cadastro.dtos.ClienteDTO;
import com.essjr.Cadastro.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteDTO> create(@RequestBody ClienteDTO dto){
        ClienteDTO clienteDTOSalvo = clienteService.save(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteDTOSalvo);
    }

    @GetMapping
    public List<ClienteDTO> getAll(){
        return clienteService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getById(@PathVariable Long id){
        return clienteService.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @RequestBody ClienteDTO dto){
        ClienteDTO clienteDTOAtualizado = clienteService.update(id, dto);
        return ResponseEntity.ok(clienteDTOAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
