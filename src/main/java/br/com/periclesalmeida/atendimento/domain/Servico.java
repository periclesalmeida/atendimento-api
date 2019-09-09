package br.com.periclesalmeida.atendimento.domain;


import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.util.StringUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;

@Document(collection = "servico")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long sequencial;
    private String descricao;
    private String sigla;
    private String tipoCor;
    private Integer numeroAtendimentoAtual;
    private Boolean ativo;

    @Id
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotBlank(message = "Obrigatório informar a descrição")
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = StringUtil.setarUpperCase(descricao) ;
    }

    @NotBlank(message = "Obrigatório informar a sigla")
    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = StringUtil.setarUpperCase(sigla) ;
    }

    @NotBlank(message = "Obrigatório informar a cor")
    public String getTipoCor() {
        return this.tipoCor;
    }
    public void setTipoCor(String tipoCor) {
        this.tipoCor = tipoCor;
    }

    public Integer getNumeroAtendimentoAtual() {
        return numeroAtendimentoAtual;
    }
    public void setNumeroAtendimentoAtual(Integer numeroAtendimentoAtual) {
        this.numeroAtendimentoAtual = numeroAtendimentoAtual;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Transient
    public String getTipoCorHtml() {
        return TipoCor.parse(getTipoCor()).getHtml();
    }

    @Transient
    public String getTipoCorDescricao() {
        return TipoCor.parse(getTipoCor()).getName();
    }

    @Transient
    public TipoCor getTipoCorEnum() {
        return TipoCor.parse(tipoCor);
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
