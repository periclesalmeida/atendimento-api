package br.com.periclesalmeida.atendimento.integration.formatador.impl;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.integration.formatador.AbstractFormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;

import java.util.ArrayList;
import java.util.List;

public class FormatadorMensagemUsuarioImpl extends AbstractFormatadorDeMensagem<Usuario> implements FormatadorDeMensagem<Usuario> {

    private static final String PADRAO = "Login: ''{0}''";

    @Override
    protected String obterPadrao() {
        return PADRAO;
    }

    @Override
    protected List<Object> gerarParametros(Usuario entidade) {
        List<Object> listaParametros = new ArrayList<Object>();
        listaParametros.add(entidade.getLogin());
        return listaParametros;
    }
}
