package br.com.periclesalmeida.atendimento.integration.tipo_localizacao;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.formatador.impl.FormatadorMensagemTipoLocalizacaoImpl;
import br.com.periclesalmeida.atendimento.integration.mapper.TipoLocalizacaoMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.Before;
import cucumber.api.java.it.Quando;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


public class TipoLocalizacaoStepDef extends AbstractStepDef<TipoLocalizacao> {

    private final String RESOURCE_TIPO_LOCALIZACAO = "/tipo-localizacao/";
    private final String ROLE_TIPO_LOCALIZACAO_CONSULTAR = "ROLE_TIPO_LOCALIZACAO_CONSULTAR";
    private final String ROLE_TIPO_LOCALIZACAO_INCLUIR = "ROLE_TIPO_LOCALIZACAO_INCLUIR";
    private final String ROLE_TIPO_LOCALIZACAO_ALTERAR = "ROLE_TIPO_LOCALIZACAO_ALTERAR";

    @Before
    public void inicializarContexto() {
        limparBancoDeDados();
        this.formatadorDeMensagem = new FormatadorMensagemTipoLocalizacaoImpl();
    }

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) throws Exception {
        inserirUsuarioIhPermissao(string, ROLE_TIPO_LOCALIZACAO_CONSULTAR, ROLE_TIPO_LOCALIZACAO_INCLUIR, ROLE_TIPO_LOCALIZACAO_ALTERAR);
        prepararContextoDeSeguranca(string);
    }

    @Dado("que exitem tipo localização cadastrados")
    public void que_exitem_tipo_localização_cadastrados(io.cucumber.datatable.DataTable dataTable) {
        List<TipoLocalizacao> tipos = criarListaDoDataTable(dataTable);
        getMongoTemplate().insertAll(tipos);
    }

    @Quando("foi que informado a descricao {string}")
    public void foi_que_informado_a_descricao(String string) throws Exception {
        get(RESOURCE_TIPO_LOCALIZACAO, criarMultiValueMapComDescricao(string));
    }

    @Então("o sistema exibe:")
    public void o_sistema_exibe(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<TipoLocalizacao> expect = criarListaDoDataTable(dataTable);
        List<TipoLocalizacao> result = extractListOfTheResponsePage();
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Quando("foi informaro a descrição {string}")
    public void foi_informaro_a_descrição(String string) throws Exception {
        post(RESOURCE_TIPO_LOCALIZACAO, new TipoLocalizacao(null, string));
    }

    @Quando("foi informaro a descrição {string} para o tipo documento de id {string}")
    public void foi_informaro_a_descrição_para_o_tipo_documento_de_id(String descricao, String id) throws Exception {
        put(RESOURCE_TIPO_LOCALIZACAO + id, new TipoLocalizacao(id, descricao));
    }

    @Então("deveria retornar sucesso")
    public void deveria_retornar_sucesso() throws Exception {
        expectativaSucesso();
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception{
        expectativaObjetoCriado();
    }

    @Então("os detalhes do Tipo Localização são:")
    public void os_detalhes_do_Tipo_Localização_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        TipoLocalizacao tipoLocalizacaoCadastrado = getMongoTemplate().findById(extractEntidadeDoResponse().getId(), TipoLocalizacao.class);
        TipoLocalizacao tipoLocalizacaoExperado = criarListaDoDataTable(dataTable).get(0);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(tipoLocalizacaoExperado);
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(tipoLocalizacaoCadastrado);
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("deveria retornar erro")
    public void deveria_retornar_erro() throws Exception{
        expectativaDeErro();
    }

    @Então("a mensagem {string}")
    public void a_mensagem(String string) throws Exception{
        assertThat(getResultaActions().andReturn().getResponse().getContentAsString(), containsString(string));
    }

    private TipoLocalizacao[] converterListaParaArray(List<TipoLocalizacao> lista) {
        return lista.stream().toArray(TipoLocalizacao[]::new);
    }

    private MultiValueMap<String, String> criarMultiValueMapComDescricao(String string) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("descricao", string);
        return params;
    }

    private TipoLocalizacao extractEntidadeDoResponse() throws IOException {
        return getMapper().readValue(getResultaActions().andReturn().
                        getResponse().getContentAsString(),
                TipoLocalizacao.class);
    }

    private List<TipoLocalizacao> extractListOfTheResponsePage() throws java.io.IOException {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        try {
            return getMapper().convertValue(jsonNode,new TypeReference<List<TipoLocalizacao>>(){});
        } catch (Exception e) {
            return getMapper().convertValue(jsonNode.get("content"), new TypeReference<List<TipoLocalizacao>>(){});
        }
    }

    private List<TipoLocalizacao> criarListaDoDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<TipoLocalizacao> tipos = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            tipos.add(new TipoLocalizacaoMapper().map(map));
        }
        return tipos;
    }
}
