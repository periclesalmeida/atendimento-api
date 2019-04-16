package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.TipoLocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.impl.TipoLocalizacaoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.verify;

public class TipoLocalizacaoServiceImplTests extends AbstractServiceImplTest {

    private TipoLocalizacaoRepository tipoLocalizacaoRepositoryMock;
    private TipoLocalizacaoService tipoLocalizacaoService;

    @Override
    public void inicializarContexto() {
        tipoLocalizacaoRepositoryMock = Mockito.mock(TipoLocalizacaoRepository.class);
        tipoLocalizacaoService = new TipoLocalizacaoServiceImpl((TipoLocalizacaoRepository) getRepositoryMock());
    }

    @Override
    protected GenericService getService() {
        return tipoLocalizacaoService;
    }

    @Override
    protected JpaRepository getRepositoryMock() {
        return tipoLocalizacaoRepositoryMock;
    }

    @Override
    public void aoConsultarTodosDeveriaDelegarParaOhRepositorio() {
        getService().consultarTodos();
        verify(getRepositoryMock()).findAll(Sort.by("descricao"));
    }

    @Override
    protected Object getEntidade() {
        return new TipoLocalizacao();
    }
}
