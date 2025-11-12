package com.essjr.Cadastro.contato;

import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContatoRepository extends JpaRepository<Contato, Long> {

    boolean existsByEmail(String email);

    List<Contato> findByNomeCompleto(String NomeCompleto);

}
