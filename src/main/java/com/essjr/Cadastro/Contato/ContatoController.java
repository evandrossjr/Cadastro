package com.essjr.Cadastro.Contato;

import com.essjr.Cadastro.Contato.dtos.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping(value = "/api/contatos")
public class ContatoController {

    @Autowired
    private ContatoService contatoService;


    @Operation(summary = "Lista todos os contatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<ContatoDTO>> findAll() {
        List<ContatoDTO> list = contatoService.findAll();
        return ResponseEntity.ok().body(list);
    }


    @Operation(summary = "Busca contato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato encontrado"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ContatoDTO> findById(@PathVariable Long id) {
        ContatoDTO obj = contatoService.findById(id);
        return ResponseEntity.ok().body(obj);

    }

    @Operation(summary = "Cadastra um novo contato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Contato criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody ContatoDTO obj) {
        try{
            obj = contatoService.insert(obj);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(obj.nomeCompleto())
                    .toUri();
            return ResponseEntity.created(uri).body(obj);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @Operation(summary = "Exclui um contato por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Contato excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contatoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza um contato existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Contato atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Contato não encontrado")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ContatoDTO> update(@PathVariable Long id, @RequestBody ContatoDTO obj) {
        obj = contatoService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }
}
