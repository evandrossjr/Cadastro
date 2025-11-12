package com.essjr.Cadastro.appUser;

import com.essjr.Cadastro.appUser.enums.AppUserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * Helper para criar uma entidade AppUser válida para os testes.
     * (Presumindo que a entidade AppUser tem validações @NotBlank, etc.)
     */
    private AppUser createAppUser(String name, String email) {
        AppUser user = new AppUser();
        user.setName(name);
        user.setEmail(email);
        user.setPasswordHash("$2a$10$abcdefghijklmnopqrstuvwx"); // Hash de senha fictício
        user.setRole(AppUserRole.REGULAR);
        return user;
    }

    // --- Testes para findByEmail (Case-Sensitive) ---

    @Test
    @DisplayName("findByEmail: Deve encontrar usuário quando o email bate exatamente")
    void findByEmail_DeveEncontrarUsuario_QuandoEmailExato() {
        // Arrange
        AppUser user = createAppUser("Usuario Teste", "teste@email.com");
        testEntityManager.persist(user);

        // Act
        Optional<AppUser> resultado = appUserRepository.findByEmail("teste@email.com");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getName()).isEqualTo("Usuario Teste");
    }

    @Test
    @DisplayName("findByEmail: Não deve encontrar usuário quando o case é diferente")
    void findByEmail_NaoDeveEncontrar_QuandoCaseDiferente() {
        // Arrange
        AppUser user = createAppUser("Usuario Teste", "teste@email.com");
        testEntityManager.persist(user);

        // Act
        Optional<AppUser> resultado = appUserRepository.findByEmail("TESTE@EMAIL.COM");

        // Assert
        assertThat(resultado).isNotPresent();
    }

    @Test
    @DisplayName("findByEmail: Deve retornar vazio se o email não existir")
    void findByEmail_DeveRetornarVazio_QuandoEmailNaoExiste() {
        // Arrange (Ninguém salvo)

        // Act
        Optional<AppUser> resultado = appUserRepository.findByEmail("naoexiste@email.com");

        // Assert
        assertThat(resultado).isNotPresent();
    }

    // --- Testes para findByEmailIgnoreCase (Case-Insensitive) ---

    @Test
    @DisplayName("findByEmailIgnoreCase: Deve encontrar usuário quando o case é diferente")
    void findByEmailIgnoreCase_DeveEncontrarUsuario_QuandoCaseDiferente() {
        // Arrange
        AppUser user = createAppUser("Usuario Teste", "teste@email.com");
        testEntityManager.persist(user);

        // Act
        // Busca com case diferente
        Optional<AppUser> resultado = appUserRepository.findByEmailIgnoreCase("TESTE@EMAIL.COM");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    @DisplayName("findByEmailIgnoreCase: Deve retornar vazio se o email não existir")
    void findByEmailIgnoreCase_DeveRetornarVazio_QuandoEmailNaoExiste() {
        // Arrange (Ninguém salvo)

        // Act
        Optional<AppUser> resultado = appUserRepository.findByEmailIgnoreCase("naoexiste@email.com");

        // Assert
        assertThat(resultado).isNotPresent();
    }

    // --- Testes para findByNameIgnoreCase (Case-Insensitive) ---

    @Test
    @DisplayName("findByNameIgnoreCase: Deve encontrar usuário quando o case é diferente")
    void findByNameIgnoreCase_DeveEncontrarUsuario_QuandoCaseDiferente() {
        // Arrange
        AppUser user = createAppUser("Evandro Sacramento", "evandro@email.com");
        testEntityManager.persist(user);

        // Act
        // Busca com case diferente
        Optional<AppUser> resultado = appUserRepository.findByNameIgnoreCase("evandro sacramento");

        // Assert
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getName()).isEqualTo("Evandro Sacramento");
    }

    @Test
    @DisplayName("findByNameIgnoreCase: Deve retornar vazio se o nome não existir")
    void findByNameIgnoreCase_DeveRetornarVazio_QuandoNomeNaoExiste() {
        // Arrange (Ninguém salvo)

        // Act
        Optional<AppUser> resultado = appUserRepository.findByNameIgnoreCase("Nome Falso");

        // Assert
        assertThat(resultado).isNotPresent();
    }
}