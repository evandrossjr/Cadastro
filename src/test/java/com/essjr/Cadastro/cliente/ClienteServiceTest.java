package com.essjr.Cadastro.cliente;

import com.essjr.Cadastro.cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.contato.Contato;
import com.essjr.Cadastro.contato.ContatoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class ClienteServiceTest {

    // Mocka as dependências (os repositórios)
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContatoRepository contatoRepository;

    // Injeta os mocks acima na classe que queremos testar
    @InjectMocks
    private ClienteService clienteService;

    // --- Dados de Teste ---
    private Cliente cliente1;
    private Contato contato1;
    private ClienteDTO clienteDTO;

    @BeforeEach
    void setUp() {
        // Configura um Contato padrão
        contato1 = new Contato();
        contato1.setId(1L);
        contato1.setNomeCompleto("Contato de Teste");
        contato1.setEmail("contato@email.com");
        contato1.setTelefone("71999999999");

        // Configura um Cliente padrão
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNomeCompleto("Cliente Teste");
        cliente1.setEmail("cliente@email.com");
        cliente1.setEmailAdicional("cliente.add@email.com");
        cliente1.setTelefone("71888888888");
        cliente1.setTelefoneAdicional("71777777777");
        cliente1.setDataRegistro(LocalDate.now());
        cliente1.setContatos(new HashSet<>(Set.of(contato1)));

        // Configura um DTO correspondente
        // (Assumindo a estrutura do record: ClienteDTO(id, nome, email, ... contatosIds))
        clienteDTO = new ClienteDTO(
                cliente1.getId(),
                cliente1.getNomeCompleto(),
                cliente1.getEmail(),
                cliente1.getEmailAdicional(),
                cliente1.getTelefone(),
                cliente1.getTelefoneAdicional(),
                cliente1.getDataRegistro(),
                List.of(contato1.getId()) // Lista de IDs
        );
    }

    // --- Testes para findAll() ---
    @Test
    @DisplayName("Deve retornar uma lista de ClienteDTOs")
    void findAll_DeveRetornarListaDeDTOs() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(List.of(cliente1));

        // Act
        List<ClienteDTO> resultado = clienteService.findAll();

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).id()).isEqualTo(cliente1.getId());
        assertThat(resultado.get(0).nomeCompleto()).isEqualTo(cliente1.getNomeCompleto());
    }

    // --- Testes para findById() ---
    @Test
    @DisplayName("Deve retornar um ClienteDTO quando o ID existe")
    void findById_DeveRetornarDTO_QuandoIdExiste() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));

        // Act
        ClienteDTO resultado = clienteService.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceAccessException quando o ID não existe")
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clienteService.findById(99L))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessageContaining("Cliente não encontrado");
    }

    // --- Testes para insert() ---
    @Test
    @DisplayName("Deve inserir um novo cliente com sucesso")
    void insert_DeveSalvarCliente_QuandoDadosValidos() {
        // Arrange
        when(clienteRepository.existsByEmail(clienteDTO.email())).thenReturn(false);
        when(contatoRepository.findAllById(clienteDTO.contatosIds())).thenReturn(List.of(contato1));

        // Quando 'save' for chamado, retorne a entidade que foi passada para ele
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ClienteDTO resultado = clienteService.insert(clienteDTO);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.nomeCompleto()).isEqualTo(clienteDTO.nomeCompleto());
        verify(clienteRepository).save(any(Cliente.class)); // Verifica se o save foi chamado
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando e-mail já existe")
    void insert_DeveLancarExcecao_QuandoEmailJaExiste() {
        // Arrange
        when(clienteRepository.existsByEmail(clienteDTO.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> clienteService.insert(clienteDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail já cadastrado.");

        // Verifica se o 'save' NUNCA foi chamado
        verify(clienteRepository, never()).save(any());
    }

    // --- Testes para delete() ---
    @Test
    @DisplayName("Deve deletar o cliente quando o ID existe")
    void delete_DeveDeletarCliente_QuandoIdExiste() {
        // Arrange
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        doNothing().when(clienteRepository).deleteById(1L); // Configura o mock para o método void

        // Act
        clienteService.delete(1L);

        // Assert
        verify(clienteRepository).findById(1L); // Verifica se a busca foi feita
        verify(clienteRepository).deleteById(1L); // Verifica se o delete foi chamado
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao deletar ID inexistente")
    void delete_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> clienteService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(clienteRepository, never()).deleteById(anyLong()); // Garante que o delete não foi chamado
    }

    // --- Testes para update() ---
    @Test
    @DisplayName("Deve atualizar o cliente com sucesso")
    void update_DeveAtualizarCliente_QuandoDadosValidos() {
        // Arrange
        ClienteDTO dtoAtualizado = new ClienteDTO(
                1L, "Nome Atualizado", "novo@email.com", null, null, null, null, List.of() // Lista de contatos vazia
        );

        when(clienteRepository.findByEmailAndIdNot(dtoAtualizado.email(), 1L)).thenReturn(Optional.empty());
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente1); // Retorna a entidade atualizada

        // Act
        ClienteDTO resultado = clienteService.update(1L, dtoAtualizado);

        // Assert
        assertThat(resultado.nomeCompleto()).isEqualTo("Nome Atualizado");
        assertThat(resultado.email()).isEqualTo("novo@email.com");
        assertThat(resultado.contatosIds()).isEmpty(); // Verifica se os contatos foram desvinculados
        verify(clienteRepository).save(cliente1); // Verifica se a entidade foi salva
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao atualizar com e-mail duplicado")
    void update_DeveLancarExcecao_QuandoEmailDuplicado() {
        // Arrange
        ClienteDTO dtoComEmailDuplicado = new ClienteDTO(
                1L, "Nome", "email.duplicado@email.com", null, null, null, null, List.of()
        );
        Cliente clienteExistente = new Cliente(); // O outro cliente que já tem o e-mail
        clienteExistente.setId(2L);

        when(clienteRepository.findByEmailAndIdNot("email.duplicado@email.com", 1L))
                .thenReturn(Optional.of(clienteExistente));

        // Act & Assert
        assertThatThrownBy(() -> clienteService.update(1L, dtoComEmailDuplicado))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail já cadastrado por outro usuário.");

        verify(clienteRepository, never()).save(any()); // 'save' não deve ser chamado
    }
}