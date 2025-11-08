package com.essjr.Cadastro.repositories;

import com.essjr.Cadastro.model.Cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
