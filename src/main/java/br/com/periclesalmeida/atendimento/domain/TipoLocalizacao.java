package br.com.periclesalmeida.atendimento.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="atm_tipo_localizacao", schema="admatm")
public class TipoLocalizacao implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer codigo;
    private String descricao;

    @Id
    @Column(name="seq_tipo_localizacao", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getCodigo() {
        return codigo;
    }
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @Column(name="dsc_tipo_localizacao", nullable=false)
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
        TipoLocalizacao that = (TipoLocalizacao) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
