package com.essjr.Cadastro.config;

import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.dtos.AppUserLogadoDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("appUserLogado")
    public AppUserLogadoDTO appUserLogado(@AuthenticationPrincipal AppUser user) {
        if (user == null) return null;
        return new AppUserLogadoDTO(user.getName(), user.getEmail());
    }
}
