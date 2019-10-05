package br.com.periclesalmeida.atendimento.integration.mapper;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class AtendimentoMapper implements Mapper<Atendimento> {

    private final long UM = 1L;
    private final String STIRNG_DATA_ATUAL_MENOS_1_MINUTO = "DATA_ATUAL_MENOS_1_MINUTO";
    private final String STRING_DATA_ATUAL = "DATA_ATUAL";
    private final String STRING_DATA_ONTEM = "DATA_ONTEM";
    private final LocalDateTime DATA_ATUAL = LocalDateTime.now();
    private final LocalDateTime DATA_ATUAL_MENOS_1_MINUTO = DATA_ATUAL.minusMinutes(UM);
    private final LocalDateTime DATA_ONTEM = DATA_ATUAL.minusDays(UM);


    @Override
    public Atendimento map(Map<String, String> map) {
        return new Atendimento(map.get("id"), Integer.parseInt(map.get("número atendimento")),
                converterStringParaLocalDateTime(map.get("data cadastro")), converterStringParaLocalDateTime(map.get("data apresentacao")),
                converterStringParaLocalDateTime(map.get("data chamada")),
                "SIM".equals(map.get("prioridade"))
        );
    }

    private LocalDateTime converterStringParaLocalDateTime(String dataFormatoString) {
        return VerificadorUtil.naoEstaNuloOuVazio(dataFormatoString) ?
                (getMapData().get(dataFormatoString) == null ?
                        LocalDateTime.parse(dataFormatoString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : getMapData().get(dataFormatoString))
                :null;
    }

    private Map<String, LocalDateTime> getMapData() {
        Map<String, LocalDateTime> map = new HashMap<>();
        map.put(STRING_DATA_ATUAL, DATA_ATUAL);
        map.put(STRING_DATA_ONTEM, DATA_ONTEM);
        map.put(STIRNG_DATA_ATUAL_MENOS_1_MINUTO, DATA_ATUAL_MENOS_1_MINUTO);
        return map;
    }
}
