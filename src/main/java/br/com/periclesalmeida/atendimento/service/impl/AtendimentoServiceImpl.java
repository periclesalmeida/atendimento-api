package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;
import br.com.periclesalmeida.atendimento.repository.AtendimentoRepository;
import br.com.periclesalmeida.atendimento.service.AtendimentoService;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

	private final String MENSAGEM_NAO_EXISTE_ATENDIMENTO_NA_FILA = "Não existe atendimento na fila.";
	private final Integer QUANTIDADE_DE_ATENDIMENTO_REALIZADO_ATE_A_PROXIMA_PRIORIDADE = 2;
	private AtendimentoRepository atendimentoRepository;
	private ServicoService servicoService;
	private LocalizacaoService localizacaoService;

	public AtendimentoServiceImpl(AtendimentoRepository atendimentoRepository, ServicoService servicoService, LocalizacaoService localizacaoService) {
		this.atendimentoRepository = atendimentoRepository;
		this.servicoService = servicoService;
		this.localizacaoService = localizacaoService;
	}

	@Override
	public Atendimento gerar(String sequencialServico) {
		Atendimento atendimento = criarAtendimento(sequencialServico);
		setarIndicadorPrioridadeComoFalso(atendimento);
		atendimentoRepository.save(atendimento);
		return atendimento;
	}

	@Override
	public Atendimento gerarPrioridade(String id) {
		Atendimento atendimento = criarAtendimento(id);
		setarIndicadorPrioridadeComoTrue(atendimento);
		atendimentoRepository.save(atendimento);
		return atendimento;
	}

	@Override
	public Atendimento consultarPorId(String var1) {
		return atendimentoRepository.findById(var1).orElseThrow(() -> new EmptyResultDataAccessException(0));
	}

	@Override
	public AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDaLocalizacao(List<String> sequenciaisServico) {
		List<Atendimento> atendimentos = listarAtendimentoDoDiaParaOsServicos(sequenciaisServico);
		ordenarListaPorDataHoraCadastro(atendimentos);
		return new AtendimentoMovimentacaoDTO(atendimentos);
	}

	@Override
	public Atendimento chamarProximo(String idLocalizacao) {
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(idLocalizacao);
		List<String> idsServico = gerarListaStringComIdDosServicosDaLocalizacao(localizacaoConsultada);
		List<Atendimento> atendimentos = listarAtendimentoDoDiaParaOsServicos(idsServico);
		lancarExcecaoCasoNaoExistaProximo(atendimentos);
		Atendimento atendimentoChamado = retornarAtendimentoQueDeveSerChamado(atendimentos);
		setarDadosDoAtendimento(atendimentoChamado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoChamado);
		return atendimentoChamado;
	}

	@Override
	public Atendimento chamarNovamente(String id, String idLocalizacao) {
		Atendimento atendimentoConsultado = consultarPorId(id);
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(idLocalizacao);
		setarDadosDoAtendimento(atendimentoConsultado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoConsultado);
		return atendimentoConsultado;
	}

	private void ordenarListaPorDataHoraCadastro(List<Atendimento> atendimentos) {
		atendimentos.sort((t1, t2) -> {
			return t1.getDataHoraCadastro().isAfter(t2.getDataHoraCadastro()) ? 1 : -1;
		});
	}

	private List<String> gerarListaStringComIdDosServicosDaLocalizacao(Localizacao localizacaoConsultada) {
		return localizacaoConsultada.getServicos().stream().map(Servico::getId).collect(Collectors.toList());
	}

	private void lancarExcecaoCasoNaoExistaProximo(List<Atendimento> atendimentos) {
		if (VerificadorUtil.isListaNulaOuVazia(atendimentos)) throw new NegocioException(MENSAGEM_NAO_EXISTE_ATENDIMENTO_NA_FILA);
	}

	private Atendimento retornarAtendimentoQueDeveSerChamado(List<Atendimento> atendimentos) {
		Optional<Atendimento> atendimentoMaisAntigoEmEspera = retornarAtendimentoComMaisTempoEmEspera(atendimentos);
		Optional<Atendimento> atendimentoMaisAntigoEmEsperaPrioritario = retornarAtendimentoComMaisTempoEmEsperaPrioritario(atendimentos);
		Integer quantidadeDeAtendimentosRealizadosAteAhPrioridadeAtendida = retornarQuantidadeDeAtendimentoRealizadoAteAhUltimaPrioridadeChamada(atendimentos);

		return isAtenderAhPrioridade(atendimentoMaisAntigoEmEsperaPrioritario, quantidadeDeAtendimentosRealizadosAteAhPrioridadeAtendida) ?
				atendimentoMaisAntigoEmEsperaPrioritario.get() :
				atendimentoMaisAntigoEmEspera.get();
	}

	private Boolean isAtenderAhPrioridade(Optional<Atendimento> atendimentoMaisAntigoEmEsperaPrioritario, Integer quantidadeDeAtendimentosRealizadosAteAhPrioridadeAtendida) {
		return atendimentoMaisAntigoEmEsperaPrioritario.isPresent() &&
				quantidadeDeAtendimentosRealizadosAteAhPrioridadeAtendida == QUANTIDADE_DE_ATENDIMENTO_REALIZADO_ATE_A_PROXIMA_PRIORIDADE;
	}

	private Integer retornarQuantidadeDeAtendimentoRealizadoAteAhUltimaPrioridadeChamada(List<Atendimento> atendimentos) {
		Integer quantidadeDeAtendimentosRealizadosAteAhPrioridade = 0;
		for (Atendimento atendimento: atendimentos) {
			if (atendimento.isRealizado()) {
				quantidadeDeAtendimentosRealizadosAteAhPrioridade =+1;
				if (atendimento.getIndicadorPrioridade()) {
					break;
				}
			}
		}
		return quantidadeDeAtendimentosRealizadosAteAhPrioridade;
	}

	private Optional<Atendimento> retornarAtendimentoComMaisTempoEmEsperaPrioritario(List<Atendimento> atendimentos) {
		return atendimentos.stream()
				.filter(Atendimento::isEmEspera)
				.filter(Atendimento::getIndicadorPrioridade).findFirst();
	}

	private Optional<Atendimento> retornarAtendimentoComMaisTempoEmEspera(List<Atendimento> atendimentos) {
		return atendimentos.stream()
				.filter(Atendimento::isEmEspera).findFirst();
	}

	private void setarDadosDoAtendimento(Atendimento atendimento , Localizacao localizacao) {
		atendimento.setLocalizacao(localizacao);
		atendimento.setDataHoraChamada(LocalDateTime.now());
	}

	private List<Atendimento> listarAtendimentoDoDiaParaOsServicos(List<String> sequenciaisServico) {
		return atendimentoRepository.listarPorPeriodoIhServico(
				LocalDate.now().atStartOfDay(),
				LocalDate.now().atTime(23,59,59),
				sequenciaisServico
		);
	}

	private void setarIndicadorPrioridadeComoFalso(Atendimento atendimento) {
		atendimento.setIndicadorPrioridade(false);
	}

	private void setarIndicadorPrioridadeComoTrue(Atendimento atendimento) {
		atendimento.setIndicadorPrioridade(true);
	}

	private Atendimento criarAtendimento(String idServico) {
		Servico servico = servicoService.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(idServico);

		Atendimento atendimento = new Atendimento();
		atendimento.setServico(servico);
		setarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico(servico, atendimento);
		setarDataHoraDeCadastroComoDataDeHoje(atendimento);
		return atendimento;
	}

	private void setarNumeroDeAtendimentoComoNumeroDeAtendimentoAtualDoServico(Servico servico, Atendimento atendimento) {
		atendimento.setNumeroAtendimento(servico.getNumeroAtendimentoAtual());
	}

	private void setarDataHoraDeCadastroComoDataDeHoje(Atendimento atendimento) {
		atendimento.setDataHoraCadastro(LocalDateTime.now());
	}
}
