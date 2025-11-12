package com.essjr.Cadastro.auth;

import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.AppUserRepository;
import com.essjr.Cadastro.appUser.dtos.AppUserRegistrationDTO;
import com.essjr.Cadastro.appUser.enums.AppUserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito
class AuthServiceTest {

    // Cria um "falso" repositório
    @Mock
    private AppUserRepository appUserRepository;

    // Cria um "falso" codificador de senha
    @Mock
    private PasswordEncoder passwordEncoder;

    // Injeta os mocks acima no service que queremos testar
    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve registrar um novo usuário com sucesso")
    void registerUser_DeveSalvarNovoUsuario_QuandoEmailNaoExiste() {
        // Arrange (Configuração)

        // 1. Define o DTO de entrada
        AppUserRegistrationDTO dto = new AppUserRegistrationDTO(
                "Evandro Teste",
                "evandro@email.com",
                "senha123"
        );

        // 2. Define a senha que esperamos que o encoder retorne
        String senhaHasheada = "$2a$10$abcdefghijklmnopqrstuvwx";

        // 3. Configura os Mocks:
        //    - Quando findByEmail for chamado, retorne "vazio" (e-mail não existe)
        when(appUserRepository.findByEmail("evandro@email.com")).thenReturn(Optional.empty());
        //    - Quando o encoder for chamado com "senha123", retorne o hash
        when(passwordEncoder.encode("senha123")).thenReturn(senhaHasheada);
        //    - (O 'save' não precisa retornar nada, nós vamos capturá-lo)

        // Act (Ação)
        authService.registerUser(dto, AppUserRole.REGULAR);

        // Assert (Verificação)

        // 1. Prepara um "captor" para pegar o objeto AppUser que foi enviado para o 'save'
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);

        // 2. Verifica se o 'save' foi chamado 1 vez e captura o usuário
        verify(appUserRepository, times(1)).save(userCaptor.capture());

        // 3. Pega o usuário que foi salvo
        AppUser usuarioSalvo = userCaptor.getValue();

        // 4. Verifica se os dados do usuário salvo estão corretos
        assertThat(usuarioSalvo.getName()).isEqualTo("Evandro Teste");
        assertThat(usuarioSalvo.getEmail()).isEqualTo("evandro@email.com");
        assertThat(usuarioSalvo.getRole()).isEqualTo(AppUserRole.REGULAR);
        assertThat(usuarioSalvo.getPasswordHash()).isEqualTo(senhaHasheada);

        // 5. Verifica se o 'findByEmail' e 'encode' também foram chamados
        verify(appUserRepository).findByEmail("evandro@email.com");
        verify(passwordEncoder).encode("senha123");
    }

    @Test
    @DisplayName("Deve lançar IllegalStateException quando o e-mail já existe")
    void registerUser_DeveLancarExcecao_QuandoEmailJaExiste() {
        // Arrange (Configuração)

        // 1. Define o DTO de entrada
        AppUserRegistrationDTO dto = new AppUserRegistrationDTO(
                "Outro Usuario",
                "email.existente@email.com",
                "outraSenha"
        );

        // 2. Cria um usuário "falso" que já existe
        AppUser usuarioExistente = new AppUser();

        // 3. Configura o Mock:
        //    - Quando findByEmail for chamado, retorne o usuário existente
        when(appUserRepository.findByEmail("email.existente@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        // Act & Assert (Ação e Verificação)

        // Verifica se a exceção correta é lançada
        assertThatThrownBy(() -> authService.registerUser(dto, AppUserRole.ADMIN))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Email já cadastrado.");

        // Verifica se o 'save' NUNCA foi chamado
        verify(appUserRepository, never()).save(any(AppUser.class));
        // Verifica se o 'encode' NUNCA foi chamado
        verify(passwordEncoder, never()).encode(anyString());
    }
}