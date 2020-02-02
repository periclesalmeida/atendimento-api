package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/servico")
public class ServicoResource {

    private ServicoService servicoService;

    public ServicoResource(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Servico> incluir(@Validated @RequestBody Servico entidade) {
        servicoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_ALTERAR')")
    public ResponseEntity<Servico>  alterar(@PathVariable String sequencial, @Validated @RequestBody Servico entidade) {
        servicoService.alterar(sequencial, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SERVICO_CONSULTAR')")
    public Page<Servico> consultarPorEntidade(Servico entidade, Pageable pageable) {
        return servicoService.consultarPassandoEntidade(entidade, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_CONSULTAR')")
    public Servico consultarPorId(@PathVariable String id) {
        return servicoService.consultarPorId(id);
    }

    @GetMapping(path = "/tipo-cor")
    @PreAuthorize("isAuthenticated()")
    public TipoCor[] consultarTipoCor() {
        return TipoCor.values();
    }
}
