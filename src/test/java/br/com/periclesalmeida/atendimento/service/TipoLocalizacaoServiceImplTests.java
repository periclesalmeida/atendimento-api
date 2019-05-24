package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.TipoLocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.impl.TipoLocalizacaoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.verify;

public class TipoLocalizacaoServiceImplTests extends AbstractServiceImplTest<TipoLocalizacao, Integer> {

    @Mock
    private TipoLocalizacaoRepository tipoLocalizacaoRepositoryMock;
    private TipoLocalizacaoService tipoLocalizacaoService;

    @Override
    public void inicializarContexto() {
        tipoLocalizacaoService = new TipoLocalizacaoServiceImpl(tipoLocalizacaoRepositoryMock);
    }

    @Override
    protected GenericService getService() {
        return tipoLocalizacaoService;
    }

    @Override
    protected JpaRepository<TipoLocalizacao, Integer> getRepositoryMock() {
        return tipoLocalizacaoRepositoryMock;
    }

    @Override
    public void aoConsultarTodosDeveriaDelegarParaOhRepositorio() {
        getService().consultarTodos();
        verify(getRepositoryMock()).findAll(Sort.by("descricao"));
    }

    @Override
    protected Integer getId() {
        return getEntidade().getCodigo();
    }

    @Override
    protected TipoLocalizacao getEntidade() {
        return new TipoLocalizacao();
    }
}
