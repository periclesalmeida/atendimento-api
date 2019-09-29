package br.com.periclesalmeida.atendimento.integration.formatador.impl;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.integration.formatador.AbstractFormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FormatadorMensagemLocalizacaoImpl extends AbstractFormatadorDeMensagem<Localizacao> implements FormatadorDeMensagem<Localizacao> {

    private static final String PADRAO = "Descrição: ''{0}'', Tipo: ''{1}'', Ativo: ''{2}'', Serviços: ''{3}''";

    @Override
    protected String obterPadrao() {
        return PADRAO;
    }

    @Override
    protected List<Object> gerarParametros(Localizacao entidade) {
        List<Object> listaParametros = new ArrayList<Object>();
        listaParametros.add(entidade.getDescricao());
        listaParametros.add(VerificadorUtil.naoEstaNulo(entidade.getTipo()) ? entidade.getTipo().getDescricao() : "");
        listaParametros.add(VerificadorUtil.naoEstaNulo(entidade.getAtivo()) && entidade.getAtivo()  ?  "SIM" : "NÃO");
        String mensagemServicos = formatarMensagemServicos(entidade.getServicos());
        listaParametros.add(mensagemServicos);
        return listaParametros;
    }

    private String formatarMensagemServicos(Set<Servico> servicos) {
        return Optional.ofNullable(servicos).isPresent() ?
                new FormatadorMensagemServicoImpl().formatarMensagem(converterListaParaArray(servicos)) :
                "";
    }

    private Servico[] converterListaParaArray(Set<Servico> lista) {
        return  lista.stream().toArray(Servico[]::new);
    }
}
