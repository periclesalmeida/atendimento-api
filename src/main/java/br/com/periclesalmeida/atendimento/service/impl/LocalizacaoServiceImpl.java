package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LocalizacaoServiceImpl extends AbstractService<Localizacao, Long> implements LocalizacaoService {

    private static final String MENSAGEM_JA_EXISTE_LOCALIZACAO_COM_A_DESCRICAO_E_O_TIPO_INFORMADOS = "Já existe localização com a descrição e o tipo informados.";
    private LocalizacaoRepository localizacaoRepository;

    public LocalizacaoServiceImpl(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    @Override
    protected JpaRepository<Localizacao, Long> getRepository() {
        return localizacaoRepository;
    }


    @Override
    protected void regrasNegocioSalvar(Localizacao localizacao) {
        lancarExecaoCasoExistaServicoComAhSiglaInformada(localizacao);
    }

    @Override
    protected void regrasNegocioCadastrar(Localizacao localizacao) {
        super.regrasNegocioCadastrar(localizacao);
        setarComoAtivo(localizacao);
    }

    private void setarComoAtivo(Localizacao localizacao) {
        localizacao.setAtivo(true);
    }

    private void lancarExecaoCasoExistaServicoComAhSiglaInformada(Localizacao localizacao) {
        Optional<Localizacao> localizacaoConsultadaOptional = localizacaoRepository.findByDescricaoContainsAndTipoAllIgnoreCase(localizacao.getDescricao(), localizacao.getTipo());
        localizacaoConsultadaOptional
                .ifPresent(localizacaoConsultado ->
                        lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(localizacaoConsultado, localizacao));
    }

    private void lancarExecaoCasoCodigoDoObjetoConsultadoEhDiferenteDoInformado(Localizacao objetoConsultado, Localizacao objetoInformado) {
        if (!objetoConsultado.getSequencial().equals(objetoInformado.getSequencial()) ){
            throw new NegocioException(MENSAGEM_JA_EXISTE_LOCALIZACAO_COM_A_DESCRICAO_E_O_TIPO_INFORMADOS);
        }
    }
}
