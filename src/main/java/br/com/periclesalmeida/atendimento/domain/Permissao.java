package br.com.periclesalmeida.atendimento.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="atm_permissao", schema="admatm")
public class Permissao implements Serializable {

    private static final long serialVersionUID = 1L;
    private String codigo;
    private String descricao;

    @Id
    @NotBlank(message = "Obrigatório informar o código")
    @Column(name="cod_permissao", nullable=false)
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @NotBlank(message = "Obrigatório informar a descrição")
    @Column(name="dsc_permissao", nullable=false)
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
