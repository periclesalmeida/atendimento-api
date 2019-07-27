package br.com.periclesalmeida.atendimento.domain;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="atm_usuario", schema="admatm")
public class Usuario {

    private Long sequencial;
    private String login;
    private String senha;
    private String senhaSemRash;
    private Boolean ativo;
    private Set<Permissao> permissoes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="seq_usuario", nullable=false)
    public Long getSequencial() {
        return sequencial;
    }
    public void setSequencial(Long sequencial) {
        this.sequencial = sequencial;
    }

    @NotBlank(message = "Obrigatório informar o login")
    @Column(name="nom_login", nullable=false)
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name="dsc_senha", nullable=false)
    @JsonIgnore
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Column(name = "ind_ativo", nullable = false)
    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @ManyToMany(fetch=FetchType.LAZY)
    @NotNull(message="Obrigatório pelo menos uma permissão")
    @JoinTable(name="atm_usuario_permissao", schema="admatm",
            joinColumns={@JoinColumn(name="seq_usuario", referencedColumnName="seq_usuario")},
            inverseJoinColumns={@JoinColumn(name="cod_permissao", referencedColumnName="cod_permissao")
            })
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
