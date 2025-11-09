package com.essjr.Cadastro.Contato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT c.nomeCompleto FROM Contato c")
    List<String> findEspecialidades();

    List<Contato> findByNomeCompleto(String NomeCompleto);

}
