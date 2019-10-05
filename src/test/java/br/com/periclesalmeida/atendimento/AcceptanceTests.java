package br.com.periclesalmeida.atendimento;

import br.com.periclesalmeida.atendimento.integration.AllUseCaseTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AllUseCaseTest.class
})
public class AcceptanceTests {
}
