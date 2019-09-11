package br.com.periclesalmeida.atendimento.domain;

import br.com.periclesalmeida.atendimento.util.DataUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Document(collection = "atendimento")
public class Atendimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String sequencial;

    @NotNull(message = "Obrigatório informar o número do atendimento")
    private Integer numeroAtendimento;
    private Date dataHoraCadastro;
    private Date dataHoraApresentacao;
    private Date dataHoraChamada;
    private Localizacao localizacao;
    private Servico servico;
    private Usuario usuario;
    private Boolean indicadorPrioridade;

    public String getSequencial() {
        return sequencial;
    }
    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public Integer getNumeroAtendimento() {
        return numeroAtendimento;
    }
    public void setNumeroAtendimento(Integer numeroAtendimento) {
        this.numeroAtendimento = numeroAtendimento;
    }

    public Date getDataHoraCadastro() {
        return dataHoraCadastro;
    }
    public void setDataHoraCadastro(Date dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public Date getDataHoraApresentacao() {
        return dataHoraApresentacao;
    }
    public void setDataHoraApresentacao(Date dataHoraApresentacao) {
        this.dataHoraApresentacao = dataHoraApresentacao;
    }

    public Date getDataHoraChamada() {
        return dataHoraChamada;
    }
    public void setDataHoraChamada(Date dataHoraChamada) {
        this.dataHoraChamada = dataHoraChamada;
    }

    public Localizacao getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public Servico getServico() {
        return servico;
    }
    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getIndicadorPrioridade() {
        return indicadorPrioridade;
    }
    public void setIndicadorPrioridade(Boolean indicadorPrioridade) {
        this.indicadorPrioridade = indicadorPrioridade;
    }

    @Transient
    public String getDataHoraCadastroFormatada() {
        return DataUtils.converterDataComHorarioParaString(getDataHoraCadastro());
    }
    
    @Transient
    public String getHoraChamada() {
        return DataUtils.converterDataParaStringNoFormato(getDataHoraChamada(), "HH:mm:ss");
    }

    @Transient
    public String getNumeroAtendimentoFormatado() {
        return String.format("%04d", getNumeroAtendimento());
    }

    @Transient
    public String getTempoDecorrido() {
        return DataUtils.getTextoTempoDecorrido(getDataHoraCadastro(), new Date());
    }

    @Transient
    public Boolean isRealizado() {
        return Optional.ofNullable(getDataHoraChamada()).isPresent();
    }

    @Transient
    public Boolean isEmEspera() {
        return !isRealizado();
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
