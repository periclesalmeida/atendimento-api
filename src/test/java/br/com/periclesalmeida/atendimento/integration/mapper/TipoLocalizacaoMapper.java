package br.com.periclesalmeida.atendimento.integration.mapper;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;

import java.util.Map;

public class TipoLocalizacaoMapper implements Mapper<TipoLocalizacao> {

    @Override
    public TipoLocalizacao map(Map<String, String> map) {
            return new TipoLocalizacao(map.get("id"), map.get("descricao"));
    }
}
