package br.com.periclesalmeida.atendimento.integration.tipo_localizacao;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/tipo_localizacao",
        tags = "@tipo_localizacao",
        glue = "br/com/periclesalmeida/atendimento/integration/tipo_localizacao"
)
public class TipoLocalizacaoUseCaseTest {
}
