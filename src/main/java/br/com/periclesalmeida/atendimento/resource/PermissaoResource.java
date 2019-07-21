package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.repository.PermissaoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/permissao")
public class PermissaoResource {

    private PermissaoRepository permissaoRepository;

    public PermissaoResource(PermissaoRepository permissaoRepository) {
        this.permissaoRepository = permissaoRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PERMISSAO_CONSULTAR')")
    public List<Permissao> getPermissoes() {
        return permissaoRepository.findAll(Sort.by("descricao"));
    }
}
