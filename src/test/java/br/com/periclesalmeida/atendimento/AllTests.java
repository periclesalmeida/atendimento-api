package br.com.periclesalmeida.atendimento;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        UnitTests.class,
        IntegrationTests.class
})
public class AllTests {

}
