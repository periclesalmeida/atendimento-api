package br.com.periclesalmeida.atendimento.config.security;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioSecurity extends User {

	private static final long serialVersionUID = 1L;
	private Usuario usuario;

	public UsuarioSecurity(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getLogin(), "{MD5}"+ usuario.getSenha(), authorities);
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

}
