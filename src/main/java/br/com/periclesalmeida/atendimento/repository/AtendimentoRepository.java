package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AtendimentoRepository extends MongoRepository<Atendimento, String> {

	/*
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
	 */

	@Query("{ 'dataHoraCadastro' : ?0 }")
	List<Atendimento> listarPorLocalizacaoIhData(
			@Param("sequencialLocalizacao") String sequencialLocalizacao,
			@Param("data") LocalDate data);
}
