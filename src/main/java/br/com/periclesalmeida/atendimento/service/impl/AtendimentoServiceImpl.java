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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

	private final String MENSAGEM_NAO_EXISTE_ATENDIMENTO_NA_FILA = "Não existe atendimento na fila.";
	private final int QUANTIDADE_DE_ATENDIMENTO_REALIZADO_ATE_A_PROXIMA_PRIORIDADE = 2;
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
	public Atendimento gerarPrioridade(String sequencialServico) {
		Atendimento atendimento = criarAtendimento(sequencialServico);
		setarIndicadorPrioridadeComoTrue(atendimento);
		atendimentoRepository.save(atendimento);
		return atendimento;
	}

	@Override
	public Atendimento consultarPorId(String var1) {
		return atendimentoRepository.findById(var1).orElseThrow(() -> new EmptyResultDataAccessException(0));
	}

	@Override
	public AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDaLocalizacao(String sequencialLocalizacao) {
		List<Atendimento> atendimentos = listarAtendimentoDoDiaParaAhLocalizacao(sequencialLocalizacao);
		return new AtendimentoMovimentacaoDTO(atendimentos);
	}

	@Override
	public Atendimento chamarProximo(String sequencialLocalizacao) {
		List<Atendimento> atendimentos = listarAtendimentoDoDiaParaAhLocalizacao(sequencialLocalizacao);
		lancarExcecaoCasoNaoExistaProximo(atendimentos);
		Atendimento atendimentoChamado = retornarAtendimentoQueDeveSerChamado(atendimentos);
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(sequencialLocalizacao);
		setarDadosDoAtendimento(atendimentoChamado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoChamado);
		return atendimentoChamado;
	}

	@Override
	public Atendimento chamarNovamente(String sequencial, String sequencialLocalizacao) {
		Atendimento atendimentoConsultado = consultarPorId(sequencial);
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(sequencialLocalizacao);
		setarDadosDoAtendimento(atendimentoConsultado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoConsultado);
		return atendimentoConsultado;
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

	private boolean isAtenderAhPrioridade(Optional<Atendimento> atendimentoMaisAntigoEmEsperaPrioritario, Integer quantidadeDeAtendimentosRealizadosAteAhPrioridadeAtendida) {
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
		atendimento.setDataHoraChamada(new Date());
	}

	private List<Atendimento> listarAtendimentoDoDiaParaAhLocalizacao(String sequencialLocalizacao) {
		return atendimentoRepository.listarPorLocalizacaoIhData(sequencialLocalizacao,
				LocalDate.now());
	}

	private void setarIndicadorPrioridadeComoFalso(Atendimento atendimento) {
		atendimento.setIndicadorPrioridade(false);
	}

	private void setarIndicadorPrioridadeComoTrue(Atendimento atendimento) {
		atendimento.setIndicadorPrioridade(true);
	}

	private Atendimento criarAtendimento(String sequencialServico) {
		Servico servico = servicoService.retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(sequencialServico);

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
		atendimento.setDataHoraCadastro(new Date());
	}
}
