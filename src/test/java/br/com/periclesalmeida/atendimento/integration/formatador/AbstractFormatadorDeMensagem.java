package br.com.periclesalmeida.atendimento.integration.formatador;

import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.List;

public abstract class AbstractFormatadorDeMensagem<OBJETO> implements FormatadorDeMensagem<OBJETO> {

    protected static final String ABRIR_COLCHETES = "[";
    protected static final String FECHAR_COLCHETES = "]";
    protected static final String DIVISOR = "; ";

    protected abstract String obterPadrao();
    protected abstract List<Object> gerarParametros(OBJETO objeto);

    @Override
    public String formatarMensagem(OBJETO... objetos) {
        String objetosFormatados = formatarObjetos(objetos);
        return gerarMensagem(objetosFormatados);
    }

    private String formatarObjetos(OBJETO[] objetos) {
        StringBuilder objetosFormatados = new StringBuilder();
        for (OBJETO objeto : objetos) {
            String camposFormatados = formatarCampos(objeto);
            objetosFormatados.append(camposFormatados);
            objetosFormatados.append(DIVISOR);
        }
        return retirarUltimoPontoIhVirgulaComEspaco(objetosFormatados.toString());
    }

    private String formatarCampos(OBJETO objetc) {
        Object[] parametros = gerarParametros(objetc).toArray();
        return MessageFormat.format(obterPadrao(), parametros).replace("'", "''");
    }

    protected String retirarUltimoPontoIhVirgulaComEspaco(String mensagem) {
        return StringUtils.substringBeforeLast(mensagem, DIVISOR);
    }

    private String gerarMensagem(String dadosGravados) {
        return ABRIR_COLCHETES + dadosGravados + FECHAR_COLCHETES;
    }
}
