package br.com.periclesalmeida.atendimento.integration.atendimento;

import br.com.periclesalmeida.atendimento.domain.*;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.formatador.impl.FormatadorMensagemAtendimentoImpl;
import br.com.periclesalmeida.atendimento.integration.mapper.AtendimentoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.LocalizacaoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.ServicoMapper;
import br.com.periclesalmeida.atendimento.integration.mapper.TipoLocalizacaoMapper;
import br.com.periclesalmeida.atendimento.util.DateUtil;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.Before;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import io.cucumber.datatable.DataTable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.everyItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AtendimentoStepDef extends AbstractStepDef<Atendimento> {

    private final String RESOURCE_ATENDIMENTO_CHAMAR_NOVAMENTE = "/atendimento/chamar-novamente/";
    private final String RESOURCE_ATENDIMENTO_GERAR_PRIORIDADE = "/atendimento/gerar-prioridade/";
    private final String RESOURCE_ATENDIMENTO_CHAMAR_PROXIMO = "/atendimento/chamar-proximo/";
    private final String RESOURCE_ATENDIMENTO_MOVIMENTACAO = "/atendimento/movimentacao/";
    private final String RESOURCE_ATENDIMENTO_MOVIMENTACAO_CHAMADO = "/atendimento/movimentacao-chamado/";
    private final String RESOURCE_ATENDIMENTO_GERAR = "/atendimento/gerar/";
    private final String ROLE_ATENDIMENTO_CONSULTAR = "ROLE_ATENDIMENTO_CONSULTAR";
    private final String ROLE_ATENDIMENTO_INCLUIR = "ROLE_ATENDIMENTO_INCLUIR";
    private List<TipoLocalizacao> tiposExpectativa;
    private List<Servico> servicosExpectativa;
    private List<Localizacao> localizacoesExpectativa;
    private AtendimentoMapper atendimentoMapper;

    @Before
    public void inicializarContexto() {
        limparBancoDeDados();
        this.formatadorDeMensagem = new FormatadorMensagemAtendimentoImpl();
    }

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) throws Exception {
        DateUtil.mockLocalDateTime(LocalDateTime.now());
        inserirUsuarioIhPermissao(string, ROLE_ATENDIMENTO_CONSULTAR, ROLE_ATENDIMENTO_INCLUIR);
        prepararContextoDeSeguranca(string);
        this.atendimentoMapper = new AtendimentoMapper();
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
        get(RESOURCE_ATENDIMENTO_MOVIMENTACAO + servicos);
    }

    @Quando("foi informado os serviços {string} para consulta de atendimento chamado")
    public void foi_informado_os_serviços_para_consulta_de_atendimento_chamado(String servicos) throws Exception {
        get(RESOURCE_ATENDIMENTO_MOVIMENTACAO_CHAMADO + servicos);
    }

    @Quando("foi informado a localização {string}")
    public void foi_informado_a_localização(String localizacao) throws Exception {
        post(RESOURCE_ATENDIMENTO_CHAMAR_PROXIMO + localizacao, null);
    }

    @Quando("foi informado a localização {string} para o atendimento {string}")
    public void foi_informado_a_localização_para_o_atendimento(String localizacao, String atendimento) throws Exception {
        post(RESOURCE_ATENDIMENTO_CHAMAR_NOVAMENTE + atendimento + "/" + localizacao, null);
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception {
        expectativaObjetoCriado();
    }

    @Então("deveria retornar sucesso")
    public void deveria_retornar_sucesso() throws Exception {
        expectativaSucesso();
    }

    @Então("deveria retornar erro")
    public void deveria_retornar_erro() throws Exception {
        expectativaDeErro();
    }

    @Então("a mensagem {string}")
    public void a_mensagem(String string) throws Exception {
        assertThat(getResultaActions().andReturn().getResponse().getContentAsString(), containsString(string));
    }

    @Então("os detalhes do Atendimento são:")
    public void os_detalhes_do_Atendimento_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Atendimento expect = criarListaDeAtendimentoDoDataTable(dataTable).get(0);
        Atendimento cadastrado = getMongoTemplate().findById(extractEntidadeDoResponse().getId(), Atendimento.class);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(expect);
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(cadastrado);
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("o sistema exibe os atendimetos em espera:")
    public void o_sistema_exibe_os_atendimetos_em_espera(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractListOfDataTableAndResponseAlsoAssert(dataTable, "atendimentosEmEspera");
    }

    @Então("os atendimentos realizados:")
    public void os_atendimentos_realizados(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractListOfDataTableAndResponseAlsoAssert(dataTable, "atendimentosRealizados");
    }

    @Então("o sistema exibe os atendimetos apresentados:")
    public void o_sistema_exibe_os_atendimetos_apresentados(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractListOfDataTableAndResponseAlsoAssert(dataTable, "atendimentosApresentados");
    }

    @Então("os atendimentos não apresentados:")
    public void os_atendimentos_não_apresentados(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractListOfDataTableAndResponseAlsoAssert(dataTable, "atendimentosNaoApresentados");
    }

    @Então("o próximo atendimento que deveria ser apresentado:")
    public void o_próximo_atendimento_que_deveria_ser_apresentado(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractEntityOfDataTableAndResponseAlsoAssert(dataTable, "proximoAtendimentoApresentado");
    }

    @Então("o último atendimento apresentado:")
    public void o_último_atendimento_apresentado(io.cucumber.datatable.DataTable dataTable) throws Exception {
        extractEntityOfDataTableAndResponseAlsoAssert(dataTable, "ultimoAtendimentoApresentado");
    }

    private void extractEntityOfDataTableAndResponseAlsoAssert(io.cucumber.datatable.DataTable dataTable, String entityName) throws Exception {
        List<Atendimento> expect = criarListaDeAtendimentoDoDataTable(dataTable);
        Atendimento result = extractEntityOfResponse(entityName);
        List<Atendimento> resultList = new ArrayList();
        Optional.ofNullable(result).ifPresent(atendimento -> resultList.add(atendimento));
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(resultList));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    private Atendimento extractEntityOfResponse(String entityName) throws Exception {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        return getMapper().convertValue(jsonNode.get(entityName), new TypeReference<Atendimento>(){});
    }

    private void extractListOfDataTableAndResponseAlsoAssert(io.cucumber.datatable.DataTable dataTable, String nomeOfListResponse) throws Exception {
        List<Atendimento> expect = criarListaDeAtendimentoDoDataTable(dataTable);
        List<Atendimento> result = extractSpecificListOfTheResponse(nomeOfListResponse);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    private List<Atendimento> extractSpecificListOfTheResponse(String nameOfListAtendimento) throws Exception {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        return getMapper().convertValue(jsonNode.get(nameOfListAtendimento), new TypeReference<List<Atendimento>>(){});
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
            Atendimento atendimento = atendimentoMapper.map(map);
            atendimento.setServico(getServico(map.get("servico")));

            if (VerificadorUtil.naoEstaNuloOuVazio(map.get("servico atendimento atual"))) {
                setarNumeroAtendimentoNoServico(map.get("servico atendimento atual"), atendimento.getServico());
            }

            atendimento.setLocalizacao(getLocalizacao(map.get("localizacao")));
            atendimento.setUsuario(VerificadorUtil.naoEstaNuloOuVazio(map.get("usuario")) ?  new Usuario(map.get("usuario")) : null);
            atendimentoList.add(atendimento);
        }
        return atendimentoList;
    }

    private void setarNumeroAtendimentoNoServico(String numeroAtendimento, Servico servico) {
        servico.setNumeroAtendimentoAtual(Integer.parseInt(numeroAtendimento));
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
