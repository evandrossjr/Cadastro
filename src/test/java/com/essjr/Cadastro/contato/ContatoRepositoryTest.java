package com.essjr.Cadastro.contato;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ContatoRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ContatoRepository contatoRepository;

    /**
     * Helper para criar um Contato válido (passando na validação)
     */
    private Contato criarContatoValido(String nomeCompleto, String email) {
        Contato contato = new Contato();
        // Assumindo que a validação exige nome completo
        contato.setNomeCompleto(nomeCompleto);
        contato.setEmail(email);
        contato.setTelefone("71999999999"); // Assumindo validação de telefone
        return contato;
    }

    // --- Testes para existsByEmail ---

    @Test
    @DisplayName("Deve retornar true se o email já existir")
    void existsByEmail_DeveRetornarTrue_QuandoEmailExiste() {
        // Arrange
        Contato contato = criarContatoValido("Maria Silva", "maria@email.com");
        testEntityManager.persist(contato);

        // Act
        boolean resultado = contatoRepository.existsByEmail("maria@email.com");

        // Assert
        assertThat(resultado).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false se o email não existir")
    void existsByEmail_DeveRetornarFalse_QuandoEmailNaoExiste() {
        // Arrange
        // (Nenhum contato salvo)

        // Act
        boolean resultado = contatoRepository.existsByEmail("fantasma@email.com");

        // Assert
        assertThat(resultado).isFalse();
    }

    // --- Testes para findByNomeCompleto ---

    @Test
    @DisplayName("Deve retornar uma lista de Contatos com o nome completo correspondente")
    void findByNomeCompleto_DeveRetornarListaDeContatos() {
        // Arrange
        Contato contato1 = criarContatoValido("Joao Pedro", "joao@email.com");
        Contato contato2 = criarContatoValido("Joao Pedro", "joao.pedro@email.com");
        Contato contato3 = criarContatoValido("Ana Julia", "ana@email.com");

        testEntityManager.persist(contato1);
        testEntityManager.persist(contato2);
        testEntityManager.persist(contato3);

        // Act
        List<Contato> resultado = contatoRepository.findByNomeCompleto("Joao Pedro");

        // Assert
        assertThat(resultado).hasSize(2);
        assertThat(resultado).containsExactlyInAnyOrder(contato1, contato2);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se o nome completo não for encontrado")
    void findByNomeCompleto_DeveRetornarListaVazia_QuandoNomeNaoExiste() {
        // Arrange
        Contato contato1 = criarContatoValido("Joao Pedro", "joao@email.com");
        testEntityManager.persist(contato1);

        // Act
        List<Contato> resultado = contatoRepository.findByNomeCompleto("Nome Inexistente");

        // Assert
        assertThat(resultado).isEmpty();
    }
}