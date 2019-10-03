package br.com.periclesalmeida.atendimento.integration.atendimento;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/atendimento",
        tags = "@atendimento",
        glue = "br/com/periclesalmeida/atendimento/integration/atendimento"
)
public class AtendimentoUseCaseTest {
}
