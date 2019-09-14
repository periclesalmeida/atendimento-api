package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    Optional<Usuario> findByLogin(String login);

}
