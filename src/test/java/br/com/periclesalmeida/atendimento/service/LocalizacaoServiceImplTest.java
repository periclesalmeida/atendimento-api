package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.impl.LocalizacaoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

public class LocalizacaoServiceImplTest extends AbstractServiceImplTest<Localizacao, Long> {

    @Mock
    private LocalizacaoRepository localizacaoRepositoryMock;
    private LocalizacaoService localizacaoService;

    @Override
    public void inicializarContexto() {
        this.localizacaoService = new LocalizacaoServiceImpl(localizacaoRepositoryMock);
    }

    @Override
    protected Long getId() {
        return getEntidade().getSequencial();
    }

    @Override
    protected Localizacao getEntidade() {
        return new Localizacao();
    }

    @Override
    protected GenericService<Localizacao, Long> getService() {
        return localizacaoService;
    }

    @Override
    protected JpaRepository<Localizacao, Long> getRepositoryMock() {
        return localizacaoRepositoryMock;
    }
}
