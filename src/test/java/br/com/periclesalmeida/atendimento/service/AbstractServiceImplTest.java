package br.com.periclesalmeida.atendimento.service;

import br.com.periclesalmeida.atendimento.util.GenericService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public abstract class AbstractServiceImplTest<ENTIDADE, ID> {

    @Test
    public void aoIncluirDeveriaDelegarParaOhRespitorio() {
        getService().incluir(getEntidade());
        verify(getRepositoryMock()).save(getEntidade());
    }

    @Test
    public void aoAlterarDeveriaDelegarParaOhRespitorio() {
        getService().alterar(getEntidade());
        verify(getRepositoryMock()).save(getEntidade());
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
        ArgumentCaptor<Pageable> pageArgument = ArgumentCaptor.forClass(Pageable.class);
        when(getRepositoryMock().findAll(any(Example.class), any(Pageable.class))).thenReturn(any(Page.class));
        getService().consultarPassandoEntidade(getEntidade() ,  eq(PageRequest.of(2,10)));
        verify(getRepositoryMock()).findAll(any(Example.class), pageArgument.capture());
    }

    @Before
    public abstract void inicializarContexto();

    protected abstract ID getId();
    protected abstract ENTIDADE getEntidade();
    protected abstract GenericService<ENTIDADE, ID> getService();
    protected abstract JpaRepository<ENTIDADE, ID> getRepositoryMock();
}
