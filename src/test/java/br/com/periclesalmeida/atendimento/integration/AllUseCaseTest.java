package br.com.periclesalmeida.atendimento.integration;

import br.com.periclesalmeida.atendimento.integration.atendimento.AtendimentoUseCaseTest;
import br.com.periclesalmeida.atendimento.integration.localizacao.LocalizacaoUseCaseTest;
import br.com.periclesalmeida.atendimento.integration.servico.ServicoUseCaseTest;
import br.com.periclesalmeida.atendimento.integration.tipo_localizacao.TipoLocalizacaoUseCaseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TipoLocalizacaoUseCaseTest.class,
        ServicoUseCaseTest.class,
        LocalizacaoUseCaseTest.class,
        AtendimentoUseCaseTest.class
})
public class AllUseCaseTest {
}
