package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Servico;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ServicoRepository extends MongoRepository<Servico, Long> {

    Optional<Servico> findBySiglaContainsAllIgnoreCase(String sigla);
}
