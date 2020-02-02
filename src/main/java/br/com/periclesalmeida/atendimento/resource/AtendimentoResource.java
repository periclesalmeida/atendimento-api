package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.Atendimento;
import br.com.periclesalmeida.atendimento.domain.dto.AtendimentoMovimentacaoChamadoDTO;
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
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_CONSULTAR')")
    public Atendimento consultarPorId(@PathVariable String id) {
        return atendimentoService.consultarPorId(id);
    }
    
    @GetMapping("/movimentacao/{idsServico}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_CONSULTAR')")
    public ResponseEntity<AtendimentoMovimentacaoDTO> consultarMovimentacao(@PathVariable List<String> idsServico) {
        AtendimentoMovimentacaoDTO atendimentoMovimentacaoDTO = atendimentoService.consultarMovimentacaoDoDiaDosServicos(idsServico);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoMovimentacaoDTO);
    }

    @GetMapping("/movimentacao-chamado/{idsServico}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_CONSULTAR')")
    public ResponseEntity<AtendimentoMovimentacaoChamadoDTO> consultarMovimentacaoChamado(@PathVariable List<String> idsServico) {
        AtendimentoMovimentacaoChamadoDTO atendimentoMovimentacaoDTO = atendimentoService.consultarMovimentacaoChamadaDoDiaDosServicos(idsServico);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoMovimentacaoDTO);
    }

    @PostMapping("/apresentar/{id}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_INCLUIR')")
    public ResponseEntity<Atendimento> apresentar(@PathVariable String id) {
        Atendimento atendimento = atendimentoService.apresentar(id);
        return ResponseEntity.status(HttpStatus.OK).body(atendimento);
    }


    @PostMapping("/gerar/{idServico}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenha(@PathVariable String idServico) {
        Atendimento atendimentoGerado = atendimentoService.gerar(idServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/gerar-prioridade/{idServico}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_INCLUIR')")
    public ResponseEntity<Atendimento> gerarSenhaPrioridade(@PathVariable String idServico) {
        Atendimento atendimentoGerado = atendimentoService.gerarPrioridade(idServico);
        return ResponseEntity.status(HttpStatus.CREATED).body(atendimentoGerado);
    }

    @PostMapping("/chamar-proximo/{idLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarProximo(@PathVariable String idLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarProximo(idLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }

    @PostMapping("/chamar-novamente/{id}/{idLocalizacao}")
    @PreAuthorize("hasAuthority('ROLE_ATENDIMENTO_INCLUIR')")
    public ResponseEntity<Atendimento> chamarNovamente(@PathVariable String id, @PathVariable String idLocalizacao) {
        Atendimento atendimentoChamado = atendimentoService.chamarNovamente(id, idLocalizacao);
        return ResponseEntity.status(HttpStatus.OK).body(atendimentoChamado);
    }
}
