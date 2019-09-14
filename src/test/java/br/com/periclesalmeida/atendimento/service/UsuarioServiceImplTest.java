package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.repository.UsuarioRepository;
import br.com.periclesalmeida.atendimento.service.impl.UsuarioServiceImpl;
import br.com.periclesalmeida.atendimento.util.GenericService;
import br.com.periclesalmeida.atendimento.util.exception.NegocioException;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioServiceImplTest extends  AbstractServiceImplTest<Usuario, String> {

    private final String SENHA_COM_RASH = "SENHA_COM_RASH";
    private final String SENHA_ALEATORIA = "SENHA_123";
    private final String CODIDO_PERMISSAO = "COD_PERMISSAO";
    private final String SEQUENCIAL_USUARIO_1 = "1L";
    private final String SEQUENCIAL_USUARIO_2 = "2L";
    private final String SEQUENCIAL_USUARIO_3 = "3L";
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

    @Test
    public void aoSalvarSeFoiInformadaSenhaSemRashDeveriaSetarAhSenhaComRash() {
        when(passwordEncoderMock.encode(anyString())).thenReturn(SENHA_COM_RASH);
        getService().salvar(getUsuarioLoginAdmin());
        Usuario usuarioToSave = captureAhEntidadeAoSalvar();
        assertEquals(SENHA_COM_RASH, usuarioToSave.getSenha());
    }

    @Test
    public void aoSalvarSeNaoFoiInformadaSenhaSemRashDeveriaFazerNada() {
        when(passwordEncoderMock.encode(anyString())).thenReturn(SENHA_COM_RASH);
        getService().salvar(getUsuarioLoginTestIhSequencial2());
        Usuario usuarioToSave = captureAhEntidadeAoSalvar();
        assertEquals(null, usuarioToSave.getSenha());
    }

    @Override
    protected String getId() {
        return getEntidade().getId();
    }

    @Override
    protected Usuario getEntidade() {
        return getUsuarioLoginAdmin();
    }

    @Override
    protected GenericService<Usuario, String> getService() {
        return usuarioService;
    }

    @Override
    protected MongoRepository<Usuario, String> getRepositoryMock() {
        return usuarioRepositoryMock;
    }

    private Usuario captureAhEntidadeAoSalvar() {
        ArgumentCaptor<Usuario> usuarioArgument = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepositoryMock).save(usuarioArgument.capture());
        return usuarioArgument.getValue();
    }

    private Usuario getUsuarioLoginAdmin() {
        Usuario usuario = new Usuario();
        usuario.setId(SEQUENCIAL_USUARIO_1);
        usuario.setLogin(LOGIN_ADMIN);
        usuario.setSenhaSemRash(SENHA_ALEATORIA);
        usuario.setPermissoes(getPermissoes());
        return usuario;
    }

    private HashSet<Permissao> getPermissoes() {
        Permissao permissao = new Permissao();
        permissao.setId(CODIDO_PERMISSAO);

        HashSet<Permissao> permissoes = new HashSet<>();
        permissoes.add(permissao);
        return permissoes;
    }

    private Usuario getUsuarioLoginTestIhSequencial2() {
        Usuario usuario = new Usuario();
        usuario.setLogin(LOGIN_TEST);
        usuario.setId(SEQUENCIAL_USUARIO_2);
        return usuario;
    }

    private Usuario getUsuarioLoginTestIhSequencial3() {
        Usuario usuario = new Usuario();
        usuario.setLogin(LOGIN_TEST);
        usuario.setId(SEQUENCIAL_USUARIO_3);
        return usuario;
    }
}
