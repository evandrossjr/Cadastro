package com.essjr.Cadastro.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.contatos")
    List<Cliente> findAllWithContatos();
}
