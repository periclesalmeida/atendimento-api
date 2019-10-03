package br.com.periclesalmeida.atendimento.integration.atendimento;

import br.com.periclesalmeida.atendimento.domain.*;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.formatador.impl.FormatadorMensagemAtendimentoImpl;
import br.com.periclesalmeida.atendimento.integration.mapper.AtendimentoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.LocalizacaoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.ServicoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.TipoLocalizacaoMapper;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.Before;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import io.cucumber.datatable.DataTable;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AtendimentoStepDef extends AbstractStepDef<Atendimento> {

    public static final String RESOURCE_ATENDIMENTO_GERAR_PRIORIDADE = "/atendimento/gerar-prioridade/";
    private final String RESOURCE_ATENDIMENTO_GERAR = "/atendimento/gerar/";
    private final String ROLE_ATENDIMENTO_CONSULTAR = "ROLE_ATENDIMENTO_CONSULTAR";
    private final String ROLE_ATENDIMENTO_INCLUIR = "ROLE_ATENDIMENTO_INCLUIR";
    private List<TipoLocalizacao> tiposExpectativa;
    private List<Servico> servicosExpectativa;
    private List<Localizacao> localizacoesExpectativa;

    @Before
    public void inicializarContexto() {
        limparBancoDeDados();
        this.formatadorDeMensagem = new FormatadorMensagemAtendimentoImpl();
    }

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) throws Exception {
        inserirUsuarioIhPermissao(string, ROLE_ATENDIMENTO_CONSULTAR, ROLE_ATENDIMENTO_INCLUIR);
        prepararContextoDeSeguranca(string);
    }

    @Dado("que exitem tipo localização cadastrados")
    public void que_exitem_tipo_localização_cadastrados(io.cucumber.datatable.DataTable dataTable) {
        tiposExpectativa = criarListaDeTipoDocumentoDoDataTable(dataTable);
        getMongoTemplate().insertAll(tiposExpectativa);
    }

    @Dado("que exitem serviços cadastrados")
    public void que_exitem_serviços_cadastrados(io.cucumber.datatable.DataTable dataTable) {
        servicosExpectativa = criarListaDeServicoDoDataTable(dataTable);
        getMongoTemplate().insertAll(servicosExpectativa);
    }

    @Dado("que exitem localizações cadastradas")
    public void que_exitem_localizações_cadastradas(io.cucumber.datatable.DataTable dataTable) {
        localizacoesExpectativa = criarListaDeLocalizacaoDoDataTable(dataTable);
        getMongoTemplate().insertAll(localizacoesExpectativa);
    }

    @Dado("que existem atendimentos cadastradas")
    public void que_existem_atendimentos_cadastradas(io.cucumber.datatable.DataTable dataTable) {
        List<Atendimento> atendimentoList = criarListaDeAtendimentoDoDataTable(dataTable);
        getMongoTemplate().insertAll(atendimentoList);
    }

    @Quando("foi informado o serviço {string}")
    public void foi_informado_o_serviço(String servico) throws Exception {
        post(RESOURCE_ATENDIMENTO_GERAR + servico, null);
    }

    @Quando("foi informado o serviço {string} para atendimento prioritário")
    public void foi_informado_o_serviço_para_atendimento_prioritário(String servico) throws Exception {
        post(RESOURCE_ATENDIMENTO_GERAR_PRIORIDADE + servico, null);
    }

    @Quando("foi informado os serviços {string}")
    public void foi_informado_os_serviços(String servicos) throws Exception {
        get("/atendimento/movimentacao/" + servicos);
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception {
        expectativaObjetoCriado();
    }

    @Então("os detalhes do Atendimento são:")
    public void os_detalhes_do_Atendimento_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Atendimento expect = criarListaDeAtendimentoDoDataTable(dataTable).get(0);
        expect.getServico().setNumeroAtendimentoAtual(expect.getServico().getNumeroAtendimentoAtual() + 1);
        Atendimento cadastrado = getMongoTemplate().findById(extractEntidadeDoResponse().getId(), Atendimento.class);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(expect);
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(cadastrado);
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("o sistema exibe os atendimetos em espera:")
    public void o_sistema_exibe_os_atendimetos_em_espera(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Atendimento> expect = criarListaDeAtendimentoDoDataTable(dataTable);
        List<Atendimento> result = extractListOfTheResponseAtendimentosEmEspera();
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("os atendimentos realizados:")
    public void os_atendimentos_realizados(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Atendimento> expect = criarListaDeAtendimentoDoDataTable(dataTable);
        List<Atendimento> result = extractListOfTheResponseAtendimentosRealizados();
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    private List<Atendimento> extractListOfTheResponseAtendimentosEmEspera() throws java.io.IOException {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        return getMapper().convertValue(jsonNode.get("atendimentosEmEspera"), new TypeReference<List<Atendimento>>(){});
    }

    private List<Atendimento> extractListOfTheResponseAtendimentosRealizados() throws java.io.IOException {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        return getMapper().convertValue(jsonNode.get("atendimentosRealizados"), new TypeReference<List<Atendimento>>(){});
    }

    private Atendimento[] converterListaParaArray(List<Atendimento> lista) {
        return lista.stream().toArray(Atendimento[]::new);
    }

    private Atendimento extractEntidadeDoResponse() throws IOException {
        return getMapper().readValue(getResultaActions().andReturn().
                        getResponse().getContentAsString(),
                Atendimento.class);
    }

    private List<Atendimento> criarListaDeAtendimentoDoDataTable(DataTable dataTable) {
        List<Atendimento> atendimentoList = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            Atendimento atendimento = new AtendimentoMapper().map(map);
            atendimento.setServico(getServico(map.get("servico")));
            atendimento.setLocalizacao(getLocalizacao(map.get("localizacao")));
            atendimento.setUsuario(VerificadorUtil.naoEstaNuloOuVazio(map.get("usuario")) ?  new Usuario(map.get("usuario")) : null);
            atendimentoList.add(atendimento);
        }
        return atendimentoList;
    }

    private Localizacao getLocalizacao(String id) {
        return this.localizacoesExpectativa.stream().filter(localizacao -> localizacao.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    private List<Localizacao> criarListaDeLocalizacaoDoDataTable(DataTable dataTable) {
        List<Localizacao> localizacaoList = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            Localizacao localizacao = new LocalizacaoMapper().map(map);
            localizacao.setTipo(getTipoLocalizacao(map.get("tipo")));
            localizacao.setServicos(Optional.ofNullable(map.get("servicos")).isPresent() ? criarListaDeServicoDaString(map.get("servicos") ): null);
            localizacaoList.add(localizacao);
        }
        return localizacaoList;
    }

    private Set<Servico> criarListaDeServicoDaString(String servicos) {
        Set<Servico> servicoList = new HashSet<>();
        if (VerificadorUtil.naoEstaNulo(servicos)) {
            for (String codigoServico: servicos.split(",")) {
                servicoList.add(getServico(codigoServico));
            }
        }
        return servicoList;
    }

    private Servico getServico(String codigoServico) {
        return servicosExpectativa.stream()
                .filter(servico1 -> servico1.getId().equals(codigoServico))
                .findFirst()
                .orElse(null);
    }

    private TipoLocalizacao getTipoLocalizacao(String codigo) {
        return tiposExpectativa.stream().
                filter(tipoLocalizacao -> tipoLocalizacao.getId().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    private List<Servico> criarListaDeServicoDoDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<Servico> tipos = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            Servico servico = new ServicoMapper().map(map);
            servico.setAtivo(true);
            servico.setNumeroAtendimentoAtual(0);
            tipos.add(servico);
        }
        return tipos;
    }

    private List<TipoLocalizacao> criarListaDeTipoDocumentoDoDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<TipoLocalizacao> tipos = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            tipos.add(new TipoLocalizacaoMapper().map(map));
        }
        return tipos;
    }
}
