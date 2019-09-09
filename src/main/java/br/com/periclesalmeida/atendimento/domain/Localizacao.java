package br.com.periclesalmeida.atendimento.domain;

import br.com.periclesalmeida.atendimento.util.StringUtil;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Document(collection = "localizacao")
public class Localizacao implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long sequencial;
    private String descricao;
    private TipoLocalizacao tipo;
    private Boolean ativo;
    private Set<Servico> servicos;

    @Id
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotBlank(message="Obrigatório informar a descrição.")
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = StringUtil.setarUpperCase(descricao) ;
    }

    @NotNull(message = "Obrigatório informar o tipo")
    public TipoLocalizacao getTipo() {
        return tipo;
    }
    public void setTipo(TipoLocalizacao tipo) {
        this.tipo = tipo;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @NotNull(message="Obrigatório pelo menos um serviço")
    public Set<Servico> getServicos() {
        return servicos;
    }
    public void setServicos(Set<Servico> servicos) {
        this.servicos = servicos;
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
