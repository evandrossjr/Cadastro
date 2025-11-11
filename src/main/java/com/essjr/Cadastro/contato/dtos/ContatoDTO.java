package com.essjr.Cadastro.contato.dtos;

import jakarta.validation.constraints.*;

public record ContatoDTO (Long id,
                          @NotBlank String nomeCompleto,
                          @NotNull String telefone,
                          String telefoneAdicional,
                          @Email String email,
                          @Email String emailAdicional
                          ){}
