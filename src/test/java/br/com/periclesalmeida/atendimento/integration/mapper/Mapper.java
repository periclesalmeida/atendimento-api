package br.com.periclesalmeida.atendimento.integration.mapper;

import java.util.Map;

public interface Mapper<T> {

    public T map(Map<String, String> map);
}
