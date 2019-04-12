package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicoServiceImpl extends AbstractService<Servico, Long> implements ServicoService {

    private static final String MENSAGEM_JA_EXISTE_SERVICO_CADASTRADO_COM_A_SIGLA_INFORMADA = "Já existe serviço cadastrado com a sigla informada";
    private static final String MENSAGEM_TIPO_COR_INFORMADO_INVALIDO = "Tipo cor informado inválido.";
    private ServicoRepository servicoRepository;

    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    protected JpaRepository<Servico, Long> getRepository() {
        return servicoRepository;
    }

    @Override
    public List<Servico> consultarTodos() {
        return servicoRepository.findAll(Sort.by("descricao"));
    }

    @Override
    protected void regrasNegocioSalvar(Servico servico) {
        lancarExecaoCasoTipoCorInformadoNaoDisponivel(servico);
        lancarExecaoCasoExistaServicoComAhSiglaInformada(servico);
    }

    @Override
    protected void regrasNegocioCadastrar(Servico servico) {
        super.regrasNegocioCadastrar(servico);
        setarComoAtivo(servico);
    }

    private void setarComoAtivo(Servico servico) {
        servico.setAtivo(true);
    }

    private void lancarExecaoCasoTipoCorInformadoNaoDisponivel(Servico servico) {
        Optional.ofNullable(TipoCor.parse(servico.getTipoCor())).orElseThrow(() -> new NegocioException(MENSAGEM_TIPO_COR_INFORMADO_INVALIDO));
    }

    private void lancarExecaoCasoExistaServicoComAhSiglaInformada(Servico servico) {
        Optional<Servico> servicoConsultadoOptional = servicoRepository.findBySiglaContainsAllIgnoreCase(servico.getSigla());
        servicoConsultadoOptional
                .ifPresent(sericoConsultado ->
                        lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(sericoConsultado, servico));
    }

    private void lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(Servico objetoConsultado, Servico objetoInformado) {
        if (!objetoConsultado.getSequencial().equals(objetoInformado.getSequencial()) ){
            throw new NegocioException(MENSAGEM_JA_EXISTE_SERVICO_CADASTRADO_COM_A_SIGLA_INFORMADA);
        }
    }
}
