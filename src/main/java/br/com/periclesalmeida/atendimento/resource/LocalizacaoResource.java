package br.com.periclesalmeida.atendimento.resource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;

@RestController
@RequestMapping(path = "/localizacao")
public class LocalizacaoResource {

    private LocalizacaoService localizacaoService;

    public LocalizacaoResource(LocalizacaoService localizacaoService) {
        this.localizacaoService = localizacaoService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_INCLUIR')")
    public ResponseEntity<Localizacao> incluir(@Validated @RequestBody Localizacao entidade) {
        localizacaoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_ALTERAR')")
    public ResponseEntity<Localizacao> alterar(@PathVariable String sequencial, @Validated @RequestBody Localizacao entidade) {
        localizacaoService.alterar(sequencial, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping(path = "/consulta")
    @PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_CONSULTAR')")
    public Page<Localizacao> consultarPorEntidade(Localizacao entidade, Pageable pageable) {
        return localizacaoService.consultarPassandoEntidade(entidade, pageable);
    }

    @GetMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_CONSULTAR')")
    public Localizacao consultarPorId(@PathVariable String sequencial) {
        return localizacaoService.consultarPorId(sequencial);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_CONSULTAR')")
    public List<Localizacao> consultarTodos() {
        return localizacaoService.consultarTodos();
    }
}

