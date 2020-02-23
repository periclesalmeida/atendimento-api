package br.com.periclesalmeida.atendimento.config.security;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final String MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO = "Usuário e/ou senha inválido";
    private final String MENSAGEM_USUARIO_NÃO_POSSUI_PERMISSÃO = "Usuário não possui permissão.";
    private UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(username);
        Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException(MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO));
        lancarExcecaoCasoUsuarioNaoPossuaPermissao(usuario.getPermissoes());
        return new UsuarioSecurity(usuario, criarGrantedAuthority(usuario));
    }

    private void lancarExcecaoCasoUsuarioNaoPossuaPermissao(Set<Permissao> permissoes) {
        Optional.ofNullable(permissoes).orElseThrow(() -> new NegocioException(MENSAGEM_USUARIO_NÃO_POSSUI_PERMISSÃO));
    }

    private Collection<? extends GrantedAuthority> criarGrantedAuthority(Usuario usuario) {
        return usuario.getPermissoes().stream()
                .map(permissao -> new SimpleGrantedAuthority(permissao.getId().toUpperCase()))
                .collect(Collectors.toList());
    }
}
