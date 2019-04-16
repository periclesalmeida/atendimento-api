package br.com.periclesalmeida.atendimento;

import br.com.periclesalmeida.atendimento.service.AllServiceUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AllServiceUnitTests.class,
})
public class UnitTests {
}
