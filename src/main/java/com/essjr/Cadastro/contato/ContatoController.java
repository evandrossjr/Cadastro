package com.essjr.Cadastro.contato;

import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Controlador responsável por gerenciar os endpoints da API relacionados a contatos.
 *
 * <p>Fornece operações CRUD (criar, listar, buscar, atualizar e excluir) para a entidade {@link ContatoDTO}.
 * Essa classe integra-se com o Swagger para gerar a documentação automática da API.</p>
 */
@Tag(name = "Contatos", description = "Gerencia informações de contatos cadastrados no sistema")
@RestController
@RequestMapping(value = "/api/contatos")
public class ContatoController {


    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    /**
     * Retorna uma lista com todos os contatos cadastrados.
     *
     * @return ResponseEntity contendo a lista de {@link ContatoDTO} e o código HTTP 200 em caso de sucesso.
     */
    @Operation(summary = "Lista todos os contatos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<ContatoDTO>> findAll() {
        List<ContatoDTO> list = contatoService.findAll();
        return ResponseEntity.ok().body(list);
    }

    /**
     * Busca um contato específico com base no seu ID.
     *
     * @param id identificador único do contato.
     * @return ResponseEntity contendo o {@link ContatoDTO} correspondente e o código HTTP 200.
     * @throws org.springframework.web.server.ResponseStatusException se o contato não for encontrado (HTTP 404).
     */
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


    /**
     * Cadastra um novo contato no sistema.
     *
     * @param obj objeto {@link ContatoDTO} contendo os dados do contato a ser criado.
     * @return ResponseEntity contendo o contato criado e o código HTTP 201 em caso de sucesso;
     *         ou código HTTP 400 em caso de dados inválidos.
     */
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
                    .buildAndExpand(obj.id())
                    .toUri();
            return ResponseEntity.created(uri).body(obj);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Exclui um contato com base no seu ID.
     *
     * @param id identificador único do contato.
     * @return ResponseEntity com o código HTTP 204 em caso de exclusão bem-sucedida;
     *         ou 404 se o contato não for encontrado.
     */
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


    /**
     * Atualiza os dados de um contato existente.
     *
     * @param id  identificador único do contato a ser atualizado.
     * @param obj objeto {@link ContatoDTO} contendo os novos dados do contato.
     * @return ResponseEntity com o contato atualizado e o código HTTP 200;
     *         ou 404 se o contato não for encontrado.
     */
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
