package br.com.periclesalmeida.atendimento.integration.mapper;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;

import java.util.Map;

public class ServicoMapper implements Mapper<Servico> {

    @Override
    public Servico map(Map<String, String> map) {
        Servico servico = new Servico(map.get("id"),
                map.get("descricao"),
                map.get("sigla"),
                TipoCor.valueOf(map.get("cor")).getValue(),
                "SIM".equals(map.get("ativo")) ? true : false);
        servico.setNumeroAtendimentoAtual(Integer.parseInt(map.get("atendimento atual")) );
        return servico;
    }
}
