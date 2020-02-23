package br.com.periclesalmeida.atendimento.domain.dto;

public class DashboardDTO {

    private long quantidadeAtendimentosHoje;
    private long quantidadeAtendimentosemEsperaHoje;
    private long quantidadeServicos;
    private long quantidadeLocalidades;

    private long quantidadeAtendimentoNaSemana;
    private long quantidadeAtendimentoNoMes;


    public long getQuantidadeAtendimentosHoje() {
        return quantidadeAtendimentosHoje;
    }

    public void setQuantidadeAtendimentosHoje(long quantidadeAtendimentosHoje) {
        this.quantidadeAtendimentosHoje = quantidadeAtendimentosHoje;
    }

    public long getQuantidadeAtendimentosemEsperaHoje() {
        return quantidadeAtendimentosemEsperaHoje;
    }

    public void setQuantidadeAtendimentosemEsperaHoje(long quantidadeAtendimentosemEsperaHoje) {
        this.quantidadeAtendimentosemEsperaHoje = quantidadeAtendimentosemEsperaHoje;
    }

    public long getQuantidadeServicos() {
        return quantidadeServicos;
    }

    public void setQuantidadeServicos(long quantidadeServicos) {
        this.quantidadeServicos = quantidadeServicos;
    }

    public long getQuantidadeLocalidades() {
        return quantidadeLocalidades;
    }

    public void setQuantidadeLocalidades(long quantidadeLocalidades) {
        this.quantidadeLocalidades = quantidadeLocalidades;
    }

    public long getQuantidadeAtendimentoNoMes() {
        return quantidadeAtendimentoNoMes;
    }

    public void setQuantidadeAtendimentoNoMes(long quantidadeAtendimentoNoMes) {
        this.quantidadeAtendimentoNoMes = quantidadeAtendimentoNoMes;
    }

    public long getQuantidadeAtendimentoNaSemana() {
        return quantidadeAtendimentoNaSemana;
    }

    public void setQuantidadeAtendimentoNaSemana(long quantidadeAtendimentoNaSemana) {
        this.quantidadeAtendimentoNaSemana = quantidadeAtendimentoNaSemana;
    }
}
