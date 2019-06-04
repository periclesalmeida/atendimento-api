package br.com.periclesalmeida.atendimento.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenericService<ENTIDADE, ID> {

    ENTIDADE incluir(ENTIDADE entidade);
    ENTIDADE alterar(ID identificador, ENTIDADE entidade);
    ENTIDADE salvar(ENTIDADE entidade);
    void excluir(ENTIDADE entidade);

    ENTIDADE consultarPorId(ID identificador);
    List<ENTIDADE> consultarTodos();
    Page<ENTIDADE> consultarPassandoEntidade(ENTIDADE entidade, Pageable pageable);
}
