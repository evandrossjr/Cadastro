package com.essjr.Cadastro.appUser.dtos;

public class AppUserLogadoDTO {
    private String nome;
    private String email;

    public AppUserLogadoDTO(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

    public String getNome() { return nome; }
    public String getEmail() { return email; }
}
