package br.com.periclesalmeida.atendimento.util;

import org.springframework.beans.BeanUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.List;

import static org.springframework.data.domain.Example.of;

public abstract class AbstractService<ENTIDADE, ID extends Serializable> implements GenericService<ENTIDADE, ID> {

    protected abstract MongoRepository<ENTIDADE, ID> getRepository();

    @Override
    public ENTIDADE incluir(ENTIDADE entidade) {
        this.regrasNegocioIncluir(entidade);
        this.getRepository().insert(entidade);
        return entidade;
    }

    @Override
    public ENTIDADE alterar(ID identificador, ENTIDADE entidade) {
        ENTIDADE entidadeConsultada = this.consultarPorId(identificador);
        copiarPropriedadesDaEntidade(entidade, entidadeConsultada);
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

    public Page<ENTIDADE> consultarPassandoEntidade(ENTIDADE entidade, Pageable pageable) {
        return getRepository().findAll(of(entidade), pageable);
    }

    protected void regrasNegocioSalvar(ENTIDADE entidade) {
    }
    protected void regrasNegocioIncluir(ENTIDADE entidade) {
        regrasNegocioSalvar(entidade);
    }

    protected void regrasNegocioAlterar(ENTIDADE entidade) {
        regrasNegocioSalvar(entidade);
    }

    protected void regrasNegocioExcluir(ENTIDADE entidade) {
    }

    protected void copiarPropriedadesDaEntidade(ENTIDADE entidade, ENTIDADE entidadeConsultada) {
        BeanUtils.copyProperties(entidade, entidadeConsultada, getPropriedadesIgnoreAlterar());
    }

    protected String[] getPropriedadesIgnoreAlterar() {
        return new String[]{"id"};
    }
}
