package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"permissoes"})
    Optional<Usuario> findByLogin(String login);

    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = {"permissoes"})
    Optional<Usuario> findById(Long sequencial);
}
