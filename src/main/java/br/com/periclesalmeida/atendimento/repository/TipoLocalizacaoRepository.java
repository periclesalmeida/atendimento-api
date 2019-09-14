package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TipoLocalizacaoRepository extends MongoRepository<TipoLocalizacao, String> {

    Optional<TipoLocalizacao> findByDescricaoContainsAllIgnoreCase(String descricao);
}
