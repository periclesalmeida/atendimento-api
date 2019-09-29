package br.com.periclesalmeida.atendimento.integration.mapper;

import br.com.periclesalmeida.atendimento.domain.Localizacao;

import java.util.Map;

public class LocalizacaoMapper implements Mapper<Localizacao> {

    @Override
    public Localizacao map(Map<String, String> map) {
        return new Localizacao(map.get("id"),
                map.get("descricao"),
                map.get("ativo").equals("SIM") ? true : false);
    }
}
