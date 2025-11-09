package com.essjr.Cadastro.Contato.dtos;

import jakarta.validation.constraints.*;

public record ContatoDTO (Long id,
                          @NotBlank String nomeCompleto,
                          @Email String email,
                          @Email String emailAdicional,
                          @NotNull String telefone,
                          String telefoneAdicional){}
