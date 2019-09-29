package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface LocalizacaoRepository extends MongoRepository<Localizacao, String> {

    Optional<Localizacao> findByDescricaoContainsAndTipoAllIgnoreCase(String descricao, TipoLocalizacao tipo);

}
