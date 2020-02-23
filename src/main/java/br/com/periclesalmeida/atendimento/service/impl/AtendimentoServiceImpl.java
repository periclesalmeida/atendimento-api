package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoChamadoDTO;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;
import br.com.periclesalmeida.atendimento.repository.AtendimentoRepository;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.service.AtendimentoService;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import br.com.periclesalmeida.atendimento.util.DateUtil;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AtendimentoServiceImpl implements AtendimentoService {

	private final String MENSAGEM_NAO_EXISTE_ATENDIMENTO_NA_FILA = "Não existe atendimento na fila";
	private final Integer QUANTIDADE_DE_ATENDIMENTO_REALIZADO_ATE_A_PROXIMA_PRIORIDADE = 2;
	private AtendimentoRepository atendimentoRepository;
	private ServicoService servicoService;
	private LocalizacaoService localizacaoService;
	private UsuarioRepository usuarioRepository;

	public AtendimentoServiceImpl(AtendimentoRepository atendimentoRepository, ServicoService servicoService, LocalizacaoService localizacaoService, UsuarioRepository usuarioRepository) {
		this.atendimentoRepository = atendimentoRepository;
		this.servicoService = servicoService;
		this.localizacaoService = localizacaoService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Atendimento apresentar(String id) {
		Atendimento atendimentoConsultado = consultarPorId(id);
		atendimentoConsultado.setDataHoraApresentacao(DateUtil.getLocalDateTimeNow());
		atendimentoRepository.save(atendimentoConsultado);
		return atendimentoConsultado;
	}

	@Override
	public Atendimento gerar(String idServico) {
		Atendimento atendimento = criarAtendimento(idServico);
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
	public AtendimentoMovimentacaoDTO consultarMovimentacaoDoDiaDosServicos(List<String> sequenciaisServico) {
		List<Atendimento> atendimentos = listarAtendimentoDoDiaParaOsServicos(sequenciaisServico);
		ordenarPorDataHoraCadastro(atendimentos);
		return new AtendimentoMovimentacaoDTO(atendimentos);
	}

	@Override
	public AtendimentoMovimentacaoChamadoDTO consultarMovimentacaoChamadaDoDiaDosServicos(List<String> idsServico) {
		List<Atendimento> atendimentos = listarAtendimentoChamadoNoDiaParaOsServicos(idsServico);
		ordenarPorDataHoraChamada(atendimentos);
		return new AtendimentoMovimentacaoChamadoDTO(atendimentos);
	}

	@Override
	public AtendimentoMovimentacaoDTO consultarMovimentacaoDosServicosNoPeriodo(List<String> idsServico, Period period) {
		List<Atendimento> atendimentos = listarAtendimentoChamadoParaOsServicosNoPeriodo(idsServico, period);
		ordenarPorDataHoraChamada(atendimentos);
		return new AtendimentoMovimentacaoDTO(atendimentos);
	}

	@Override
	public Atendimento chamarProximo(String idLocalizacao) {
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(idLocalizacao);
		List<String> idsServico = gerarListaStringComIdDosServicosDaLocalizacao(localizacaoConsultada);
		AtendimentoMovimentacaoDTO atendimentoMovimentacaoDTO = consultarMovimentacaoDoDiaDosServicos(idsServico);
		lancarExcecaoCasoNaoExistaProximo(atendimentoMovimentacaoDTO.getAtendimentosEmEspera());
		Atendimento atendimentoChamado = retornarAtendimentoQueDeveSerChamado(atendimentoMovimentacaoDTO.getAtendimentosEmEspera());
		setarInformacoesDaChamada(atendimentoChamado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoChamado);
		return atendimentoChamado;
	}

	@Override
	public Atendimento chamarNovamente(String id, String idLocalizacao) {
		Atendimento atendimentoConsultado = consultarPorId(id);
		Localizacao localizacaoConsultada = localizacaoService.consultarPorId(idLocalizacao);
		setarInformacoesDaChamada(atendimentoConsultado, localizacaoConsultada);
		atendimentoRepository.save(atendimentoConsultado);
		return atendimentoConsultado;
	}

	private void ordenarPorDataHoraChamada(List<Atendimento> atendimentos) {
		atendimentos.sort((t1, t2) -> {
			return t1.getDataHoraChamada().isAfter(t2.getDataHoraChamada()) ? 1 : -1;
		});
	}

	private List listarAtendimentoChamadoNoDiaParaOsServicos(List<String> idsServico) {
		return atendimentoRepository.listarPorPeriodoDeChamadaIhServicos(
				DateUtil.getLocalDateNow().atStartOfDay(),
				DateUtil.getLocalDateNow().atTime(23,59,59),
				idsServico
		);
	}

	private void ordenarPorDataHoraCadastro(List<Atendimento> atendimentos) {
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

	private List listarAtendimentoChamadoParaOsServicosNoPeriodo(List<String> idsServico, Period period) {
		return atendimentoRepository.listarPorPeriodoDeChamadaIhServicos(
				DateUtil.getLocalDateNow().atStartOfDay().minus(period),
				DateUtil.getLocalDateNow().atTime(23, 59,59),
				idsServico
		);
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

	private void setarInformacoesDaChamada(Atendimento atendimento , Localizacao localizacao) {
		atendimento.setLocalizacao(localizacao);
		setarDataHoraChamadaComDataHoraAtual(atendimento);
		setarDataHoraApresentacaoComoNulo(atendimento);
		setarUsuarioConectado(atendimento);
	}

	private void setarDataHoraChamadaComDataHoraAtual(Atendimento atendimento) {
		atendimento.setDataHoraChamada(DateUtil.getLocalDateTimeNow());
	}

	private void setarDataHoraApresentacaoComoNulo(Atendimento atendimento) {
		atendimento.setDataHoraApresentacao(null);
	}

	private void setarUsuarioConectado(Atendimento atendimento) {
		Optional<Usuario> usuario = usuarioRepository.findByLogin(getLoginUsuarioConectado());
		atendimento.setUsuario(usuario.isPresent() ? usuario.get() : null);
	}

	private String getLoginUsuarioConectado() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return Optional.ofNullable(authentication).isPresent() ?
						(String) authentication.getPrincipal() :
						null;
	}

	private List<Atendimento> listarAtendimentoDoDiaParaOsServicos(List<String> sequenciaisServico) {
		return atendimentoRepository.listarPorPeriodoDeCadastroIhServicos(
				DateUtil.getLocalDateNow().atStartOfDay(),
				DateUtil.getLocalDateNow().atTime(23,59,59),
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
		atendimento.setDataHoraCadastro(DateUtil.getLocalDateTimeNow());
	}
}
