package com.essjr.Cadastro.auth;

import com.essjr.Cadastro.appUser.dtos.AppUserRegistrationDTO;
import com.essjr.Cadastro.appUser.enums.AppUserRole;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthWebController {

    private final AuthService authService;
    private final JpaUserDetailsService jpaUserDetailsService;

    public AuthWebController(AuthService authService, JpaUserDetailsService jpaUserDetailsService) {
        this.authService = authService;
        this.jpaUserDetailsService = jpaUserDetailsService;
    }

    /**
     * Apenas exibe a página de login.
     * O Spring Security cuidará do processamento do POST /login.
     */
    @GetMapping("/login")
    public String login() {
        return "login"; // Retorna 'templates/login.html'
    }

    /**
     * Exibe o formulário de registro.
     * Adiciona um DTO vazio ao 'model' para o th:object do formulário.
     */
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Usa o DTO de Registro que definimos (com validação)
        model.addAttribute("conteudo","register");
        model.addAttribute("user", new AppUserRegistrationDTO("", "", ""));
        return "register"; // Retorna 'templates/register.html'
    }

    /**
     * Processa o envio do formulário de registro.
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") AppUserRegistrationDTO userDTO,
            BindingResult bindingResult,
            Model model) { // Adicionamos 'Model' para o caso de erro

        // 1. Verifica os erros de validação (ex: @NotBlank, @Email)
        if (bindingResult.hasErrors()) {
            return "register"; // Volta ao formulário, os erros aparecerão
        }

        // 2. Tenta registrar o usuário
        try {
            // Chama o 'AuthService' que corrigimos
            authService.registerUser(userDTO, AppUserRole.REGULAR);

        } catch (IllegalStateException e) {
            // 3. Captura o erro "Email já cadastrado" do service

            // Adiciona a mensagem de erro ao model para o Thymeleaf
            model.addAttribute("erro", e.getMessage());

            // Retorna ao formulário (sem redirect) para manter os dados
            return "register";
        }

        // 4. Sucesso! Redireciona para o login com uma flag de sucesso
        return "redirect:/login?success";
    }


}