package br.com.periclesalmeida.atendimento.integration.tipo_localizacao.stepdef;

import br.com.periclesalmeida.atendimento.domain.TipoLocalizacao;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.mapper.TipoLocalizacaoMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.it.Quando;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TipoLocalizacaoStepDef extends AbstractStepDef {

    private final String RESOURCE_TIPO_LOCALIZACAO = "/tipo-localizacao/";
    private final String ROLE_TIPO_LOCALIZACAO_CONSULTAR = "ROLE_TIPO_LOCALIZACAO_CONSULTAR";
    private final String ROLE_TIPO_LOCALIZACAO_INCLUIR = "ROLE_TIPO_LOCALIZACAO_INCLUIR";
    private final String ROLE_TIPO_LOCALIZACAO_ALTERAR = "ROLE_TIPO_LOCALIZACAO_ALTERAR";

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) {
        limparBancoDeDados();
        inserirUsuarioComSenhaIhPermissao(string, ROLE_TIPO_LOCALIZACAO_CONSULTAR, ROLE_TIPO_LOCALIZACAO_INCLUIR, ROLE_TIPO_LOCALIZACAO_ALTERAR);
        obterAccessToken(string);
    }

    @Dado("que exitem tipo localização cadastrados")
    public void que_exitem_tipo_localização_cadastrados(io.cucumber.datatable.DataTable dataTable) {
        List<TipoLocalizacao> tipos = criarListaDoDataTable(dataTable);
        mongoTemplate.insertAll(tipos);
    }

    @Quando("foi que informado a descricao {string}")
    public void foi_que_informado_a_descricao(String string) throws Exception {
        this.actions = mockMvc.perform(get(RESOURCE_TIPO_LOCALIZACAO,
                Pageable.unpaged()).param("descricao", string)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + getTokenDeAcesso()));
    }

    @Quando("foi solicitado listar tipo localização")
    public void foi_solicitado_listar_tipo_localização() throws Exception {
        this.actions = mockMvc.perform(get(RESOURCE_TIPO_LOCALIZACAO)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + getTokenDeAcesso()));
    }

    @Então("o sistema exibe:")
    public void o_sistema_exibe(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<TipoLocalizacao> tiposExperado = criarListaDoDataTable(dataTable);
        List<TipoLocalizacao> result = extractListOfTheResponse();

        assertThat(tiposExperado.size(), is(result.size()));
        for (int i = 0; i < result.size(); i++) {
            assertThat(tiposExperado.get(i).getId(), is(result.get(i).getId()));
            assertThat(tiposExperado.get(i).getDescricao(), equalTo(result.get(i).getDescricao()));
        }
    }

    @Quando("foi informaro a descrição {string}")
    public void foi_informaro_a_descrição(String string) throws Exception {
        this.actions = mockMvc.perform(post(RESOURCE_TIPO_LOCALIZACAO)
                .content( convertObjectToJsonString(new TipoLocalizacao(null, string)))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + getTokenDeAcesso()));
    }

    @Quando("foi informaro a descrição {string} para o tipo documento de id {string}")
    public void foi_informaro_a_descrição_para_o_tipo_documento_de_id(String descricao, String id) throws Exception {
        this.actions = mockMvc.perform(put(RESOURCE_TIPO_LOCALIZACAO + id)
                .content( convertObjectToJsonString(new TipoLocalizacao(id, descricao)))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization","Bearer " + getTokenDeAcesso()));
    }

    @Então("deveria retornar sucesso")
    public void deveria_retornar_sucesso() throws Exception {
        actions.andExpect(status().isOk());
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception{
        actions.andExpect(status().isCreated());
    }

    @Então("os detalhes do Tipo Localização são:")
    public void os_detalhes_do_Tipo_Localização_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        TipoLocalizacao tipoLocalizacaoCadastrado = mongoTemplate.findById(extractEntidadeDoResponse().getId(), TipoLocalizacao.class);
        assertThat(tipoLocalizacaoCadastrado.getDescricao(), equalTo(criarListaDoDataTable(dataTable).get(0).getDescricao()));
    }

    @Então("deveria retornar erro")
    public void deveria_retornar_erro() throws Exception{
        expectativaDeClientError();
    }

    @Então("a mensagem {string}")
    public void a_mensagem(String string) throws Exception{
        assertThat(actions.andReturn().getResponse().getContentAsString(), containsString(string));
    }

    private TipoLocalizacao extractEntidadeDoResponse() throws IOException {
        return mapper.readValue(actions.andReturn().
                        getResponse().getContentAsString(),
                TipoLocalizacao.class);
    }

    private void assertThatEntidade(TipoLocalizacao entidadeEsperada, TipoLocalizacao entidadeRetornada) {
        assertThat(entidadeEsperada.getId(), equalTo(entidadeRetornada.getId()));
        assertThat(entidadeEsperada.getDescricao(), equalTo(entidadeRetornada.getDescricao()));
    }

    private void expectativaDeSucesso() throws Exception {
        actions.andExpect(status().isOk());
    }

    private void expectativaDeClientError() throws Exception {
        actions.andExpect(status().is4xxClientError());
    }

    private List<TipoLocalizacao> extractListOfTheResponse() throws java.io.IOException {
        JsonNode jsonNode = mapper.readValue(actions.andReturn().getResponse().getContentAsString(), JsonNode.class);
        try {
            return mapper.convertValue(jsonNode,new TypeReference<List<TipoLocalizacao>>(){});
        } catch (Exception e) {
            return mapper.convertValue(jsonNode.get("content"), new TypeReference<List<TipoLocalizacao>>(){});
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
