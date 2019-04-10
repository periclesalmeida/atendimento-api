package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoLocalizacaoRepository extends JpaRepository<TipoLocalizacao, Integer> {

    Optional<TipoLocalizacao> findByDescricaoContainsAllIgnoreCase(String descricao);
}
