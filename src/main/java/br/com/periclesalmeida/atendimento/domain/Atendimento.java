package br.com.periclesalmeida.atendimento.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name="atm_atendimento", schema="admatm")
public class Atendimento implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long sequencial;
    private Long numeroAtendimento;
    private Date dataHoraCadastro;
    private Date dataHoraApresentacao;
    private Date dataHoraChamada;
    private Localizacao localizacao;
    private Servico servico;
    private Usuario usuario;
    private Boolean indicadorPrioridade = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_atendimento", nullable=false)
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotNull(message = "Obrigatório informar o número do atendimento")
    @Column(name="num_atendimento", nullable=false)
    public Long getNumeroAtendimento() {
        return numeroAtendimento;
    }
    public void setNumeroAtendimento(Long numeroAtendimento) {
        this.numeroAtendimento = numeroAtendimento;
    }

    @Column(name="dth_cadastro", nullable=false, updatable = false)
    public Date getDataHoraCadastro() {
        return dataHoraCadastro;
    }
    public void setDataHoraCadastro(Date dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    @Column(name="dth_apresentacao")
    public Date getDataHoraApresentacao() {
        return dataHoraApresentacao;
    }
    public void setDataHoraApresentacao(Date dataHoraApresentacao) {
        this.dataHoraApresentacao = dataHoraApresentacao;
    }

    @Column(name="dth_chamada")
    public Date getDataHoraChamada() {
        return dataHoraChamada;
    }
    public void setDataHoraChamada(Date dataHoraChamada) {
        this.dataHoraChamada = dataHoraChamada;
    }

    @ManyToOne
    @JoinColumn(name="seq_localizacao", referencedColumnName="seq_localizacao")
    public Localizacao getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    @ManyToOne
    @JoinColumn(name="seq_servico", referencedColumnName="seq_servico", nullable = false)
    public Servico getServico() {
        return servico;
    }
    public void setServico(Servico servico) {
        this.servico = servico;
    }

    @ManyToOne
    @JoinColumn(name="seq_usuario", referencedColumnName="seq_usuario")
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Column(name="ind_prioridade", nullable=false)
    public Boolean getIndicadorPrioridade() {
        return indicadorPrioridade;
    }
    public void setIndicadorPrioridade(Boolean indicadorPrioridade) {
        this.indicadorPrioridade = indicadorPrioridade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atendimento that = (Atendimento) o;
        return Objects.equals(sequencial, that.sequencial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequencial);
    }
}
