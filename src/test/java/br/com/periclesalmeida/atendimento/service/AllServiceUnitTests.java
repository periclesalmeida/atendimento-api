package br.com.periclesalmeida.atendimento.service;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TipoLocalizacaoServiceImplTests.class,
        ServicoServiceImplTest.class,
        LocalizacaoServiceImplTest.class
})
public class AllServiceUnitTests {}
