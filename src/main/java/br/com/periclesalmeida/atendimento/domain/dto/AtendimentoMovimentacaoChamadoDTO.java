package br.com.periclesalmeida.atendimento.domain.dto;

import br.com.periclesalmeida.atendimento.domain.Atendimento;

import java.util.List;
import java.util.stream.Collectors;

public class AtendimentoMovimentacaoChamadoDTO {

	private List<Atendimento> atendimentosChamados;

	public AtendimentoMovimentacaoChamadoDTO() {
	}

	public AtendimentoMovimentacaoChamadoDTO(List<Atendimento> atendimentos) {
		this.atendimentosChamados = atendimentos;
	}

	public List<Atendimento> getAtendimentosApresentados() {
		return atendimentosChamados.stream().filter(Atendimento::isApresentado)
				.sorted((t1, t2) -> {
					return t1.getDataHoraApresentacao().isAfter(t2.getDataHoraApresentacao())
							? -1 : 1;
				}).collect(Collectors.toList());
	}

	public List<Atendimento> getAtendimentosNaoApresentados() {
		return atendimentosChamados.stream().filter(Atendimento::isNaoApresentado).collect(Collectors.toList());
	}

	public Atendimento getProximoAtendimentoApresentado() {
		return getAtendimentosNaoApresentados().stream().findFirst().orElse(null);
	}

	public Atendimento getUltimoAtendimentoApresentado() {
		return getAtendimentosApresentados().stream().findFirst().orElse(null);
	}
}
