package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;

public interface AtendimentoService {

	Atendimento consultarPorId(String identificador);
    Atendimento gerar(String sequencialServico);
    Atendimento gerarPrioridade(String sequencialServico);
    AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDaLocalizacao(String sequencialLocalizacao);
    Atendimento chamarProximo(String sequencialLocalizacao);
    Atendimento chamarNovamente(String sequencial, String sequencialLocalizacao) ;
}
