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
@RequestMapping(path = "/tipo-localizacao")
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

    @PutMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_ALTERAR')")
    public ResponseEntity<TipoLocalizacao>  alterar(@PathVariable Integer codigo, @Validated @RequestBody TipoLocalizacao entidade) {
        tipoLocalizacaoService.alterar(codigo, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping("/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_CONSULTAR')")
    public TipoLocalizacao  consultarPorId(@PathVariable Integer codigo) {
        return tipoLocalizacaoService.consultarPorId(codigo);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_CONSULTAR')")
    public List<TipoLocalizacao> consultarTodos() {
        return tipoLocalizacaoService.consultarTodos();
    }
}
