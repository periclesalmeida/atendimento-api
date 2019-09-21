package br.com.periclesalmeida.atendimento.integration.tipo_localizacao;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features="src/test/resources/features/tipo_localizacao")
public class TipoLocalizacaoUseCaseTest {
}
