package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.impl.ServicoServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServicoServiceImplTest  extends AbstractServiceImplTest<Servico, String> {

	private final Integer NUMERO_ATENDIMENTO_ATUAL_ZERO = 0;
	private final Integer NUMERO_ATENDIMENTO_ATUAL_CINCO = 5;
	private final Integer NUMERO_ATENDIMENTO_ATUAL_SEIS = 6;
	private final String SEQUENCIAL_SERVICO_1 = "1";
	private final String SEQUENCIAL_SERVICO_2 = "2";
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
	protected GenericService<Servico, String> getService() {
		return servicoService;
	}

	@Override
	protected MongoRepository<Servico, String> getRepositoryMock() {
		return servicoRepositoryMock;
	}

	@Override
	protected String getId() {
		return getEntidade().getId();
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

	@Test
	public void aoIncluirDeveriaSetarEntidadeComoAtivo() {
		getService().incluir(getEntidade());
		Servico servicoToSave = captureAhEntidadeAoIncluir();
		assertTrue(servicoToSave.getAtivo());
	}

	@Test
	public void aoSalvarSeOhNumeroAtendimentoAtualEstiverNuloDeveriaSetarComoZero() {
		getService().salvar(getEntidade());
		Servico servicoToSave = captureAhEntidadeAoSalvar();
		assertEquals(NUMERO_ATENDIMENTO_ATUAL_ZERO, servicoToSave.getNumeroAtendimentoAtual());
	}

	@Test
	public void aoSalvarSeOhNumeroAtendimentoAtualEstiverPreenchidoDeveriaFazerNada() {
		getService().salvar(getServicoComCorAzul());
		Servico servicoToSave = captureAhEntidadeAoSalvar();
		assertEquals(NUMERO_ATENDIMENTO_ATUAL_CINCO, servicoToSave.getNumeroAtendimentoAtual());
	}

	@Test
	public void aoRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtualDeveriaDelegarParaRepositoryFindById() {
		when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getServicoComCorAzul()));
		servicoService.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1);
		verify(getRepositoryMock()).findById(getId());
	}

	@Test
	public void aoRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtualDeveriaDelegarParaRepositorySave() {
		when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getServicoComCorAzul()));
		servicoService.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1);
		verify(getRepositoryMock()).save(getServicoComCorAzul());
	}

	@Test
	public void aoRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtualDeveriaDeveriaIncrementarOhNumeroAtendimentoMaisUm() {
		when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getServicoComCorAzul()));
		servicoService.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1);
		Servico servicoToSave = captureAhEntidadeAoSalvar();
		assertEquals(NUMERO_ATENDIMENTO_ATUAL_SEIS, servicoToSave.getNumeroAtendimentoAtual());
	}

	@Override
	protected Servico getEntidade() {
		return getServicoComCorVermelho();
	}

	private Servico captureAhEntidadeAoSalvar() {
		ArgumentCaptor<Servico> servicoArgument = ArgumentCaptor.forClass(Servico.class);
		verify(servicoRepositoryMock).save(servicoArgument.capture());
		return servicoArgument.getValue();
	}

	private Servico captureAhEntidadeAoIncluir() {
		ArgumentCaptor<Servico> servicoArgument = ArgumentCaptor.forClass(Servico.class);
		verify(servicoRepositoryMock).insert(servicoArgument.capture());
		return servicoArgument.getValue();
	}


	private Servico getServicoComCorInvalida() {
		Servico servico = new Servico();
		servico.setTipoCor(TIPO_COR_G);
		return servico;
	}

	private Servico getServicoComCorVermelho() {
		Servico servico = new Servico();
		servico.setId(SEQUENCIAL_SERVICO_1);
		servico.setSigla(SIGLA_SERVICO_FIN);
		servico.setTipoCor(TipoCor.VERMELHO.getValue());
		return servico;
	}

	private Servico getServicoComCorAzul() {
		Servico servico =  new Servico();
		servico.setId(SEQUENCIAL_SERVICO_2);
		servico.setSigla(SIGLA_SERVICO_NEG);
		servico.setNumeroAtendimentoAtual(NUMERO_ATENDIMENTO_ATUAL_CINCO);
		servico.setTipoCor(TipoCor.AZUL.getValue());
		return servico;
	}
}
