package br.com.periclesalmeida.atendimento.resource;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.PermissaoRepository;
import br.com.periclesalmeida.atendimento.service.UsuarioService;

@RestController
@RequestMapping(path = "/usuario")
public class UsuarioResource {

    private UsuarioService usuarioService;
    private PermissaoRepository permissaoRepository;

    public UsuarioResource(UsuarioService usuarioService, PermissaoRepository permissaoRepository) {
        this.usuarioService = usuarioService;
        this.permissaoRepository = permissaoRepository;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USUARIO_INCLUIR')")
    public ResponseEntity<Usuario> incluir(@Validated @RequestBody Usuario entidade) {
        usuarioService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidade);
    }

    @PutMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_USUARIO_ALTERAR')")
    public ResponseEntity<Usuario>  alterar(@PathVariable String sequencial, @Validated @RequestBody Usuario entidade) {
        usuarioService.alterar(sequencial, entidade);
        return ResponseEntity.status(HttpStatus.OK).body(entidade);
    }

    @GetMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_USUARIO_CONSULTAR')")
    public Usuario  consultarPorId(@PathVariable String sequencial) {
        return usuarioService.consultarPorId(sequencial);
    }

    @GetMapping(path = "/consulta")
    @PreAuthorize("hasAuthority('ROLE_USUARIO_CONSULTAR')")
    public Page<Usuario> consultarPorEntidade(Usuario entidade, Pageable pageable) {
        return usuarioService.consultarPassandoEntidade(entidade, pageable);
    }

    @PostMapping(path = "/permissao")
    @PreAuthorize("isAuthenticated()")
    public List<Permissao> getPermissoes() {
        return permissaoRepository.findAll(Sort.by("descricao"));
    }
}
