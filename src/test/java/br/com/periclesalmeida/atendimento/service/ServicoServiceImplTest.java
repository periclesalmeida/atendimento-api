package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.impl.ServicoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import static org.mockito.Mockito.verify;

public class ServicoServiceImplTest  extends AbstractServiceImplTest<Servico, Long> {

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
        verify(getRepositoryMock()).findAll(Sort.by("descricao"));
    }

    @Override
    protected Servico getEntidade() {
        return criarServicoComCorVermelha();
    }

    private Servico criarServicoComCorVermelha() {
        Servico servico = new Servico();
        servico.setTipoCor(TipoCor.VERMELHO.getValue());
        return servico;
    }
}
