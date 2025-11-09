package com.essjr.Cadastro.model.Contato.dtos;

import com.essjr.Cadastro.model.Contato.Contato;
import com.essjr.Cadastro.model.Contato.EmailContato;
import com.essjr.Cadastro.model.Contato.TelefoneContato;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

public record ContatoDTO (Long id,
                          @NotBlank String nomeCompleto,
                          @Email String email,
                          @Email String emailAdicional,
                          @NotNull String telefone,
                          String telefoneAdicional){}
