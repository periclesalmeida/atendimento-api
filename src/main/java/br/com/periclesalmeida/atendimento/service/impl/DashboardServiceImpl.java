package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;
import br.com.periclesalmeida.atendimento.domain.dto.DashboardDTO;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.AtendimentoService;
import br.com.periclesalmeida.atendimento.service.DashboardService;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private ServicoRepository servicoRepository;
    private LocalizacaoRepository localizacaoRepository;
    private AtendimentoService atendimentoService;

    public DashboardServiceImpl(ServicoRepository servicoRepository, LocalizacaoRepository localizacaoRepository, AtendimentoService atendimentoService) {
        this.servicoRepository = servicoRepository;
        this.localizacaoRepository = localizacaoRepository;
        this.atendimentoService = atendimentoService;
    }

    @Override
    public DashboardDTO carregar() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        setarQuantidadeLocalidades(dashboardDTO);
        List<String> codigosServicos = getIdsServicos();
        setarQuantidadeServicos(dashboardDTO, codigosServicos);
        setarQuantidadeEmEsperaIhAtendidoHoje(dashboardDTO, codigosServicos);
        setarQuantidadeAtendimentoNoMes(dashboardDTO, codigosServicos);
        setarQuantidadeAtendimentoNaSemana(dashboardDTO, codigosServicos);
        return dashboardDTO;
    }

    private void setarQuantidadeAtendimentoNaSemana(DashboardDTO dashboardDTO, List<String> codigosServicos) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoSemana = atendimentoService.consultarMovimentacaoDosServicosNoPeriodo(codigosServicos, Period.ofWeeks(1));
        dashboardDTO.setQuantidadeAtendimentoNaSemana(atendimentoMovimentacaoSemana.getAtendimentosRealizados().stream().count());
    }

    private void setarQuantidadeAtendimentoNoMes(DashboardDTO dashboardDTO, List<String> codigosServicos) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoMes = atendimentoService.consultarMovimentacaoDosServicosNoPeriodo(codigosServicos, Period.ofMonths(1));
        dashboardDTO.setQuantidadeAtendimentoNoMes(atendimentoMovimentacaoMes.getAtendimentosRealizados().stream().count());
    }

    private void setarQuantidadeServicos(DashboardDTO dashboardDTO, List<String> codigosServicos) {
        dashboardDTO.setQuantidadeServicos(codigosServicos.stream().count());
    }

    private List<String> getIdsServicos() {
        List<Servico> servicos = servicoRepository.findAll();
        return servicos.stream().map(Servico::getId).collect(Collectors.toList());
    }

    private void setarQuantidadeEmEsperaIhAtendidoHoje(DashboardDTO dashboardDTO, List<String> codigosServicos) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoDTO = atendimentoService.consultarMovimentacaoDoDiaDosServicos(codigosServicos);
        dashboardDTO.setQuantidadeAtendimentosHoje(atendimentoMovimentacaoDTO.getAtendimentosRealizados().stream().count());
        dashboardDTO.setQuantidadeAtendimentosemEsperaHoje(atendimentoMovimentacaoDTO.getAtendimentosEmEspera().stream().count());
    }

    private void setarQuantidadeLocalidades(DashboardDTO dashboardDTO) {
        dashboardDTO.setQuantidadeLocalidades(localizacaoRepository.count());
    }
}
