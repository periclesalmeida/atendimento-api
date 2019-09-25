package br.com.periclesalmeida.atendimento.integration.servico;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features="src/test/resources/features/servico",
        tags = "@servico",
        glue = "br/com/periclesalmeida/atendimento/integration/servico"
)
public class ServicoUseCaseTest {}
