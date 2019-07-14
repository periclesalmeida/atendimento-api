package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.impl.ServicoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServicoServiceImplTest  extends AbstractServiceImplTest<Servico, Long> {

    private final long SEQUENCIAL_SERVICO_1 = 1L;
    private final long SEQUENCIAL_SERVICO_2 = 2L;
    private final String SIGLA_SERVICO_NEG = "NEG";
    private final String SIGLA_SERVICO_FIN = "FIN";
    private final String TIPO_COR_G = "G";
    private final String DESCRICAO = "descricao";

    @Mock
    private ServicoRepository servicoRepositoryMock;

    private ServicoService servicoService;

    @Override
    public void inicializarContexto() {
        this.servicoService = new ServicoServiceImpl(servicoRepositoryMock);
    }

    @Override
    protected GenericService<Servico, Long> getService() {
        return servicoService;
    }

    @Override
    protected JpaRepository<Servico, Long> getRepositoryMock() {
        return servicoRepositoryMock;
    }

    @Override
    protected Long getId() {
        return getEntidade().getSequencial();
    }

    @Override
    public void aoConsultarTodosDeveriaDelegarParaOhRepositorio() {
        getService().consultarTodos();
        verify(getRepositoryMock()).findAll(Sort.by(DESCRICAO));
    }

    @Test(expected = NegocioException.class)
    public void aoSalvarEntidadeComCorInvalidaDeveriaLancarExcecao() {
        getService().salvar(getServicoComCorInvalida());
    }

    @Test
    public void aoSalvarDeveriaDelegarParaOhRepositorioFindBySiglaContainsAllIgnoreCase() {
        when(servicoRepositoryMock.findBySiglaContainsAllIgnoreCase(anyString())).thenReturn(Optional.of(getServicoComCorAzul()));
        getService().salvar(getServicoComCorAzul());
        verify(servicoRepositoryMock).findBySiglaContainsAllIgnoreCase(anyString());
    }

    @Test(expected = NegocioException.class)
    public void aoSalvarEntidadeQueJaExisteDeveriaLancarExcecaoComNegocioException() {
        when(servicoRepositoryMock.findBySiglaContainsAllIgnoreCase(anyString())).thenReturn(Optional.of(getServicoComCorAzul()));
        getService().salvar(getServicoComCorVermelho());
    }

    @Override
    protected Servico getEntidade() {
        return getServicoComCorVermelho();
    }

    private Servico getServicoComCorInvalida() {
        Servico servico = new Servico();
        servico.setTipoCor(TIPO_COR_G);
        return servico;
    }

    private Servico getServicoComCorVermelho() {
        Servico servico = new Servico();
        servico.setSequencial(SEQUENCIAL_SERVICO_1);
        servico.setSigla(SIGLA_SERVICO_FIN);
        servico.setTipoCor(TipoCor.VERMELHO.getValue());
        return servico;
    }

    private Servico getServicoComCorAzul() {
        Servico servico =  new Servico();
        servico.setSequencial(SEQUENCIAL_SERVICO_2);
        servico.setSigla(SIGLA_SERVICO_NEG);
        servico.setTipoCor(TipoCor.AZUL.getValue());
        return servico;
    }
}
