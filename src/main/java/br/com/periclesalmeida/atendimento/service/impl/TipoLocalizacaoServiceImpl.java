package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.repository.TipoLocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.TipoLocalizacaoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoLocalizacaoServiceImpl extends AbstractService<TipoLocalizacao, Integer> implements TipoLocalizacaoService {

    private static final String MENSAGEM_OBJETO_JA_CADASTRADO = "Objeto já cadastrado.";
    private TipoLocalizacaoRepository tipoLocalizacaoRepository;

    public TipoLocalizacaoServiceImpl(TipoLocalizacaoRepository tipoLocalizacaoRepository) {
        this.tipoLocalizacaoRepository = tipoLocalizacaoRepository;
    }

    @Override
    protected JpaRepository<TipoLocalizacao, Integer> getRepository() {
        return tipoLocalizacaoRepository;
    }

    @Override
    public List<TipoLocalizacao> consultarTodos() {
        return tipoLocalizacaoRepository.findAll(Sort.by("descricao"));
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
        if (!objetoConsultado.getCodigo().equals(objetoInformado.getCodigo())) {
            throw new NegocioException(MENSAGEM_OBJETO_JA_CADASTRADO);
        }
    }
}
