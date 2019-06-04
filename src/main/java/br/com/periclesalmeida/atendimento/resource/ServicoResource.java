package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.service.ServicoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PutMapping("/sequencial")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_ALTERAR')")
    public ResponseEntity<Servico>  alterar(@PathVariable Long sequencial, @Validated @RequestBody Servico entidade) {
        servicoService.alterar(sequencial, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SERVICO_CONSULTAR')")
    public List<Servico> consultarTodos() {
        return servicoService.consultarTodos();
    }

    @GetMapping(path = "/tipoCor")
    @PreAuthorize("isAuthenticated()")
    public Map<String, String> consultarTipoCor() {
        return TipoCor.getMapValues();
    }
}
