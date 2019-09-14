package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.AtendimentoRepository;
import br.com.periclesalmeida.atendimento.service.impl.AtendimentoServiceImpl;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AtendimentoServiceImplTest {

	private final String ID_ATENDIMENTO_2 = "2";
	private final String DESCRICAO_LOCALIZACAO_A = "A";
	private final String ID_LOCALIZACAO_1 = "1";
	private final String ID_ATENDIMENTO_1 = "1";
	private final Integer NUMERO_ATENDIMENTO_1 = 1;
	private final String ID_SERVICO_1 = "1";
	private final LocalDateTime DATA_HORA_ATUAL = LocalDateTime.now();
	private final String DATA_HORA_ATUAL_FORMATADA = DATA_HORA_ATUAL.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

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
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1))
				.thenReturn(getLocalizacaoA());
		when(atendimentoRepositoryMock.listarPorPeriodoIhServico(LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59), Arrays.asList(ID_SERVICO_1)
		)).thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));

		getService().chamarProximo(ID_LOCALIZACAO_1);
		verify(getRepositoryMock()).save(any(Atendimento.class));
	}

	@Test(expected = NegocioException.class)
	public void aoChamarProximoDeveriaIhNaoExisteProximoDeveriaLancarExcecao() throws Exception {
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1))
				.thenReturn(getLocalizacaoA());
		when(atendimentoRepositoryMock.listarPorPeriodoIhServico(LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59), Arrays.asList(ID_SERVICO_1)
		)).thenReturn(null);
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());

		getService().chamarProximo(ID_LOCALIZACAO_1);
	} 

	@Test 
	public void aoChamarProximoDeveriaDelegarParaOhLocalizacaoServiceConsultarPorId() throws Exception {
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1))
				.thenReturn(getLocalizacaoA());
		when(atendimentoRepositoryMock.listarPorPeriodoIhServico(LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59), Arrays.asList(ID_SERVICO_1)
		)).thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());

		getService().chamarProximo(ID_LOCALIZACAO_1);
		verify(localizacaoServiceMock).consultarPorId(Mockito.anyString());
	} 

	@Test 
	public void aoChamarProximoDeveriaDelegarParaOhRepositorylistarPorPeriodoIhServico() throws Exception {
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1))
				.thenReturn(getLocalizacaoA());
		when(atendimentoRepositoryMock.listarPorPeriodoIhServico(LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59), Arrays.asList(ID_SERVICO_1)
		)).thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));
		getService().chamarProximo(ID_LOCALIZACAO_1);
		verify(atendimentoRepositoryMock).listarPorPeriodoIhServico(any(LocalDateTime.class),any(LocalDateTime.class), any(List.class));
	} 

	@Test
	public void aoConsultarMovimentacaoDoDiaDaLocalizacaoDeveriaDelegarParaOhRepositoryListarMovimentacaoPorLocalizacaoIhData() throws Exception {
		when(atendimentoRepositoryMock.listarPorPeriodoIhServico(LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59), Arrays.asList(ID_SERVICO_1)
		)).thenReturn(Arrays.asList(getAtendimentoNaoPrioridade()));
		getService().consultarMovimentacaoDoDiaDaLocalizacao(Arrays.asList(ID_SERVICO_1));
		verify(atendimentoRepositoryMock).listarPorPeriodoIhServico(any(LocalDateTime.class),any(LocalDateTime.class),  any(List.class));
	} 

	@Test
	public void aoChamarNovamenteDeveriaDelegarParaOhLocalizacaoServiceConsultarPorId() throws Exception {
		when(getRepositoryMock().findById(ID_ATENDIMENTO_1)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(ID_ATENDIMENTO_1, ID_LOCALIZACAO_1);
		verify(localizacaoServiceMock).consultarPorId(Mockito.anyString());
	}

	@Test
	public void aoChamarNovamenteDeveriaDelegarParaRepositorySave() throws Exception {
		when(getRepositoryMock().findById(ID_ATENDIMENTO_1)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(ID_ATENDIMENTO_1, ID_LOCALIZACAO_1);
		verify(getRepositoryMock()).save(getEntidade());
	}

	@Test
	public void aoChamarNovamenteDeveriaSetarLocalizacaoNoAtendimento() {
		when(getRepositoryMock().findById(ID_ATENDIMENTO_1)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(ID_ATENDIMENTO_1, ID_LOCALIZACAO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getLocalizacaoA(), atendimentoToSave.getLocalizacao());
	}

	@Test
	public void aoChamarNovamenteDeveriaSetarDataHoraDaChamadaNoAtendimento() {
		when(getRepositoryMock().findById(ID_ATENDIMENTO_1)).thenReturn(Optional.of(getEntidade()));
		when(localizacaoServiceMock.consultarPorId(ID_LOCALIZACAO_1)).thenReturn(getLocalizacaoA());
		getService().chamarNovamente(ID_ATENDIMENTO_1, ID_LOCALIZACAO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DATA_HORA_ATUAL_FORMATADA, atendimentoToSave.getDataHoraCadastroFormatada());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarPrioridadeComoFalse() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertTrue(atendimentoToSave.getIndicadorPrioridade());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarDataHoraDeCadastro() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DATA_HORA_ATUAL_FORMATADA, atendimentoToSave.getDataHoraCadastroFormatada());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getServicoComCorVermelho().getNumeroAtendimentoAtual(), atendimentoToSave.getNumeroAtendimento());
	}

	@Test
	public void aoGerarPrioridadeDeveriaSetarOhServicoConsultadoNoAtendimento() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(atendimentoToSave.getServico(), getServicoComCorVermelho());
	}

	@Test
	public void aoGerarPrioridadeDeveriaDelegarParaOhServicoServiceRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		verify(servicoServiceMock).retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(Mockito.anyString());
	}

	@Test
	public void aoGerarPrioridadeDeveriaDelegarParaOhSave() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerarPrioridade(ID_SERVICO_1);
		verify(getRepositoryMock()).save(getEntidade());
	}


	@Test
	public void aoGerarDeveriaSetarPrioridadeComoFalse() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertFalse(atendimentoToSave.getIndicadorPrioridade());
	}

	@Test
	public void aoGerarDeveriaSetarDataHoraDeCadastro() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(DATA_HORA_ATUAL_FORMATADA, atendimentoToSave.getDataHoraCadastroFormatada());
	}

	@Test
	public void aoGerarDeveriaSetarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(getServicoComCorVermelho().getNumeroAtendimentoAtual(), atendimentoToSave.getNumeroAtendimento());
	}

	@Test
	public void aoGerarDeveriaSetarOhServicoConsultadoNoAtendimento() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
		Atendimento atendimentoToSave = captureAhEntidadeAoSalvar();
		assertEquals(atendimentoToSave.getServico(), getServicoComCorVermelho());
	}

	@Test
	public void aoGerarDeveriaDelegarParaOhServicoServiceRetornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual() {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
		verify(servicoServiceMock).retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(Mockito.anyString());
	}

	@Test
	public void aoGerarDeveriaDelegarParaOhSave() throws Exception {
		when(servicoServiceMock.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(ID_SERVICO_1)).
				thenReturn(getServicoComCorVermelho());
		getService().gerar(ID_SERVICO_1);
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
		servico.setId(ID_SERVICO_1);
		servico.setTipoCor(TipoCor.VERMELHO.getValue());
		servico.setNumeroAtendimentoAtual(NUMERO_ATENDIMENTO_1);
		return servico;
	}

	private Localizacao getLocalizacaoA() {
		Localizacao localizacao = new Localizacao();
		localizacao.setId(ID_LOCALIZACAO_1);
		localizacao.setDescricao(DESCRICAO_LOCALIZACAO_A);
		localizacao.setServicos(new HashSet<>(Arrays.asList(getServicoComCorVermelho())));
		return localizacao;
	}

	private String getId() {
		return getEntidade().getId();
	}

	private Atendimento getEntidade() {
		return getAtendimentoNaoPrioridade();
	}

	private Atendimento getAtendimentoPrioridade() {
		Atendimento atendimento = new Atendimento();
		atendimento.setId(ID_ATENDIMENTO_2);
		atendimento.setIndicadorPrioridade(true);
		atendimento.setDataHoraCadastro(DATA_HORA_ATUAL);
		return atendimento;
	}

	private Atendimento getAtendimentoNaoPrioridade() {
		Atendimento atendimento = new Atendimento();
		atendimento.setIndicadorPrioridade(false);
		atendimento.setDataHoraCadastro(DATA_HORA_ATUAL);
		return atendimento;
	}

	private AtendimentoService getService() {
		return atendimentoService;
	}

	private MongoRepository<Atendimento, String> getRepositoryMock() {
		return atendimentoRepositoryMock;
	}
}
