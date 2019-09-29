package br.com.periclesalmeida.atendimento.integration.localizacao;

import br.com.periclesalmeida.atendimento.domain.Localizacao;
import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.formatador.impl.FormatadorMensagemLocalizacaoImpl;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LocalizacaoStepDef extends AbstractStepDef<Localizacao> {

    private final String SIM = "SIM";
    private final String ROLE_LOCALIZACAO_INCLUIR = "ROLE_LOCALIZACAO_INCLUIR";
    private final String ROLE_LOCALIZACAO_CONSULTAR = "ROLE_LOCALIZACAO_CONSULTAR";
    private final String ROLE_LOCALIZACAO_ALTERAR = "ROLE_LOCALIZACAO_ALTERAR";
    private final String RESOURCE_LOCALIZACAO = "/localizacao/";

    private List<TipoLocalizacao> tiposExpectativa;
    private List<Servico> servicosExpectativa;

    @Before
    public void inicializarContexto() {
        limparBancoDeDados();
        this.formatadorDeMensagem = new FormatadorMensagemLocalizacaoImpl();
    }

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) throws Exception {
        inserirUsuarioIhPermissao(string, ROLE_LOCALIZACAO_INCLUIR, ROLE_LOCALIZACAO_CONSULTAR, ROLE_LOCALIZACAO_ALTERAR);
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
        List<Localizacao> localizacaoList = criarListaDeLocalizacaoDoDataTable(dataTable);
        getMongoTemplate().insertAll(localizacaoList);
    }

    @Quando("foi informada a descricao {string} e o tipo {string}")
    public void foi_informada_a_descricao_e_o_tipo(String descricao, String tipo) throws Exception {
        get(RESOURCE_LOCALIZACAO, criarMultiValueMapDosParametros(descricao, tipo));
    }

    @Quando("foi informaro a descrição {string}, tipo {string} e os serviços {string}")
    public void foi_informaro_a_descrição_tipo_e_os_serviços(String descricao, String tipo, String servicos) throws Exception{
        post(RESOURCE_LOCALIZACAO, criarEntidade(descricao, tipo, servicos, null));
    }

    @Quando("foi informaro a descrição {string}, tipo {string}, ativo {string} e os serviços {string} para a localização de id {string}")
    public void foi_informaro_a_descrição_tipo_ativo_e_os_serviços_para_a_localização_de_id(String descricao, String tipo, String ativo, String servicos, String id) throws Exception {
        Localizacao localizacao = criarEntidade(descricao, tipo, servicos, ativo);
        localizacao.setId(id);
        put(RESOURCE_LOCALIZACAO + id, localizacao);
    }

    @Então("o sistema exibe:")
    public void o_sistema_exibe(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Localizacao> expect = criarListaDeLocalizacaoDoDataTable(dataTable);
        List<Localizacao> result = extractListOfTheResponsePage();
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }


    @Então("os detalhes do Serviço são:")
    public void os_detalhes_do_Serviço_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Localizacao cadastrado = getMongoTemplate().findById(extractEntidadeDoResponse().getId(), Localizacao.class);
        Localizacao experado = criarListaDeLocalizacaoDoDataTable(dataTable).get(0);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(experado);
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(cadastrado);
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("deveria retornar sucesso")
    public void deveria_retorar_sucesso() throws Exception {
        expectativaSucesso();
    }

    @Então("deveria retornar erro")
    public void deveria_retornar_erro() throws Exception {
        expectativaDeErro();
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception {
        expectativaObjetoCriado();
    }

    @Então("a mensagem {string}")
    public void a_mensagem(String string) throws Exception {
        assertThat(getResultaActions().andReturn().getResponse().getContentAsString(), containsString(string));
    }

    private Localizacao criarEntidade(String descricao, String tipo, String servicos, String ativo) {
        Localizacao localizacao = new Localizacao();
        localizacao.setDescricao(descricao);
        localizacao.setTipo(getTipoLocalizacao(tipo));
        localizacao.setServicos(VerificadorUtil.naoEstaNuloOuVazio(servicos) ? criarListaDeServicoDaString(servicos) : null);
        localizacao.setAtivo(SIM.equals(ativo) ? true : false);
        return localizacao;
    }

    private Localizacao extractEntidadeDoResponse() throws IOException {
        return getMapper().readValue(getResultaActions().andReturn().
                        getResponse().getContentAsString(),
                Localizacao.class);
    }

    private Localizacao[] converterListaParaArray(List<Localizacao> lista) {
        return lista.stream().toArray(Localizacao[]::new);
    }

    private List<Localizacao> extractListOfTheResponsePage() throws java.io.IOException {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        return getMapper().convertValue(jsonNode.get("content"), new TypeReference<List<Localizacao>>(){});
    }

    private MultiValueMap<String, String> criarMultiValueMapDosParametros(String descricao, String tipo) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("descricao", descricao);
        if (VerificadorUtil.naoEstaNuloOuVazio(tipo)) params.set("tipo.id", tipo);

        return params;
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
