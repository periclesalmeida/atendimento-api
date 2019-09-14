package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
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
public class LocalizacaoServiceImpl extends AbstractService<Localizacao, String> implements LocalizacaoService {

    private static final String MENSAGEM_JA_EXISTE_LOCALIZACAO_COM_A_DESCRICAO_E_O_TIPO_INFORMADOS = "Já existe localização com a descrição e o tipo informados.";
    private LocalizacaoRepository localizacaoRepository;

    public LocalizacaoServiceImpl(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    @Override
    protected MongoRepository<Localizacao, String> getRepository() {
        return localizacaoRepository;
    }


    @Override
    public Page<Localizacao> consultarPassandoEntidade(Localizacao localizacao, Pageable pageable) {
        ExampleMatcher matcher = matching()
                .withMatcher("descricao", contains().ignoreCase());
        return getRepository().findAll(of(localizacao, matcher), pageable);
    }

    @Override
    public List<Localizacao> consultarTodos() {
        return this.getRepository().findAll(Sort.by("tipo.descricao","descricao"));
    }

    @Override
    protected void regrasNegocioSalvar(Localizacao localizacao) {
        lancarExecaoCasoExistaLocalizacaoCadastradaComAhDescricaoIhTipoInformado(localizacao);
    }

    @Override
    protected void regrasNegocioIncluir(Localizacao localizacao) {
        super.regrasNegocioIncluir(localizacao);
        setarComoAtivo(localizacao);
    }

    private void setarComoAtivo(Localizacao localizacao) {
        localizacao.setAtivo(true);
    }

    private void lancarExecaoCasoExistaLocalizacaoCadastradaComAhDescricaoIhTipoInformado(Localizacao localizacao) {
        Optional<Localizacao> localizacaoConsultadaOptional = localizacaoRepository.findByDescricaoContainsAndTipoAllIgnoreCase(localizacao.getDescricao(), localizacao.getTipo());
        localizacaoConsultadaOptional
                .ifPresent(localizacaoConsultado ->
                        lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(localizacaoConsultado, localizacao));
    }

    private void lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(Localizacao objetoConsultado, Localizacao objetoInformado) {
        if (!objetoConsultado.getId().equals(objetoInformado.getId()) ){
            throw new NegocioException(MENSAGEM_JA_EXISTE_LOCALIZACAO_COM_A_DESCRICAO_E_O_TIPO_INFORMADOS);
        }
    }
}
