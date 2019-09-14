package br.com.periclesalmeida.atendimento.domain.dto;

import br.com.periclesalmeida.atendimento.domain.Atendimento;

import java.util.List;
import java.util.stream.Collectors;

public class AtendimentoMovimentacaoDTO {

	private List<Atendimento> atendimentos;

	public AtendimentoMovimentacaoDTO(List<Atendimento> atendimentos) {
		this.atendimentos = atendimentos;
	}

	public List<Atendimento> getAtendimentosEmEspera() {
		return atendimentos.stream().filter(Atendimento::isEmEspera).collect(Collectors.toList());
	}

	public List<Atendimento> getAtendimentosRealizados() {
		return atendimentos.stream().filter(Atendimento::isRealizado)
				.sorted((t1, t2) -> {
					return t1.getDataHoraChamada().isAfter(t2.getDataHoraChamada())
							? -1 : 1;
				}).collect(Collectors.toList());
	}
}
