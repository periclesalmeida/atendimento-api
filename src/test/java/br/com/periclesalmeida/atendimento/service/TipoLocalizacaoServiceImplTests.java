package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.TipoLocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.impl.TipoLocalizacaoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TipoLocalizacaoServiceImplTests extends AbstractServiceImplTest<TipoLocalizacao, String> {

    private final String CODIGO_TIPO_LOCALIZACAO_2 = "2";
    private final String DESCRICAO = "descricao";
    private final String DESCRICAO_SALA = "SALA";
    private final String CODIGO_TIPO_LOCALIZACAO_1 = "1";

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
    protected MongoRepository<TipoLocalizacao, String> getRepositoryMock() {
        return tipoLocalizacaoRepositoryMock;
    }

    @Override
    public void aoConsultarTodosDeveriaDelegarParaOhRepositorio() {
        getService().consultarTodos();
        verify(getRepositoryMock()).findAll(Sort.by(DESCRICAO));
    }

    @Test
    public void aoSalvarDeveriaDelegarParaOhRepositorioFindByDescricaoContainsAllIgnoreCase() {
        when(tipoLocalizacaoRepositoryMock.findByDescricaoContainsAllIgnoreCase(anyString())).thenReturn(Optional.of(getEntidadeComCodigoUm()));
        getService().salvar(getEntidadeComCodigoUm());
        verify(tipoLocalizacaoRepositoryMock).findByDescricaoContainsAllIgnoreCase(anyString());
    }


    @Test(expected = NegocioException.class)
    public void aoSalvarEntidadeQueJaExisteDeveriaLancarExcecaoComNegocioException() {
        when(tipoLocalizacaoRepositoryMock.findByDescricaoContainsAllIgnoreCase(anyString())).thenReturn(Optional.of(getEntidadeComCodigoUm()));
        getService().salvar(getEntidadeComCodigoDois());
    }

    @Override
    protected String getId() {
        return getEntidade().getId();
    }

    @Override
    protected TipoLocalizacao getEntidade() {
        return new TipoLocalizacao();
    }

    private TipoLocalizacao getEntidadeComCodigoUm() {
        TipoLocalizacao tipoLocalizacao = new TipoLocalizacao();
        tipoLocalizacao.setId(CODIGO_TIPO_LOCALIZACAO_1);
        tipoLocalizacao.setDescricao(DESCRICAO_SALA);
        return tipoLocalizacao;
    }

    private TipoLocalizacao getEntidadeComCodigoDois() {
        TipoLocalizacao tipoLocalizacao = new TipoLocalizacao();
        tipoLocalizacao.setId(CODIGO_TIPO_LOCALIZACAO_2);
        tipoLocalizacao.setDescricao(DESCRICAO_SALA);
        return tipoLocalizacao;
    }
}
