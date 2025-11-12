package com.essjr.Cadastro.cliente;

import com.essjr.Cadastro.contato.Contato;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ClienteRepository clienteRepository;


    private Cliente criarCliente(String nome, String email) {
        Cliente cliente = new Cliente();
        cliente.setNomeCompleto(nome);
        cliente.setEmail(email);
        cliente.setTelefone("71999999999");
        cliente.setDataRegistro(LocalDate.now());
        return cliente;
    }

    private Contato criarContato(String nome, String email) {
        Contato contato = new Contato();
        contato.setNomeCompleto(nome);
        contato.setEmail(email);
        contato.setTelefone("71888888888");
        return contato;
    }

    // --- Testes para existsByEmail ---

    @Test
    @DisplayName("Deve retornar true quando o email já existe")
    void existsByEmail_DeveRetornarTrue_QuandoEmailExiste() {
        // Arrange (Configuração)
        Cliente cliente = criarCliente("Cliente Teste", "cliente@email.com");
        testEntityManager.persist(cliente);

        // Act (Ação)
        boolean resultado = clienteRepository.existsByEmail("cliente@email.com");

        // Assert (Verificação)
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando o email não existe")
    void existsByEmail_DeveRetornarFalse_QuandoEmailNaoExiste() {
        // Arrange
        // (Nenhum cliente salvo)

        // Act
        boolean resultado = clienteRepository.existsByEmail("naoexiste@email.com");

        // Assert
        assertThat(resultado).isFalse();
    }

    // --- Testes para findAllWithContatos ---

    @Test
    @DisplayName("Deve retornar clientes e seus contatos associados")
    void findAllWithContatos_DeveRetornarClientesComContatos() {
        // Arrange
        Contato contato1 = criarContato("Contato Teste", "contat1o@email.com");
        Cliente cliente1 = criarCliente("Cliente Com Contato", "cliente1@email.com");

        // Associa o contato ao cliente (simulando o @ManyToMany)
        cliente1.getContatos().add(contato1);

        testEntityManager.persist(contato1);
        testEntityManager.persist(cliente1);

        // Act
        List<Cliente> clientes = clienteRepository.findAllWithContatos();

        // Assert
        assertThat(clientes).isNotEmpty();
        assertThat(clientes.get(0).getNomeCompleto()).isEqualTo("Cliente Com Contato");
        // Verifica se a lista de contatos (lazy) foi carregada (FETCH)
        assertThat(clientes.get(0).getContatos()).isNotEmpty();
        assertThat(clientes.get(0).getContatos().iterator().next().getNomeCompleto()).isEqualTo("Contato Teste");
    }

    @Test
    @DisplayName("Deve retornar cliente mesmo sem contatos (LEFT JOIN)")
    void findAllWithContatos_DeveRetornarClienteSemContatos() {
        // Arrange
        Cliente clienteSemContato = criarCliente("Cliente Sozinho", "sozinho@email.com");
        testEntityManager.persist(clienteSemContato);

        // Act
        List<Cliente> clientes = clienteRepository.findAllWithContatos();

        // Assert
        assertThat(clientes).isNotEmpty();
        assertThat(clientes.get(0).getNomeCompleto()).isEqualTo("Cliente Sozinho");
        assertThat(clientes.get(0).getContatos()).isEmpty(); // O LEFT JOIN deve funcionar
    }

    @Test
    @DisplayName("Não deve retornar clientes duplicados (DISTINCT)")
    void findAllWithContatos_NaoDeveRetornarDuplicatas() {
        // Arrange
        Contato contato1 = criarContato("Contato A", "ca@email.com");
        Contato contato2 = criarContato("Contato B", "cb@email.com");
        Cliente cliente1 = criarCliente("Cliente Unico", "unico@email.com");

        // Cliente com 2 contatos (um JOIN normal retornaria 2 linhas)
        cliente1.getContatos().addAll(List.of(contato1, contato2));

        testEntityManager.persist(contato1);
        testEntityManager.persist(contato2);
        testEntityManager.persist(cliente1);

        // Act
        List<Cliente> clientes = clienteRepository.findAllWithContatos();

        // Assert
        // O DISTINCT deve garantir que o cliente só apareça uma vez
        assertThat(clientes.size()).isEqualTo(1);
    }

    // --- Testes para findByEmailAndIdNot ---

    @Test
    @DisplayName("Deve encontrar email se pertencer a um ID diferente")
    void findByEmailAndIdNot_DeveEncontrarEmailDeOutroId() {
        // Arrange
        Cliente cliente1 = criarCliente("Cliente A", "email.a@email.com");
        Cliente cliente2 = criarCliente("Cliente B", "email.b@email.com");

        // Persiste e captura os IDs gerados
        Long idCliente1 = testEntityManager.persistAndGetId(cliente1, Long.class);
        testEntityManager.persist(cliente2);

        // Act
        // Procura por "email.b@email.com" ignorando o ID do cliente1
        Optional<Cliente> resultado = clienteRepository.findByEmailAndIdNot("email.b@email.com", idCliente1);

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNomeCompleto()).isEqualTo("Cliente B");
    }

    @Test
    @DisplayName("Não deve encontrar email se pertencer ao ID excluído")
    void findByEmailAndIdNot_NaoDeveEncontrarEmailDoIdExcluido() {
        // Arrange
        Cliente cliente1 = criarCliente("Cliente A", "email.a@email.com");
        Long idCliente1 = testEntityManager.persistAndGetId(cliente1, Long.class);

        // Act
        // Procura por "email.a@email.com" MAS ignora o ID do cliente1
        Optional<Cliente> resultado = clienteRepository.findByEmailAndIdNot("email.a@email.com", idCliente1);

        // Assert
        assertThat(resultado).isNotPresent(); // Não deve encontrar
    }

    // --- Testes para findByContatosContains ---

    @Test
    @DisplayName("Deve retornar a lista de clientes que contém o contato")
    void findByContatosContains_DeveRetornarClientesCorretos() {
        // Arrange
        Contato contatoAlvo = criarContato("Contato Alvo", "alvo@email.com");
        Contato contatoOutro = criarContato("Outro Contato", "outro@email.com");
        testEntityManager.persist(contatoAlvo);
        testEntityManager.persist(contatoOutro);

        Cliente clienteA = criarCliente("Cliente A", "a@email.com");
        clienteA.getContatos().add(contatoAlvo);
        testEntityManager.persist(clienteA);

        Cliente clienteB = criarCliente("Cliente B", "b@email.com");
        clienteB.getContatos().add(contatoOutro); // Associado a outro contato
        testEntityManager.persist(clienteB);

        Cliente clienteC = criarCliente("Cliente C", "c@email.com");
        clienteC.getContatos().add(contatoAlvo);
        testEntityManager.persist(clienteC);

        // Act
        // Busca todos os clientes que possuem o "contatoAlvo"
        List<Cliente> clientes = clienteRepository.findByContatosContains(contatoAlvo);

        // Assert
        assertThat(clientes).hasSize(2);
        assertThat(clientes).containsExactlyInAnyOrder(clienteA, clienteC);
        assertThat(clientes).doesNotContain(clienteB);
    }
}