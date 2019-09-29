package br.com.periclesalmeida.atendimento.integration.formatador.impl;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.integration.formatador.AbstractFormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;

import java.util.ArrayList;
import java.util.List;

public class FormatadorMensagemServicoImpl extends AbstractFormatadorDeMensagem<Servico> implements FormatadorDeMensagem<Servico> {

    private static final String PADRAO = "Descrição: ''{0}'', Sigla: ''{1}'', Tipo Cor: ''{2}'', Número Atendimento: ''{3}'', Ativo: ''{4}''";

    @Override
    protected String obterPadrao() {
        return PADRAO;
    }

    @Override
    protected List<Object> gerarParametros(Servico servido) {
        List<Object> listaParametros = new ArrayList<Object>();
        listaParametros.add(servido.getDescricao());
        listaParametros.add(servido.getSigla());
        listaParametros.add( servido.getTipoCorEnum().getName());
        listaParametros.add(servido.getNumeroAtendimentoAtual());
        listaParametros.add(VerificadorUtil.naoEstaNulo(servido.getAtivo()) && servido.getAtivo()  ?  "SIM" : "NÃO");
        return listaParametros;
    }
}
