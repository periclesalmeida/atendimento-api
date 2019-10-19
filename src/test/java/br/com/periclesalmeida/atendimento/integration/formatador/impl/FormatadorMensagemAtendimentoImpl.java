package br.com.periclesalmeida.atendimento.integration.formatador.impl;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.integration.formatador.AbstractFormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormatadorMensagemAtendimentoImpl extends AbstractFormatadorDeMensagem<Atendimento> implements FormatadorDeMensagem<Atendimento> {

    private static final String PADRAO = "Número Atendimento: ''{0}'', Data Hora Cadastro: ''{1}'', Data Hora Apresentação: ''{2}'', Data Hora Chamada: ''{3}'', " +
            "Prioridade: ''{4}'', Localizacao: ''{5}'', Serviço: ''{6}'', Usuário:''{7}''";

    @Override
    protected String obterPadrao() {
        return PADRAO;
    }

    @Override
    protected List<Object> gerarParametros(Atendimento entidade) {
        List<Object> listaParametros = new ArrayList<Object>();
        listaParametros.add(entidade.getNumeroAtendimentoFormatado());
        listaParametros.add(entidade.getDataHoraCadastroFormatada());
        listaParametros.add(entidade.getDataHoraApresentacaoFormatada());
        listaParametros.add(entidade.getDataHoraChamadaFormadata());
        listaParametros.add(entidade.getIndicadorPrioridade() ? "SIM" : "NÃO");
        listaParametros.add(Optional.ofNullable(entidade.getLocalizacao()).isPresent() ? new FormatadorMensagemLocalizacaoImpl().formatarMensagem(entidade.getLocalizacao()) : null);
        listaParametros.add(new FormatadorMensagemServicoImpl().formatarMensagem(entidade.getServico()));
        listaParametros.add(Optional.ofNullable(entidade.getUsuario()).isPresent() ? new FormatadorMensagemUsuarioImpl().formatarMensagem(entidade.getUsuario()): null);
        return listaParametros;
    }
}
