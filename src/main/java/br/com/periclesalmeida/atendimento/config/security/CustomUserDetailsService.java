package br.com.periclesalmeida.atendimento.config.security;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final String MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO = "Usuário e/ou senha inválido";

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(login);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException(MENSAGEM_USUARIO_E_OU_SENHA_INVALIDO));
		return new UsuarioSecurity(usuario, criarGrantedAuthority(usuario));
	}

	private Collection<? extends GrantedAuthority> criarGrantedAuthority(Usuario usuario) {
		return usuario.getPermissoes().stream()
				.map(permissao -> new SimpleGrantedAuthority(permissao.getCodigo().toUpperCase()))
				.collect(Collectors.toList());
	}

}
