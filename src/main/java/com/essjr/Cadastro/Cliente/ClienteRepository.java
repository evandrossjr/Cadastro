package com.essjr.Cadastro.Cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.contatos")
    List<Cliente> findAllWithContatos();

    /**
     * Procura por um cliente com um dado e-mail, mas que NÃO TENHA o ID especificado.
     * Isso é usado para checar duplicatas ao ATUALIZAR um usuário.
     * @param email O e-mail para checar
     * @param id O ID do usuário atual (que queremos ignorar na busca)
     * @return Um Optional contendo o cliente duplicado, se houver
     */
    Optional<Cliente> findByEmailAndIdNot(String email, Long id);
}
