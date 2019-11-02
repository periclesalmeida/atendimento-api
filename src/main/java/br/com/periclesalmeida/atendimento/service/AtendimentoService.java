package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoChamadoDTO;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;

import java.util.List;

public interface AtendimentoService {

	Atendimento consultarPorId(String id);
    Atendimento gerar(String idServico);
    Atendimento gerarPrioridade(String idServico);
    Atendimento chamarProximo(String idLocalizacao);
    Atendimento chamarNovamente(String id, String idLocalizacao) ;

    AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDosServicos(List<String> idsServico);
    AtendimentoMovimentacaoChamadoDTO consultarMovimentacaoChamadaDoDiaDosServicos(List<String> idsServico);
}
