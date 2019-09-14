package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoDTO;
import br.com.periclesalmeida.atendimento.service.AtendimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/atendimento")
public class AtendimentoResource {

    private AtendimentoService atendimentoService;

    public AtendimentoResource(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public Atendimento consultarPorId(@PathVariable String id) {
        return atendimentoService.consultarPorId(id);
    }
    
    @GetMapping("/movimentacao/{idsServico}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<AtendimentoMovimentacaoDTO> consultarMovimentacao(@PathVariable List<String> idsServico) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoDTO = atendimentoService.consultarMovimentacaoDoDiaDaLocalizacao(idsServico);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoMovimentacaoDTO);
    }

    @PostMapping("/gerar/{idServico}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenha(@PathVariable String idServico) {
        Atendimento atendimentoGerado = atendimentoService.gerar(idServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/gerar-prioridade/{sequencialServico}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenhaPrioridade(@PathVariable String sequencialServico) {
        Atendimento atendimentoGerado = atendimentoService.gerarPrioridade(sequencialServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/chamar-proximo/{idLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarProximo(@PathVariable String idLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarProximo(idLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }

    @PostMapping("/chamar-novamente/{id}/{idLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_SERVICO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarNovamente(@PathVariable String id, @PathVariable String idLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarNovamente(id, idLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }
}
