package br.com.periclesalmeida.atendimento.integration.localizacao;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/localizacao",
        tags = "@localizacao",
        glue = "br/com/periclesalmeida/atendimento/integration/localizacao"
)
public class LocalizacaoUseCaseTest {
}
