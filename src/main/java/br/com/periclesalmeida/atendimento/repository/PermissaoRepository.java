package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PermissaoRepository extends MongoRepository<Permissao, String> {
}
