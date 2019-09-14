package br.com.periclesalmeida.atendimento.domain;

import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@Document(collection = "atendimento")
public class Atendimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull(message = "Obrigatório informar o número do atendimento")
    private Integer numeroAtendimento;

    @NotNull
    private LocalDateTime dataHoraCadastro;
    private LocalDateTime dataHoraApresentacao;
    private LocalDateTime dataHoraChamada;
    private Localizacao localizacao;
    private Servico servico;
    private Usuario usuario;
    private Boolean indicadorPrioridade;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumeroAtendimento() {
        return numeroAtendimento;
    }
    public void setNumeroAtendimento(Integer numeroAtendimento) {
        this.numeroAtendimento = numeroAtendimento;
    }

    public LocalDateTime getDataHoraCadastro() {
        return dataHoraCadastro;
    }
    public void setDataHoraCadastro(LocalDateTime dataHoraCadastro) {
        this.dataHoraCadastro = dataHoraCadastro;
    }

    public LocalDateTime getDataHoraApresentacao() {
        return dataHoraApresentacao;
    }
    public void setDataHoraApresentacao(LocalDateTime dataHoraApresentacao) {
        this.dataHoraApresentacao = dataHoraApresentacao;
    }

    public LocalDateTime getDataHoraChamada() {
        return dataHoraChamada;
    }
    public void setDataHoraChamada(LocalDateTime dataHoraChamada) {
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
        return getDataHoraCadastro().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }
    
    @Transient
    public String getHoraChamada() {
        return VerificadorUtil.naoEstaNulo(getDataHoraChamada()) ?
                getDataHoraChamada().format(DateTimeFormatter.ofPattern("HH:mm:ss")) :
                null;
    }

    @Transient
    public String getNumeroAtendimentoFormatado() {
        return String.format("%04d", getNumeroAtendimento());
    }

    @Transient
    public String getTempoDecorrido() {
        Duration duration = Duration.between(LocalDateTime.now(), getDataHoraCadastro());
        long diff = Math.abs(duration.toMinutes());
        return diff + " minuto(s)";
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
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
