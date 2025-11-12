package com.essjr.Cadastro.cliente;

import com.essjr.Cadastro.contato.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);

    /**
     * Query para trazer os clientes que possuem contatos cadastrados.
     */
    @Query("SELECT DISTINCT c FROM Cliente c LEFT JOIN FETCH c.contatos")
    List<Cliente> findAllWithContatos();

    /**
     * Procura por um cliente com um dado e-mail, mas que NÃO TENHA o ID especificado.
     * Isso é usado para checar duplicidade ao atualizar um usuário.
     */
    Optional<Cliente> findByEmailAndIdNot(String email, Long id);

    /**
     * Encontra todos os Clientes que contêm o Contato especificado
     * em sua lista de contatos.
     */
    List<Cliente> findByContatosContains(Contato contato);
}
