package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.repository.LocalizacaoRepository;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class LocalizacaoServiceImpl extends AbstractService<Localizacao, Long> implements LocalizacaoService {

    private LocalizacaoRepository localizacaoRepository;

    public LocalizacaoServiceImpl(LocalizacaoRepository localizacaoRepository) {
        this.localizacaoRepository = localizacaoRepository;
    }

    @Override
    protected JpaRepository<Localizacao, Long> getRepository() {
        return localizacaoRepository;
    }
}
