package br.com.periclesalmeida.atendimento.resource;


import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.service.TipoLocalizacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/tipoLocalizacao")
public class TipoLocalizacaoResource {

    private TipoLocalizacaoService tipoLocalizacaoService;

    public TipoLocalizacaoResource(TipoLocalizacaoService tipoLocalizacaoService) {
        this.tipoLocalizacaoService = tipoLocalizacaoService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_INCLUIR')")
    public ResponseEntity<TipoLocalizacao>  incluir(@Validated @RequestBody TipoLocalizacao entidade) {
        tipoLocalizacaoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_ALTERAR')")
    public ResponseEntity<TipoLocalizacao>  alterar(@Validated @RequestBody TipoLocalizacao entidade) {
        tipoLocalizacaoService.alterar(entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_CONSULTAR')")
    public List<TipoLocalizacao> consultarTodos() {
        return tipoLocalizacaoService.consultarTodos();
    }
}
