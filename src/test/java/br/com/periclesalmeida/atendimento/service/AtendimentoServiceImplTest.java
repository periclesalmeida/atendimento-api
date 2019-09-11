package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.AtendimentoRepository;
import br.com.periclesalmeida.atendimento.service.impl.AtendimentoServiceImpl;
import br.com.periclesalmeida.atendimento.util.DataUtils;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AtendimentoServiceImplTest {

	private static final String SEQUENCIAL_ATENDIMENTO_2L = "2";
	private static final String DESCRICAO_LOCALIZACAO_A = "A";
	private static final String SEQUENCIAL_LOCALIZACAO_1L = "1";
	private static final String SEQUENCIAL_ATENDIMENTO_1L = "1";
	private static final Integer NUMERO_ATENDIMENTO_1 = 1;
	private static final String SEQUENCIAL_SERVICO_1L = "1";

	@Mock
	private AtendimentoRepository atendimentoRepositoryMock;
	
	@Mock
	private ServicoService servicoServiceMock;
	@Mock
	private LocalizacaoService localizacaoServiceMock;

	private AtendimentoService atendimentoService;

	@Before
	public void inicializarContexto() {
		this.atendimentoService = new AtendimentoServiceImpl(atendimentoRepositoryMock, servicoServiceMock, localizacaoServiceMock);
	}
	
	@Test 
	public void aoChamarProximoDeveriaDelegarParaOhRepositorySave() throws Exception {
		when(atendimentoRepositoryMock.listarPorLocalizacaoIhData(SEQUENCIAL_LOCALIZACAO_1L, LocalDate.now()))
		.thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());

		getService().chamarProximo(SEQUENCIAL_LOCALIZACAO_1L);
		verify(getRepositoryMock()).save(getEntidade());
	} 
	
	@Test(expected = NegocioException.class)
	public void aoChamarProximoDeveriaIhNaoExisteProximoDeveriaLancarExcecao() throws Exception {
		when(atendimentoRepositoryMock.listarPorLocalizacaoIhData(SEQUENCIAL_LOCALIZACAO_1L, LocalDate.now()))
		.thenReturn(null);
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());

		getService().chamarProximo(SEQUENCIAL_LOCALIZACAO_1L);
	} 

	@Test 
	public void aoChamarProximoDeveriaDelegarParaOhLocalizacaoServiceConsultarPorId() throws Exception {
		when(atendimentoRepositoryMock.listarPorLocalizacaoIhData(SEQUENCIAL_LOCALIZACAO_1L, LocalDate.now()))
		.thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());

		getService().chamarProximo(SEQUENCIAL_LOCALIZACAO_1L);
		verify(localizacaoServiceMock).consultarPorId(Mockito.anyString());
	} 

	@Test 
	public void aoChamarProximoDeveriaDelegarParaOhRepositoryListarPorLocalizacaoIhData() throws Exception {
		when(atendimentoRepositoryMock.listarPorLocalizacaoIhData(SEQUENCIAL_LOCALIZACAO_1L, LocalDate.now()))
		.thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));

		getService().chamarProximo(SEQUENCIAL_LOCALIZACAO_1L);
		verify(atendimentoRepositoryMock).listarPorLocalizacaoIhData(Mockito.anyString(), any(LocalDate.class));
	} 

	@Test
	public void aoConsultarMovimentacaoDoDiaDaLocalizacaoDeveriaDelegarParaOhRepositoryListarMovimentacaoPorLocalizacaoIhData() throws Exception {
		when(atendimentoRepositoryMock.listarPorLocalizacaoIhData(SEQUENCIAL_LOCALIZACAO_1L, LocalDate.now()))
		.thenReturn(Arrays.asList(getAtendimentoPrioridade()));
		getService().consultarMovimentacaoDoDiaDaLocalizacao(SEQUENCIAL_LOCALIZACAO_1L);
		verify(atendimentoRepositoryMock).listarPorLocalizacaoIhData(Mockito.anyString(), any(LocalDate.class));
	} 

	@Test
	public void aoChamarNovamenteDeveriaDelegarParaOhLocalizacaoServiceConsultarPorId() throws Exception {
		when(getRepositoryMock().findById(SEQUENCIAL_ATENDIMENTO_1L)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(SEQUENCIAL_ATENDIMENTO_1L, SEQUENCIAL_LOCALIZACAO_1L);
		verify(localizacaoServiceMock).consultarPorId(Mockito.anyString());
	}

	@Test
	public void aoChamarNovamenteDeveriaDelegarParaRepositorySave() throws Exception {
		when(getRepositoryMock().findById(SEQUENCIAL_ATENDIMENTO_1L)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(SEQUENCIAL_ATENDIMENTO_1L, SEQUENCIAL_LOCALIZACAO_1L);
		verify(getRepositoryMock()).save(getEntidade());
	}

	@Test
	public void aoChamarNovamenteDeveriaSetarLocalizacaoNoAtendimento() {
		when(getRepositoryMock().findById(SEQUENCIAL_ATENDIMENTO_1L)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(SEQUENCIAL_ATENDIMENTO_1L, SEQUENCIAL_LOCALIZACAO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getLocalizacaoA(), atendimentoToSave.getLocalizacao());
	}

	@Test
	public void aoChamarNovamenteDeveriaSetarDataHoraDaChamadaNoAtendimento() {
		when(getRepositoryMock().findById(SEQUENCIAL_ATENDIMENTO_1L)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(SEQUENCIAL_LOCALIZACAO_1L)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(SEQUENCIAL_ATENDIMENTO_1L, SEQUENCIAL_LOCALIZACAO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DataUtils.converterDataComHorarioParaString(new Date()), DataUtils.converterDataComHorarioParaString(atendimentoToSave.getDataHoraChamada()) );
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarPrioridadeComoFalse() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertTrue(atendimentoToSave.getIndicadorPrioridade());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarDataHoraDeCadastro() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DataUtils.converterDataComHorarioParaString(new Date()), atendimentoToSave.getDataHoraCadastroFormatada());
	} 

	@Test
	public void aoGerarPrioridadeDeveriaSetarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getServicoComCorVermelho().getNumeroAtendimentoAtual(), atendimentoToSave.getNumeroAtendimento());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarOhServicoConsultadoNoAtendimento() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(atendimentoToSave.getServico(), getServicoComCorVermelho());
	}

	@Test
	public void aoGerarPrioridadeDeveriaDelegarParaOhServicoServiceRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		verify(servicoServiceMock).retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(Mockito.anyString());
	}

	@Test
	public void aoGerarPrioridadeDeveriaDelegarParaOhSave() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(SEQUENCIAL_SERVICO_1L);
		verify(getRepositoryMock()).save(getEntidade());
	}


	@Test
	public void aoGerarDeveriaSetarPrioridadeComoFalse() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertFalse(atendimentoToSave.getIndicadorPrioridade());
	}

	@Test
	public void aoGerarDeveriaSetarDataHoraDeCadastro() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DataUtils.converterDataComHorarioParaString(new Date()), atendimentoToSave.getDataHoraCadastroFormatada());
	} 

	@Test
	public void aoGerarDeveriaSetarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getServicoComCorVermelho().getNumeroAtendimentoAtual(), atendimentoToSave.getNumeroAtendimento());
	}

	@Test
	public void aoGerarDeveriaSetarOhServicoConsultadoNoAtendimento() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(atendimentoToSave.getServico(), getServicoComCorVermelho());
	}

	@Test
	public void aoGerarDeveriaDelegarParaOhServicoServiceRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		verify(servicoServiceMock).retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(Mockito.anyString());
	}

	@Test
	public void aoGerarDeveriaDelegarParaOhSave() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(SEQUENCIAL_SERVICO_1L)).
		thenReturn(getServicoComCorVermelho());
		getService().gerar(SEQUENCIAL_SERVICO_1L);
		verify(getRepositoryMock()).save(getEntidade());
	}

	@Test
	public void aoConsultarPorIdDeveriaDelegarParaOhRepositorio() {
		when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getEntidade()));
		getService().consultarPorId(getId());
		verify(getRepositoryMock()).findById(getId());
	}

	@Test(expected = EmptyResultDataAccessException.class)
	public void aoConsultarPorIdIhDelegarParaOhRepositorioIhRetornouNuloDeveriaLancarExcecaoEmptyResultDataAccessException() {
		when(getRepositoryMock().findById(getId())).thenReturn(Optional.empty());
		getService().consultarPorId(getId());
	}

	private Atendimento captureAhEntidadeAoSalvar() {
		ArgumentCaptor<Atendimento> atendimentoArgument = ArgumentCaptor.forClass(Atendimento.class);
		verify(atendimentoRepositoryMock).save(atendimentoArgument.capture());
		return atendimentoArgument.getValue();
	}

	private Servico getServicoComCorVermelho() {
		Servico servico = new Servico();
		servico.setSequencial(SEQUENCIAL_SERVICO_1L);
		servico.setTipoCor(TipoCor.VERMELHO.getValue());
		servico.setNumeroAtendimentoAtual(NUMERO_ATENDIMENTO_1);
		return servico;
	}

	private Localizacao getLocalizacaoA() {
		Localizacao localizacao = new Localizacao();
		localizacao.setSequencial(SEQUENCIAL_LOCALIZACAO_1L);
		localizacao.setDescricao(DESCRICAO_LOCALIZACAO_A);
		return localizacao;
	}

	private String getId() {
		return getEntidade().getSequencial();
	}

	private Atendimento getEntidade() {
		return getAtendimentoNaoPrioridade();
	}

	private Atendimento getAtendimentoPrioridade() {
		Atendimento atendimento = new Atendimento();
		atendimento.setSequencial(SEQUENCIAL_ATENDIMENTO_2L);
		atendimento.setIndicadorPrioridade(true);
		atendimento.setDataHoraCadastro(new Date());
		return atendimento;
	}

	private Atendimento getAtendimentoNaoPrioridade() {
		Atendimento atendimento = new Atendimento();
		atendimento.setIndicadorPrioridade(false);
		atendimento.setDataHoraCadastro(new Date());
		return atendimento;
	}

	private AtendimentoService getService() {
		return atendimentoService;
	}

	private MongoRepository<Atendimento, String> getRepositoryMock() {
		return atendimentoRepositoryMock;
	}
}
