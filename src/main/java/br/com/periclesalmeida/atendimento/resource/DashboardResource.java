package br.com.periclesalmeida.atendimento.resource;

import br.com.periclesalmeida.atendimento.domain.dto.DashboardDTO;
import br.com.periclesalmeida.atendimento.service.DashboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardResource {

    private DashboardService dashboardService;

    public DashboardResource(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DashboardDTO> carregar() {
        DashboardDTO dashboardDTO = dashboardService.carregar();
        return ResponseEntity.status(HttpStatus.OK).body(dashboardDTO);
    }
}
