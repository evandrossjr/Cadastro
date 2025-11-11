package com.essjr.Cadastro.cliente;

import com.essjr.Cadastro.cliente.dtos.ClienteDTO;
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
 * Controlador responsável por gerenciar os endpoints relacionados aos clientes.
 *
 * <p>Esta classe expõe operações para listar, buscar, cadastrar, atualizar
 * e excluir clientes. Integra-se ao Swagger para documentação automática e
 * segue as boas práticas REST.</p>
 */
@Tag(name = "Clientes", description = "Endpoints de gestão de clientes")
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {


    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }


    /**
     * Retorna uma lista com todos os clientes cadastrados.
     *
     * @return ResponseEntity contendo a lista de {@link ClienteDTO} e o código HTTP 200 em caso de sucesso.
     */
    @Operation(summary = "Lista todos os clientes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    })
    @GetMapping()
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<ClienteDTO> list = clienteService.findAll();
        return ResponseEntity.ok().body(list);
    }


    /**
     * Busca um cliente específico com base no seu ID.
     *
     * @param id identificador único do cliente.
     * @return ResponseEntity contendo o {@link ClienteDTO} correspondente e o código HTTP 200.
     * @throws org.springframework.web.server.ResponseStatusException se o cliente não for encontrado (HTTP 404).
     */
    @Operation(summary = "Busca cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> findById(@PathVariable Long id) {
        ClienteDTO obj = clienteService.findById(id);
        return ResponseEntity.ok().body(obj);

    }


    /**
     * Cadastra um novo cliente no sistema.
     *
     * @param obj objeto {@link ClienteDTO} com os dados do cliente a ser criado.
     * @return ResponseEntity contendo o cliente criado e o código HTTP 201 em caso de sucesso;
     *         ou código HTTP 400 em caso de dados inválidos.
     */
    @Operation(summary = "Cadastra um novo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<?> insert(@RequestBody ClienteDTO obj) {
        try{
            obj = clienteService.insert(obj);
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


    /**
     * Exclui um cliente com base no seu ID.
     *
     * @param id identificador único do cliente.
     * @return ResponseEntity com o código HTTP 204 em caso de exclusão bem-sucedida;
     *         ou 404 se o cliente não for encontrado.
     */
    @Operation(summary = "Exclui um cliente por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Atualiza os dados de um cliente existente.
     *
     * @param id  identificador único do cliente a ser atualizado.
     * @param obj objeto {@link ClienteDTO} contendo os novos dados do cliente.
     * @return ResponseEntity com o cliente atualizado e o código HTTP 200;
     *         ou 404 se o cliente não for encontrado.
     */
    @Operation(summary = "Atualiza um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteDTO> update(@PathVariable Long id, @RequestBody ClienteDTO obj) {
        obj = clienteService.update(id, obj);
        return ResponseEntity.ok().body(obj);
    }

}
