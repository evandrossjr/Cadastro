package com.essjr.Cadastro.contato;

import com.essjr.Cadastro.cliente.Cliente;
import com.essjr.Cadastro.cliente.ClienteRepository;
import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContatoServiceTest {

    // Cria "falsos" repositórios
    @Mock
    private ContatoRepository contatoRepository;
    @Mock
    private ClienteRepository clienteRepository;

    // Injeta os mocks acima no service que queremos testar
    @InjectMocks
    private ContatoService contatoService;

    // --- Objetos de Teste Padrão ---
    private Contato contato1;
    private ContatoDTO contatoDTO;
    private Cliente cliente1;

    // Este método roda antes de CADA teste
    @BeforeEach
    void setUp() {
        // 1. Configura um Contato (Entidade)
        contato1 = new Contato();
        contato1.setId(1L);
        contato1.setNomeCompleto("Contato Teste");
        contato1.setEmail("contato@email.com");
        contato1.setTelefone("71999999999");
        contato1.setEmailAdicional("contato.add@email.com");

        // 2. Configura um Cliente (Entidade)
        cliente1 = new Cliente();
        cliente1.setId(1L);
        cliente1.setNomeCompleto("Cliente Teste");
        // Importante: Inicializa a lista de contatos para o teste de 'delete'
        cliente1.setContatos(new HashSet<>(Set.of(contato1)));

        // 3. Configura um ContatoDTO (Record)
        // (Assumindo a estrutura do seu DTO e Mapper)
        contatoDTO = new ContatoDTO(
                contato1.getId(),
                contato1.getNomeCompleto(),
                contato1.getEmail(),
                contato1.getEmailAdicional(),
                contato1.getTelefone(),
                contato1.getTelefoneAdicional()
        );
    }

    // --- Testes para findAll() ---
    @Test
    @DisplayName("Deve retornar todos os contatos como DTOs")
    void findAll_DeveRetornarListaDeDTOs() {
        // Arrange (Configuração)
        when(contatoRepository.findAll()).thenReturn(List.of(contato1));

        // Act (Ação)
        List<ContatoDTO> resultado = contatoService.findAll();

        // Assert (Verificação)
        assertThat(resultado).hasSize(1);
        // Assumindo que seu DTO é um record (contatoDTO.nomeCompleto())
        assertThat(resultado.get(0).nomeCompleto()).isEqualTo("Contato Teste");
    }

    // --- Testes para findById() ---
    @Test
    @DisplayName("Deve retornar um DTO quando o ID for encontrado")
    void findById_DeveRetornarDTO_QuandoIdExiste() {
        // Arrange
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato1));

        // Act
        ContatoDTO resultado = contatoService.findById(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceAccessException quando o ID não for encontrado")
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(contatoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> contatoService.findById(99L))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessage("Contato não encontrado");
    }

    // --- Testes para insert() ---
    @Test
    @DisplayName("Deve salvar um novo contato com sucesso")
    void insert_DeveSalvarContato() {
        // Arrange
        when(contatoRepository.existsByEmail(contatoDTO.email())).thenReturn(false);
        // Retorna o objeto que foi passado para o 'save'
        when(contatoRepository.save(any(Contato.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ContatoDTO resultado = contatoService.insert(contatoDTO);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.nomeCompleto()).isEqualTo("Contato Teste");
        // Verifica se o método 'save' foi chamado exatamente 1 vez
        verify(contatoRepository, times(1)).save(any(Contato.class));
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException se o e-mail já existir")
    void insert_DeveLancarExcecao_QuandoEmailJaExiste() {
        // Arrange
        when(contatoRepository.existsByEmail(contatoDTO.email())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> contatoService.insert(contatoDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("E-mail já cadastrado.");

        // Verifica se o 'save' NUNCA foi chamado
        verify(contatoRepository, never()).save(any());
    }

    // --- Testes para delete() ---
    @Test
    @DisplayName("Deve excluir o contato e desvinculá-lo dos clientes")
    void delete_DeveExcluirContatoEDesvincular() {
        // Arrange
        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato1));
        when(clienteRepository.findByContatosContains(contato1)).thenReturn(List.of(cliente1));
        doNothing().when(contatoRepository).delete(contato1);

        // Act
        contatoService.delete(1L);

        // Assert
        // Verifica se os métodos corretos foram chamados
        verify(contatoRepository).findById(1L);
        verify(clienteRepository).findByContatosContains(contato1);
        verify(contatoRepository).delete(contato1);

        // Verifica se a lógica de remoção (a mais importante) funcionou
        assertThat(cliente1.getContatos()).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar excluir ID inexistente")
    void delete_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(contatoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> contatoService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);

        // Garante que a lógica de desvinculação e exclusão nunca foi chamada
        verify(clienteRepository, never()).findByContatosContains(any());
        verify(contatoRepository, never()).delete(any());
    }

    // --- Testes para update() ---
    @Test
    @DisplayName("Deve atualizar os dados do contato")
    void update_DeveAtualizarContato() {
        // Arrange
        ContatoDTO dtoAtualizado = new ContatoDTO(
                1L,
                "Nome Atualizado",
                "novo@email.com",
                "novo.add@email.com",
                "11111111111",
                "22222222222"
        );

        when(contatoRepository.findById(1L)).thenReturn(Optional.of(contato1));
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato1); // Retorna a entidade atualizada

        // Act
        ContatoDTO resultado = contatoService.update(1L, dtoAtualizado);

        // Assert
        verify(contatoRepository).save(contato1);
        assertThat(resultado.nomeCompleto()).isEqualTo("Nome Atualizado");
        assertThat(resultado.email()).isEqualTo("novo@email.com");
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException ao tentar atualizar ID inexistente")
    void update_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(contatoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> contatoService.update(99L, contatoDTO))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // --- Testes para findByNomeCompleto() ---
    @Test
    @DisplayName("Deve retornar DTOs pelo nome completo")
    void findByNomeCompleto_DeveRetornarListaDeDTOs() {
        // Arrange
        when(contatoRepository.findByNomeCompleto("Contato Teste")).thenReturn(List.of(contato1));

        // Act
        List<ContatoDTO> resultado = contatoService.findByNomeCompleto("Contato Teste");

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
    }

    // --- Testes para findAllByIds() ---
    @Test
    @DisplayName("Deve retornar DTOs pela lista de IDs")
    void findAllByIds_DeveRetornarListaDeDTOs() {
        // Arrange
        when(contatoRepository.findAllById(List.of(1L))).thenReturn(List.of(contato1));

        // Act
        List<ContatoDTO> resultado = contatoService.findAllByIds(List.of(1L));

        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
    }
}