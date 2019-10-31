package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.service.LocalizacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/localizacao")
public class LocalizacaoResource {

    private LocalizacaoService localizacaoService;

    public LocalizacaoResource(LocalizacaoService localizacaoService) {
        this.localizacaoService = localizacaoService;
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_INCLUIR')")
    public ResponseEntity<Localizacao> incluir(@Validated @RequestBody Localizacao entidade) {
        localizacaoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping("/{sequencial}")
    //@PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_ALTERAR')")
    public ResponseEntity<Localizacao> alterar(@PathVariable String sequencial, @Validated @RequestBody Localizacao entidade) {
        localizacaoService.alterar(sequencial, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_LOCALIZACAO_CONSULTAR')")
    public Page<Localizacao> consultarPorEntidade(Localizacao entidade, Pageable pageable) {
        return localizacaoService.consultarPassandoEntidade(entidade, pageable);
    }
}

