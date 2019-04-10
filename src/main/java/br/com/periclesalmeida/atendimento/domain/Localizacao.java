package br.com.periclesalmeida.atendimento.domain;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="atm_localizacao", schema="admatm")
public class Localizacao implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long sequencial;
    private String descricao;
    private TipoLocalizacao tipo;
    private Boolean ativo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_localizacao", nullable=false)
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @Column(name="dsc_localizacao", nullable=false)
    @NotBlank(message="Obrigatório informar a descrição.")
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @ManyToOne
    @NotNull(message = "Obrigatório informar o tipo")
    @JoinColumn(name="seq_tipo_localizacao", referencedColumnName="seq_tipo_localizacao", nullable=false)
    public TipoLocalizacao getTipo() {
        return tipo;
    }
    public void setTipo(TipoLocalizacao tipo) {
        this.tipo = tipo;
    }

    @Column(name="ind_ativo", nullable=false)
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localizacao that = (Localizacao) o;
        return Objects.equals(sequencial, that.sequencial);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sequencial);
    }
}
