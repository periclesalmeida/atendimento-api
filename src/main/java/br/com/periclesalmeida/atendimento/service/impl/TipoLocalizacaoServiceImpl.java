package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.TipoLocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.TipoLocalizacaoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
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
public class TipoLocalizacaoServiceImpl extends AbstractService<TipoLocalizacao, String> implements TipoLocalizacaoService {

    private final String MENSAGEM_OBJETO_JA_CADASTRADO = "Objeto já cadastrado.";
    private TipoLocalizacaoRepository tipoLocalizacaoRepository;

    public TipoLocalizacaoServiceImpl(TipoLocalizacaoRepository tipoLocalizacaoRepository) {
        this.tipoLocalizacaoRepository = tipoLocalizacaoRepository;
    }

    @Override
    protected MongoRepository<TipoLocalizacao, String> getRepository() {
        return tipoLocalizacaoRepository;
    }

    @Override
    public List<TipoLocalizacao> consultarTodos() {
        return tipoLocalizacaoRepository.findAll(Sort.by("descricao"));
    }

    @Override
    public Page<TipoLocalizacao> consultarPassandoEntidade(TipoLocalizacao tipoLocalizacao, Pageable pageable) {
        ExampleMatcher matcher = matching()
                .withMatcher("descricao", contains().ignoreCase());
        return getRepository().findAll(of(tipoLocalizacao, matcher), pageable);
    }

    @Override
    protected void regrasNegocioSalvar(TipoLocalizacao tipoLocalizacao) {
        lancarExecaoCasoExistaTipoLocalizacaoComAhDescricaoInformada(tipoLocalizacao);
    }

    private void lancarExecaoCasoExistaTipoLocalizacaoComAhDescricaoInformada(TipoLocalizacao tipoLocalizacao) {
        Optional<TipoLocalizacao> tipoLocalizacaoConsultadoOptional = tipoLocalizacaoRepository.findByDescricaoContainsAllIgnoreCase(tipoLocalizacao.getDescricao());
        tipoLocalizacaoConsultadoOptional
                .ifPresent(tipoLocalizacaoConsultado ->
                        lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(tipoLocalizacaoConsultado, tipoLocalizacao));
    }

    private void lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(TipoLocalizacao objetoConsultado, TipoLocalizacao objetoInformado) {
        if (!objetoConsultado.getSequencial().equals(objetoInformado.getSequencial())) {
            throw new NegocioException(MENSAGEM_OBJETO_JA_CADASTRADO);
        }
    }
}
