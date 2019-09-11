package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.util.GenericService;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsuarioService extends GenericService<Usuario, String>, UserDetailsService {

}
