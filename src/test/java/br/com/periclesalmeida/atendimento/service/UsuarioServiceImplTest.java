package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.service.impl.UsuarioServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioServiceImplTest extends  AbstractServiceImplTest<Usuario, Long> {

    public static final String SENHA_ALEATORIA = "SENHA_123";
    private final String CODIDO_PERMISSAO = "COD_PERMISSAO";
    private final long SEQUENCIAL_USUARIO_1 = 1L;
    private final long SEQUENCIAL_USUARIO_2 = 2L;
    private final long SEQUENCIAL_USUARIO_3 = 3L;
    private final String LOGIN_ADMIN = "ADMIN";
    private final String LOGIN_TEST = "TEST";

    @Mock
    private UsuarioRepository usuarioRepositoryMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    private UsuarioService usuarioService;

    @Override
    public void inicializarContexto() {
        this.usuarioService = new UsuarioServiceImpl(usuarioRepositoryMock, passwordEncoderMock);
    }

    @Test
    public void aoLoadUserByUsernameDeveriaDelegarParaOhRepositorioFindByLogin() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.of(getUsuarioLoginAdmin()));
        usuarioService.loadUserByUsername(anyString());
        verify(usuarioRepositoryMock).findByLogin(anyString());
    }

    @Test(expected = NegocioException.class)
    public void aoLoadUserByUsernameIhUsuarioNaoPossuiPermissaoDeveriaLancarNegocioException() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.of(getUsuarioLoginTestIhSequencial2()));
        usuarioService.loadUserByUsername(anyString());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void aoLoadUserByUsernameIhUsuarioNaoExisteDeveriaLancarExcecaoUsernameNotFoundException() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.empty());
        usuarioService.loadUserByUsername(anyString());
    }

    @Test
    public void aoSalvarDeveriaDelegarParaOhRepositorioFindByLogin() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.of(getUsuarioLoginAdmin()));
        getService().salvar(getUsuarioLoginAdmin());
        verify(usuarioRepositoryMock).findByLogin(anyString());
    }

    @Test(expected = NegocioException.class)
    public void aoSalvarEntidadeQueJaExisteDeveriaLancarExcecaoComNegocioException() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.of(getUsuarioLoginTestIhSequencial2()));
        getService().salvar(getUsuarioLoginTestIhSequencial3());
    }

    @Test(expected = NegocioException.class)
    public void aoIncluirEntidadeSemInformarSenhComRashDeveriaLancarExcecaoNegocioException() {
        when(usuarioRepositoryMock.findByLogin(anyString())).thenReturn(Optional.of(getUsuarioLoginTestIhSequencial2()));
        getService().incluir(getUsuarioLoginTestIhSequencial2());
    }

    @Override
    protected Long getId() {
        return getEntidade().getSequencial();
    }

    @Override
    protected Usuario getEntidade() {
        return getUsuarioLoginAdmin();
    }

    @Override
    protected GenericService<Usuario, Long> getService() {
        return usuarioService;
    }

    @Override
    protected JpaRepository<Usuario, Long> getRepositoryMock() {
        return usuarioRepositoryMock;
    }

    private Usuario getUsuarioLoginAdmin() {
        Usuario usuario = new Usuario();
        usuario.setSequencial(SEQUENCIAL_USUARIO_1);
        usuario.setLogin(LOGIN_ADMIN);
        usuario.setSenhaSemRash(SENHA_ALEATORIA);
        usuario.setPermissoes(getPermissoes());
        return usuario;
    }

    private HashSet<Permissao> getPermissoes() {
        Permissao permissao = new Permissao();
        permissao.setCodigo(CODIDO_PERMISSAO);

        HashSet<Permissao> permissoes = new HashSet<>();
        permissoes.add(permissao);
        return permissoes;
    }

    private Usuario getUsuarioLoginTestIhSequencial2() {
        Usuario usuario = new Usuario();
        usuario.setLogin(LOGIN_TEST);
        usuario.setSequencial(SEQUENCIAL_USUARIO_2);
        return usuario;
    }

    private Usuario getUsuarioLoginTestIhSequencial3() {
        Usuario usuario = new Usuario();
        usuario.setLogin(LOGIN_TEST);
        usuario.setSequencial(SEQUENCIAL_USUARIO_3);
        return usuario;
    }
}
