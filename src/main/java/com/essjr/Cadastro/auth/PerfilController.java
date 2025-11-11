package com.essjr.Cadastro.auth;

import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.AppUserRepository;
import com.essjr.Cadastro.auth.dtos.EditarUsuarioDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil") // URL base mais limpa
public class PerfilController {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder; // Injetando o Bean

    public PerfilController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder; // Usando o Bean do SecurityConfig
    }


    @GetMapping("/editar")
    public String showEditForm(Model model, @AuthenticationPrincipal AppUser appUser) {

        EditarUsuarioDTO form = new EditarUsuarioDTO();
        form.setEmail(appUser.getEmail());

        model.addAttribute("form", form);
        model.addAttribute("titulo", "Editar Senha");
        model.addAttribute("conteudo", "editarSenha"); // Nome do fragmento Thymeleaf

        return "layout";
    }


    @PostMapping("/editar")
    public String processEditForm(
            @Valid @ModelAttribute("form") EditarUsuarioDTO form,
            BindingResult bindingResult,
            @AuthenticationPrincipal AppUser appUser,
            RedirectAttributes redirectAttributes,
            Model model) {

        AppUser managedUser = appUserRepository.findById(appUser.getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


        if (!passwordEncoder.matches(form.getPassword(), managedUser.getPassword())) {

            model.addAttribute("erro", "Senha atual incorreta.");
            model.addAttribute("titulo", "Editar Senha");
            model.addAttribute("conteudo", "editarSenha");
            return "layout";
        }

        managedUser.setEmail(form.getEmail());

        if (form.getNewPassword() != null && !form.getNewPassword().isBlank()) {

            managedUser.setPasswordHash(passwordEncoder.encode(form.getNewPassword()));
        }

        appUserRepository.save(managedUser);

        redirectAttributes.addFlashAttribute("mensagem", "Senha atualizada com sucesso!");
        return "redirect:/perfil/editar";
    }
}

