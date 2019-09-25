package br.com.periclesalmeida.atendimento.integration.formatador;

public interface FormatadorDeMensagem<OBJETO> {

    String formatarMensagem(OBJETO... objeto);
}
