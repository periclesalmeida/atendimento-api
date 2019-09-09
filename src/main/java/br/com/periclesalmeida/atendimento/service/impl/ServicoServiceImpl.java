package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.repository.ServicoRepository;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class ServicoServiceImpl extends AbstractService<Servico, Long> implements ServicoService {

    private final Integer NUMERO_ATENDIMENTO_ATUAL_ZERO_0 = 0;
    private final String MENSAGEM_JA_EXISTE_SERVICO_CADASTRADO_COM_A_SIGLA_INFORMADA = "Já existe serviço cadastrado com a sigla informada";
    private final String MENSAGEM_TIPO_COR_INFORMADO_INVALIDO = "Tipo cor informado inválido.";
    private ServicoRepository servicoRepository;

    public ServicoServiceImpl(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @Override
    protected MongoRepository<Servico, Long> getRepository() {
        return servicoRepository;
    }

    @Override
    public List<Servico> consultarTodos() {
        return servicoRepository.findAll(Sort.by("descricao"));
    }

    @Override
    public Page<Servico> consultarPassandoEntidade(Servico servico, Pageable pageable) {
        ExampleMatcher matcher = matching()
                .withMatcher("descricao", contains().ignoreCase())
                .withMatcher("sigla", contains().ignoreCase());
        return getRepository().findAll(of(servico, matcher), pageable);
    }

    @Override
    public Servico retornarServicoAtualizandoOhProximoNumeroDeAtendimentoAtual(Long sequencial) {
        Servico servicoConsultado = consultarPorId(sequencial);
        incrementarNumeroDeAtendimentoAtual(servicoConsultado);
        servicoRepository.save(servicoConsultado);
        return servicoConsultado;
    }

    @Override
    protected void regrasNegocioSalvar(Servico servico) {
        lancarExecaoCasoTipoCorInformadoNaoDisponivel(servico);
        lancarExecaoCasoExistaServicoComAhSiglaInformada(servico);
    }

    @Override
    protected void regrasNegocioIncluir(Servico entidade) {
        regrasNegocioSalvar(entidade);
        setarComoAtivo(entidade);
        setarNumeroAtendimentoAtualSeNaoInformado(entidade);
    }

    private void incrementarNumeroDeAtendimentoAtual(Servico servico) {
        servico.setNumeroAtendimentoAtual(servico.getNumeroAtendimentoAtual() + 1);
    }

    private void setarNumeroAtendimentoAtualSeNaoInformado(Servico entidade) {
        if (VerificadorUtil.estaNuloOuVazio(entidade.getNumeroAtendimentoAtual())) {
            entidade.setNumeroAtendimentoAtual(NUMERO_ATENDIMENTO_ATUAL_ZERO_0);
        }
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
