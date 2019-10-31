package br.com.periclesalmeida.atendimento.resource;


import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.service.TipoLocalizacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/tipo-localizacao")
public class TipoLocalizacaoResource {

    private TipoLocalizacaoService tipoLocalizacaoService;

    public TipoLocalizacaoResource(TipoLocalizacaoService tipoLocalizacaoService) {
        this.tipoLocalizacaoService = tipoLocalizacaoService;
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_INCLUIR')")
    public ResponseEntity<TipoLocalizacao>  incluir(@Validated @RequestBody TipoLocalizacao entidade) {
        tipoLocalizacaoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping("/{codigo}")
    //@PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_ALTERAR')")
    public ResponseEntity<TipoLocalizacao>  alterar(@PathVariable String codigo, @Validated @RequestBody TipoLocalizacao entidade) {
        tipoLocalizacaoService.alterar(codigo, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('ROLE_TIPO_LOCALIZACAO_CONSULTAR')")
    public Page<TipoLocalizacao> consultarPorEntidade(TipoLocalizacao entidade, Pageable pageable) {
        return tipoLocalizacaoService.consultarPassandoEntidade(entidade, pageable);
    }
}
