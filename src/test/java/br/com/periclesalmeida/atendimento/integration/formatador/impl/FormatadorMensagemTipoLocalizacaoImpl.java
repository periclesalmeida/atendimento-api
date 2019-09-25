package br.com.periclesalmeida.atendimento.integration.formatador.impl;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.integration.formatador.AbstractFormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;

import java.util.ArrayList;
import java.util.List;

public class FormatadorMensagemTipoLocalizacaoImpl extends AbstractFormatadorDeMensagem<TipoLocalizacao> implements FormatadorDeMensagem<TipoLocalizacao> {

    private static final String PADRAO = "Descrição: '{0}'";

    @Override
    protected String obterPadrao() {
        return PADRAO;
    }

    @Override
    protected List<Object> gerarParametros(TipoLocalizacao tipoLocalizacao) {
        List<Object> listaParametros = new ArrayList<Object>();
        listaParametros.add(tipoLocalizacao.getDescricao());
        return listaParametros;
    }
}
