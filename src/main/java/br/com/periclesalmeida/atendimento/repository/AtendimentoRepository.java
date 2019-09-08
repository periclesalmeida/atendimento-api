package br.com.periclesalmeida.atendimento.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.periclesalmeida.atendimento.domain.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

	@Query("SELECT at                                " +
			"FROM Atendimento           at           " +
			"JOIN at.servico            se           " +
			"WHERE at.dataHoraCadastro > :data 				  " +
			"  AND EXISTS                                     " +
			" (SELECT 1                                       " +
			"    FROM Localizacao lo                          " +
			"    JOIN lo.servicos se2                         " +
			"   WHERE se2 = se                                " +
			"     AND lo.sequencial = :sequencialLocalizacao) " +
			"ORDER BY at.dataHoraCadastro ASC                 ")
	List<Atendimento> listarPorLocalizacaoIhData(
			@Param("sequencialLocalizacao") Long sequencialLocalizacao,
			@Param("data") LocalDate data);
}
