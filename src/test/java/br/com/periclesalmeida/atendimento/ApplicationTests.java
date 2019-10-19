package br.com.periclesalmeida.atendimento;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
		UnitTests.class,
		AcceptanceTests.class
})
public class ApplicationTests {
}
