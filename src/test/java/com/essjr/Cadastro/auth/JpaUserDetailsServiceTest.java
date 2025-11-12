package com.essjr.Cadastro.auth;

import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.AppUserRepository;
import com.essjr.Cadastro.appUser.enums.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class JpaUserDetailsServiceTest {

    // Cria um "falso" repositório
    @Mock
    private AppUserRepository appUserRepository;

    // Injeta o mock acima no service que queremos testar
    @InjectMocks
    private JpaUserDetailsService jpaUserDetailsService;

    // Objeto de teste padrão
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        // Configura um usuário padrão que o repositório "falso" irá retornar
        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setName("Usuário Teste");
        testUser.setEmail("teste@email.com");
        testUser.setPasswordHash("hash_da_senha"); // O hash real
        testUser.setRole(AppUserRole.REGULAR);
    }

    @Test
    @DisplayName("Deve carregar o usuário pelo email (username) com sucesso")
    void loadUserByUsername_DeveRetornarUserDetails_QuandoUsuarioEncontrado() {
        // Arrange (Configuração)
        String email = "teste@email.com";

        // Configura o mock: quando o repositório procurar por "teste@email.com",
        // ele deve retornar o 'testUser'
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

        // Act (Ação)
        UserDetails userDetails = jpaUserDetailsService.loadUserByUsername(email);

        // Assert (Verificação)
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo("hash_da_senha");
        // Verifica se a Role foi carregada corretamente
        assertThat(userDetails.getAuthorities())
                .hasSize(1)
                .extracting(Object::toString)
                .contains("ROLE_REGULAR");
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando o email não for encontrado")
    void loadUserByUsername_DeveLancarExcecao_QuandoUsuarioNaoEncontrado() {
        // Arrange (Configuração)
        String email = "naoexiste@email.com";

        // Configura o mock: quando o repositório procurar, ele deve retornar vazio
        when(appUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert (Ação e Verificação)

        // Verifica se a exceção correta é lançada
        assertThatThrownBy(() -> jpaUserDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found with email: " + email);
    }
}