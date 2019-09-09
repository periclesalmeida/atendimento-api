package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;

public interface AtendimentoService {

	Atendimento consultarPorId(Long identificador);
    Atendimento gerar(Long sequencialServico);
    Atendimento gerarPrioridade(Long sequencialServico);
    AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDaLocalizacao(Long sequencialLocalizacao);
    Atendimento chamarProximo(Long sequencialLocalizacao);
    Atendimento chamarNovamente(Long sequencial, Long sequencialLocalizacao) ;
}
