package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;
import br.com.periclesalmeida.atendimento.service.AtendimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/atendimento")
public class AtendimentoResource {

    private AtendimentoService atendimentoService;

    public AtendimentoResource(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @GetMapping("/{sequencial}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public Atendimento consultarPorId(@PathVariable Long sequencial) {
        return atendimentoService.consultarPorId(sequencial);
    }
    
    @GetMapping("/movimentacao/{sequencialLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<AtendimentoMovimentacaoDTO> consultarMovimentacao(@PathVariable Long sequencialLocalizacao) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoDTO = atendimentoService.consultarMovimentacaoDoDiaDaLocalizacao(sequencialLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoMovimentacaoDTO);
    }

    @PostMapping("/gerar/{sequencialServico}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenha(@PathVariable Long sequencialServico) {
        Atendimento atendimentoGerado = atendimentoService.gerar(sequencialServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/gerar-prioridade/{sequencialServico}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenhaPrioridade(@PathVariable Long sequencialServico) {
        Atendimento atendimentoGerado = atendimentoService.gerarPrioridade(sequencialServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/chamar-proximo/{sequencialLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarProximo(@PathVariable Long sequencialLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarProximo(sequencialLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }

    @PostMapping("/chamar-novamente/{sequencial}/{sequencialLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarNovamente(@PathVariable Long sequencial, @PathVariable Long sequencialLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarNovamente(sequencial, sequencialLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }
}
