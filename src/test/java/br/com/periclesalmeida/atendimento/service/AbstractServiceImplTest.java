package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.util.GenericService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public abstract class AbstractServiceImplTest<ENTIDADE, ID> {

    private final int PAGE_2 = 2;
    private final int SIZE_10 = 10;

    @Test
    public void aoIncluirDeveriaDelegarParaOhRespitorio() {
        getService().incluir(getEntidade());
        verify(getRepositoryMock()).insert(getEntidade());
    }

    @Test
    public void aoAlterarDeveriaDelegarParaOhRespitorio() {
        when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getEntidade()));
        getService().alterar(getId(), getEntidade());
        verify(getRepositoryMock()).save(getEntidade());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void aoAlterarIhRetornouNuloNaConsultaPorIdDeveriaLancarExcecaoEmptyResultDataAccessException() {
        when(getRepositoryMock().findById(getId())).thenReturn(Optional.empty());
        getService().alterar(getId(), getEntidade());
    }

    @Test
    public void aoSalvarDeveriaDelegarParaOhRespitorio() {
        getService().salvar(getEntidade());
        verify(getRepositoryMock()).save(getEntidade());
    }

    @Test
    public void aoExcluirDeveriaDelegarParaOhRespitorio() {
        getService().excluir(getEntidade());
        verify(getRepositoryMock()).delete(getEntidade());
    }

    @Test
    public void aoConsultarTodosDeveriaDelegarParaOhRepositorio() {
        getService().consultarTodos();
        verify(getRepositoryMock()).findAll();
    }

    @Test
    public void aoConsultarPorIdDeveriaDelegarParaOhRepositorio() {
        when(getRepositoryMock().findById(getId())).thenReturn(Optional.of(getEntidade()));
        getService().consultarPorId(getId());
        verify(getRepositoryMock()).findById(getId());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void aoConsultarPorIdIhDelegarParaOhRepositorioIhRetornouNuloDeveriaLancarExcecaoEmptyResultDataAccessException() {
        when(getRepositoryMock().findById(getId())).thenReturn(Optional.empty());
        getService().consultarPorId(getId());
    }

    @Test
    public void aoConsultarPassandoEntidadeDeveriaDelegarParaOhRepositorio() {
        getService().consultarPassandoEntidade(getEntidade() ,  PageRequest.of(PAGE_2, SIZE_10));
        verify(getRepositoryMock()).findAll(any(Example.class), any(PageRequest.class));
    }

    @Before
    public abstract void inicializarContexto();

    protected abstract ID getId();
    protected abstract ENTIDADE getEntidade();
    protected abstract GenericService<ENTIDADE, ID> getService();
    protected abstract MongoRepository<ENTIDADE, ID> getRepositoryMock();
}
