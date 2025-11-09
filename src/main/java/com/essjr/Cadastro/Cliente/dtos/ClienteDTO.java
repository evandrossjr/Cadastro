package com.essjr.Cadastro.Cliente.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record ClienteDTO (Long id,
                          @NotBlank String nomeCompleto,
                          @Email String email,
                          @Email String emailAdicional,
                          @NotNull String telefone,
                          String telefoneAdicional,
                          LocalDate dataRegistro,
                          List<Long> contatosIds){}


