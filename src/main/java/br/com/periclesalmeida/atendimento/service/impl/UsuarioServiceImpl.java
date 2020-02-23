package br.com.periclesalmeida.atendimento.service.impl;

import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.service.UsuarioService;
import br.com.periclesalmeida.atendimento.util.AbstractService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.domain.Example.of;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.matching;

@Service
public class UsuarioServiceImpl extends AbstractService<Usuario, String> implements UsuarioService {

    private final String MENSAGEM_OBRIGATORIO_INFORMAR_A_SENHA = "Obrigatório informar a senha";
    private final String MENSAGEM_JA_EXISTE_USUARIO_CADASTRADO_COM_O_LOGIN_INFORMADO = "Já existe usuário cadastrado com o LOGIN informado.";

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<Usuario> consultarPassandoEntidade(Usuario usuario, Pageable pageable) {
        ExampleMatcher matcher = matching()
                .withMatcher("login", contains().ignoreCase());
        return getRepository().findAll(of(usuario, matcher), pageable);
    }

    @Override
    public Usuario consultarPorLogin(String login) {
        return  usuarioRepository.findByLogin(login).orElse(null);
    }

    @Override
    protected MongoRepository<Usuario, String> getRepository() {
        return usuarioRepository;
    }

    @Override
    protected void regrasNegocioSalvar(Usuario usuario) {
        setarSenhaSeSenhaSemRashInformada(usuario);
        lancarExececaoCasoJaExistaUsuarioCadastradoComOhLoginIformado(usuario);
    }

    @Override
    protected void regrasNegocioIncluir(Usuario usuario) {
        lancarExcecaoCasoSenhaSemRashNaoInformada(usuario);
        super.regrasNegocioIncluir(usuario);
    }

    private void setarSenhaSeSenhaSemRashInformada(Usuario usuario) {
        Optional.ofNullable(usuario.getSenhaSemRash()).ifPresent(s -> {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenhaSemRash()));
        });
    }

    private void lancarExcecaoCasoSenhaSemRashNaoInformada(Usuario usuario) {
        Optional.ofNullable(usuario.getSenhaSemRash()).orElseThrow(() -> new NegocioException(MENSAGEM_OBRIGATORIO_INFORMAR_A_SENHA));
    }

    private void lancarExececaoCasoJaExistaUsuarioCadastradoComOhLoginIformado(Usuario usuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findByLogin(usuario.getLogin());
        optionalUsuario
                .ifPresent(usuarioConsultado ->
                        lancarExecaoCasoSequencialDoObjetoConsultadoEhDiferenteDoInformado(usuarioConsultado, usuario));
    }

    private void lancarExecaoCasoSequencialDoObjetoConsultadoEhDiferenteDoInformado(Usuario usuarioConsultado, Usuario usuario) {
        if (!usuarioConsultado.getId().equals(usuario.getId()) ){
            throw new NegocioException(MENSAGEM_JA_EXISTE_USUARIO_CADASTRADO_COM_O_LOGIN_INFORMADO);
        }
    }
}
