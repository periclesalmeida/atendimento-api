package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocalizacaoRepository extends JpaRepository<Localizacao, Long> {


    Optional<Localizacao> findByDescricaoContainsAndTipoAllIgnoreCase(String descricao, TipoLocalizacao tipo);


}
