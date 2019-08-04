package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.service.impl.AtendimentoServiceImpl;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TipoLocalizacaoServiceImplTests.class,
        ServicoServiceImplTest.class,
        LocalizacaoServiceImplTest.class,
        UsuarioServiceImplTest.class,
        AtendimentoServiceImplTest.class
})
public class AllServiceUnitTests {}
