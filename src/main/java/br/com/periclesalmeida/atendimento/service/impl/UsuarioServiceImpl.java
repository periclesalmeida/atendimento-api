package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.config.security.UsuarioSecurity;
import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.service.UsuarioService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class UsuarioServiceImpl extends AbstractService<Usuario, Long> implements UsuarioService {

    private final String MENSAGEM_USUARIO_NÃO_POSSUI_PERMISSÃO = "Usuário não possui permissão.";
    private final String MENSAGEM_JA_EXISTE_USUARIO_CADASTRADO_COM_O_LOGIN_INFORMADO = "Já existe usuário cadastrado com o LOGIN informado.";
    private final String MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO = "Usuário e/ou senha inválido";

    private UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Page<Usuario> consultarPassandoEntidade(Usuario usuario, Pageable pageable) {
        ExampleMatcher matcher = matching()
                .withMatcher("login", contains().ignoreCase());
        return getRepository().findAll(of(usuario, matcher), pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(username);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException(MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO));
        lancarExcecaoCasoUsuarioNaoPossuaPermissao(usuario.getPermissoes());
        return new UsuarioSecurity(usuario, criarGrantedAuthority(usuario));
    }

    @Override
    protected JpaRepository<Usuario, Long> getRepository() {
        return usuarioRepository;
    }

    @Override
    protected void regrasNegocioSalvar(Usuario usuario) {
        lancarExececaoCasoJaExistaUsuarioCadastradoComOhLoginIformado(usuario);
    }

    private void lancarExcecaoCasoUsuarioNaoPossuaPermissao(Set<Permissao> permissoes) {
        Optional.ofNullable(permissoes).orElseThrow(() -> new NegocioException(MENSAGEM_USUARIO_NÃO_POSSUI_PERMISSÃO));
    }

    private void lancarExececaoCasoJaExistaUsuarioCadastradoComOhLoginIformado(Usuario usuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByLogin(usuario.getLogin());
        optionalUsuario
                .ifPresent(usuarioConsultado ->
                        lancarExecaoCasoSequencialDoObjetoConsultadoEhDiferenteDoInformado(usuarioConsultado, usuario));
    }

    private void lancarExecaoCasoSequencialDoObjetoConsultadoEhDiferenteDoInformado(Usuario usuarioConsultado, Usuario usuario) {
        if (!usuarioConsultado.getSequencial().equals(usuario.getSequencial()) ){
            throw new NegocioException(MENSAGEM_JA_EXISTE_USUARIO_CADASTRADO_COM_O_LOGIN_INFORMADO);
        }
    }

    private Collection<? extends GrantedAuthority> criarGrantedAuthority(Usuario usuario) {
        return usuario.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getCodigo().toUpperCase()))
                .collect(Collectors.toList());
    }
}
