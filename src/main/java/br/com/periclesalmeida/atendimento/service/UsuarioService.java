package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.util.GenericService;

public interface UsuarioService extends GenericService<Usuario, Long> {

    Usuario efetuarLogin(String login, String senha);
}
