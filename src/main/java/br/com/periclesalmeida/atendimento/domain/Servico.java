package br.com.periclesalmeida.atendimento.domain;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name="atm_servico", schema="admatm")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long sequencial;
    private String descricao;
    private String sigla;
    private String tipoCor;
    private Long numeroAtendimentoAtual;
    private Boolean ativo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_servico", nullable=false)
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotBlank(message = "Obrigatório informar a descrição")
    @Column(name="dsc_servico", nullable=false)
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @NotBlank(message = "Obrigatório informar a sigla")
    @Column(name="dsc_sigla", nullable=false)
    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    @NotBlank(message = "Obrigatório informar a cor")
    @Column(name="tip_cor", nullable=false)
    public String getTipoCor() {
        return tipoCor;
    }
    public void setTipoCor(String tipoCor) {
        this.tipoCor = tipoCor;
    }

    @NotNull(message = "Obrigatório informar o número do atendimento atual")
    @Column(name="num_atendimento_atual", nullable=false)
    public Long getNumeroAtendimentoAtual() {
        return numeroAtendimentoAtual;
    }
    public void setNumeroAtendimentoAtual(Long numeroAtendimentoAtual) {
        this.numeroAtendimentoAtual = numeroAtendimentoAtual;
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
        Servico servico = (Servico) o;
        return Objects.equals(sequencial, servico.sequencial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequencial);
    }
}
