package br.com.periclesalmeida.atendimento.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "permissao")
public class Permissao implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String descricao;

    public Permissao(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Permissao() {
    }

    @Id
    @NotBlank(message = "Obrigatório informar o código")
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @NotBlank(message = "Obrigatório informar a descrição")
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permissao permissao = (Permissao) o;
        return Objects.equals(codigo, permissao.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
