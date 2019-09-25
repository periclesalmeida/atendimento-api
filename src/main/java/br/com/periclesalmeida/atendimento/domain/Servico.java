package br.com.periclesalmeida.atendimento.domain;


import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.util.StringUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

@Document(collection = "servico")
public class Servico implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "Obrigatório informar a descrição")
    private String descricao;

    @NotBlank(message = "Obrigatório informar a sigla")
    private String sigla;

    @NotBlank(message = "Cor inválida")
    private String tipoCor;
    private Integer numeroAtendimentoAtual;
    private Boolean ativo;

    public Servico() {
    }

    public Servico(String id, String descricao, String sigla, String tipoCor) {
        this.id = id;
        this.descricao = descricao;
        this.sigla = sigla;
        this.tipoCor = tipoCor;
    }

    public Servico(String descricao, String sigla, String tipoCor) {
        this.descricao = descricao;
        this.sigla = sigla;
        this.tipoCor = tipoCor;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = StringUtil.setarUpperCase(descricao) ;
    }

    public String getSigla() {
        return sigla;
    }
    public void setSigla(String sigla) {
        this.sigla = StringUtil.setarUpperCase(sigla) ;
    }

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
        return Optional.ofNullable(getTipoCor()).isPresent() ? TipoCor.parse(getTipoCor()).getHtml() : null;
    }

    @Transient
    public String getTipoCorDescricao() {
        return Optional.ofNullable(getTipoCor()).isPresent() ? TipoCor.parse(getTipoCor()).getName() : null;
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
        return Objects.equals(id, servico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
