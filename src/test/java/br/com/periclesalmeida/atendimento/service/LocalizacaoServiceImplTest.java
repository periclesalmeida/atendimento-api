package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.impl.LocalizacaoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocalizacaoServiceImplTest extends AbstractServiceImplTest<Localizacao, Long> {

    private final int CODIGO_TIPO_LOCALIZACA_1 = 1;
    private final long SEQUENCIAL_LOCALIZACAL_1 = 1L;
    private final long SEQUENCIAL_LOCALIZACAO_2 = 2L;
    private final String DESCRICAO_TIPO_LOCALIZACAO_SALA = "SALA";
    private final String DESCRICAO_LOCALIZACAO_A = "A";
    private final String DESCRICAO_LOCALIZACAO_B = "B";

    @Mock
    private LocalizacaoRepository localizacaoRepositoryMock;
    private LocalizacaoService localizacaoService;

    @Override
    public void inicializarContexto() {
        this.localizacaoService = new LocalizacaoServiceImpl(localizacaoRepositoryMock);
    }

    @Test
    public void aoSalvarDeveriaDelegarParaOhRepositorioFindBySiglaContainsAllIgnoreCase() {
        when(localizacaoRepositoryMock.findByDescricaoContainsAndTipoAllIgnoreCase(anyString(), any()))
                .thenReturn(Optional.of(getLocalizacaoA()));
        getService().salvar(getLocalizacaoA());
        verify(localizacaoRepositoryMock).findByDescricaoContainsAndTipoAllIgnoreCase(anyString(), any());
    }

    @Test(expected = NegocioException.class)
    public void aoSalvarEntidadeQueJaExisteDeveriaLancarExcecaoComNegocioException() {
        when(localizacaoRepositoryMock.findByDescricaoContainsAndTipoAllIgnoreCase(anyString(), any()))
                .thenReturn(Optional.of(getLocalizacaoA()));
        getService().salvar(getLocalizacaoB());
    }

    @Test
    public void aoIncluirDeveriaSetarEntidadeComoAtivo() {
        getService().incluir(getLocalizacaoA());

        ArgumentCaptor<Localizacao> localizacaoArgument = ArgumentCaptor.forClass(Localizacao.class);
        verify(localizacaoRepositoryMock).save(localizacaoArgument.capture());
        Localizacao localizacaoToSave = localizacaoArgument.getValue();

        assertTrue(localizacaoToSave.getAtivo());
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


    private Localizacao getLocalizacaoA() {
        Localizacao localizacao = new Localizacao();
        localizacao.setSequencial(SEQUENCIAL_LOCALIZACAL_1);
        localizacao.setDescricao(DESCRICAO_LOCALIZACAO_A);
        localizacao.setTipo(getTipoLocalizacaoSala());
        return localizacao;
    }

    private Localizacao getLocalizacaoB() {
        Localizacao localizacao = new Localizacao();
        localizacao.setSequencial(SEQUENCIAL_LOCALIZACAO_2);
        localizacao.setDescricao(DESCRICAO_LOCALIZACAO_B);
        localizacao.setTipo(getTipoLocalizacaoSala());
        return localizacao;
    }

    private TipoLocalizacao getTipoLocalizacaoSala() {
        TipoLocalizacao tipoLocalizacao = new TipoLocalizacao();
        tipoLocalizacao.setCodigo(CODIGO_TIPO_LOCALIZACA_1);
        tipoLocalizacao.setDescricao(DESCRICAO_TIPO_LOCALIZACAO_SALA);
        return tipoLocalizacao;
    }
}
