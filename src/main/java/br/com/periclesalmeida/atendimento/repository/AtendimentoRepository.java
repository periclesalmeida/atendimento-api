package br.com.periclesalmeida.atendimento.repository;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AtendimentoRepository extends MongoRepository<Atendimento, String> {

	@Query("{'dataHoraCadastro' : { $gte: ?0, $lte: ?1 }, 'servico.id': { $in: ?2} }")
	List<Atendimento> listarPorPeriodoDeCadastroIhServicos(LocalDateTime dataInicial, LocalDateTime dataFinal, List<String> idServicos);


	@Query("{'dataHoraChamada' : { $gte: ?0, $lte: ?1 }, 'servico.id': { $in: ?2} }")
	List<Atendimento> listarPorPeriodoDeChamadaIhServicos(LocalDateTime atStartOfDay, LocalDateTime atTime, List<String> idsServico);


}
