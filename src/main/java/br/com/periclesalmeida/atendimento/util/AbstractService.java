package br.com.periclesalmeida.atendimento.util;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

import static org.springframework.data.domain.Example.of;

public abstract class AbstractService<ENTIDADE, ID extends Serializable> implements GenericService<ENTIDADE, ID> {

    protected abstract JpaRepository<ENTIDADE, ID> getRepository();

    @Override
    public ENTIDADE incluir(ENTIDADE entidade) {
        this.regrasNegocioCadastrar(entidade);
        this.getRepository().save(entidade);
        return entidade;
    }

    @Override
    public ENTIDADE alterar(ENTIDADE entidade) {
        this.regrasNegocioAlterar(entidade);
        this.getRepository().save(entidade);
        return entidade;
    }


    public ENTIDADE salvar(ENTIDADE entidade) {
        this.regrasNegocioSalvar(entidade);
        this.getRepository().save(entidade);
        return entidade;
    }

    public void excluir(ENTIDADE entidade) {
        this.regrasNegocioExcluir(entidade);
        this.getRepository().delete(entidade);
    }

    public ENTIDADE consultarPorId(ID var1) {
        return this.getRepository().findById(var1).orElseThrow(() -> new EmptyResultDataAccessException(0));
    }

    public List<ENTIDADE> consultarTodos() {
        return this.getRepository().findAll();
    }

    public Integer obterQuantidadeDeRegistros(ENTIDADE entidade) {
        Long quantidade = this.getRepository().count(of(entidade));
        return quantidade.intValue();
    }

    public Page<ENTIDADE> consultarPassandoEntidade(ENTIDADE entidade, Pageable pageable) {
        return getRepository().findAll(of(entidade), pageable);
    }

    protected void regrasNegocioSalvar(ENTIDADE entidade) {
    }

    protected void regrasNegocioCadastrar(ENTIDADE entidade) {
        regrasNegocioSalvar(entidade);
    }

    protected void regrasNegocioAlterar(ENTIDADE entidade) {
        regrasNegocioSalvar(entidade);
    }

    protected void regrasNegocioExcluir(ENTIDADE entidade) {
    }
}
