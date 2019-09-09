package br.com.periclesalmeida.atendimento.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Document(collection = "usuario")
public class Usuario {

    private Long sequencial;
    private String login;
    private String senha;
    private String senhaSemRash;
    private Boolean ativo;
    private Set<Permissao> permissoes;

    public Usuario(String login, String senha, Boolean ativo, Set<Permissao> permissoes) {
        this.login = login;
        this.senha = senha;
        this.senhaSemRash = senhaSemRash;
        this.ativo = ativo;
        this.permissoes = permissoes;
    }

    public Usuario() {
    }

    @Id
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotBlank(message = "Obrigatório informar o login")
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @JsonIgnore
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @NotNull(message="Obrigatório pelo menos uma permissão")
    public Set<Permissao> getPermissoes() {
        return permissoes;
    }
    public void setPermissoes(Set<Permissao> permissoes) {
        this.permissoes = permissoes;
    }

    @Transient
    public String getSenhaSemRash() {
        return senhaSemRash;
    }
    public void setSenhaSemRash(String senhaSemRash) {
        this.senhaSemRash = senhaSemRash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(sequencial, usuario.sequencial);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sequencial);
    }
}
