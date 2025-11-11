package com.essjr.Cadastro.appUser.dtos;

import com.essjr.Cadastro.appUser.enums.AppUserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AppUserDTO (@NotNull Long id,
                       @NotBlank String name,
                       @NotBlank @Email String email,
                       AppUserRole role){
}
