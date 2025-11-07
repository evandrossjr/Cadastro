package com.essjr.Cadastro.repositories;

import com.essjr.Cadastro.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContatoRepository extends JpaRepository<Contato, Long> {
}
