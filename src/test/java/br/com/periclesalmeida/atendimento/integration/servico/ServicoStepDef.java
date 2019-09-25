package br.com.periclesalmeida.atendimento.integration.servico;

import br.com.periclesalmeida.atendimento.domain.Servico;
import br.com.periclesalmeida.atendimento.domain.type.TipoCor;
import br.com.periclesalmeida.atendimento.integration.AbstractStepDef;
import br.com.periclesalmeida.atendimento.integration.formatador.impl.FormatadorMensagemServicoImpl;
import br.com.periclesalmeida.atendimento.integration.mapper.ServicoMapper;
import br.com.periclesalmeida.atendimento.util.VerificadorUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import cucumber.api.java.Before;
import cucumber.api.java.pt.Dado;
import cucumber.api.java.pt.Então;
import cucumber.api.java.pt.Quando;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ServicoStepDef extends AbstractStepDef<Servico> {

    private final String RESOURCE_SERVICO = "/servico/";
    private final String ROLE_SERVICO_INCLUIR = "ROLE_SERVICO_INCLUIR";
    private final String ROLE_SERVICO_ALTERAR = "ROLE_SERVICO_ALTERAR";
    private final String ROLE_SERVICO_CONSULTAR = "ROLE_SERVICO_CONSULTAR";

    @Before
    public void inicializarContexto() {
        limparBancoDeDados();
        this.formatadorDeMensagem = new FormatadorMensagemServicoImpl();
    }

    @Dado("que o usuário {string} possui permissao de acesso a funcionalidade")
    public void que_o_usuário_possui_permissao_de_acesso_a_funcionalidade(String string) throws Exception {
        inserirUsuarioIhPermissao(string, ROLE_SERVICO_CONSULTAR, ROLE_SERVICO_INCLUIR, ROLE_SERVICO_ALTERAR);
        prepararContextoDeSeguranca(string);
    }

    @Dado("que exitem serviços cadastrados")
    public void que_exitem_serviços_cadastrados(io.cucumber.datatable.DataTable dataTable) {
        getMongoTemplate().insertAll(criarListaDoDataTable(dataTable));
    }

    @Quando("foi informada a descricao {string}, a sigla {string} e a cor {string}")
    public void foi_informada_a_descricao_a_sigla_e_a_cor(String descricao, String sigla, String cor) throws Exception {
        get(RESOURCE_SERVICO, criarMultiValueMap(descricao, sigla, cor));
    }

    @Quando("foi informaro a descrição {string}, sigla {string} e cor {string}")
    public void foi_informaro_a_descrição_sigla_e_cor(String descricao, String sigla, String cor) throws Exception {
        post(RESOURCE_SERVICO, criarServico(descricao, sigla, cor));
    }

    @Quando("foi informaro a descrição {string}, sigla {string} e cor {string} para o serviço de id {string}")
    public void foi_informaro_a_descrição_sigla_e_cor_para_o_serviço_de_id(String descricao, String sigla, String cor, String id) throws Exception {
        Servico servico = criarServico(descricao, sigla, cor);
        servico.setId(id);
        put(RESOURCE_SERVICO + id, servico);
    }

    @Então("deveria retornar objecto criado com sucesso")
    public void deveria_retornar_objecto_criado_com_sucesso() throws Exception {
        expectativaObjetoCriado();
    }


    @Então("deveria retornar sucesso")
    public void deveria_retornar_sucesso() throws Exception {
        expectativaSucesso();
    }

    @Então("os detalhes do Serviço são:")
    public void os_detalhes_do_Serviço_são(io.cucumber.datatable.DataTable dataTable) throws Exception {
        Servico servicoCadastrado = getMongoTemplate().findById(extractEntidadeDoResponse().getId(), Servico.class);
        Servico servicoExperado = criarListaDoDataTable(dataTable).get(0);
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(servicoExperado);
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(servicoCadastrado);
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    @Então("deveria retornar erro")
    public void deveria_retornar_erro() throws Exception {
        expectativaDeErro();
    }

    @Então("a mensagem {string}")
    public void a_mensagem(String string) throws Exception {
        assertThat(getResultaActions().andReturn().getResponse().getContentAsString(), containsString(string));
    }

    @Então("o sistema exibe:")
    public void o_sistema_exibe(io.cucumber.datatable.DataTable dataTable) throws Exception {
        List<Servico> expect = criarListaDoDataTable(dataTable);
        List<Servico> result = extractListOfTheResponsePage();
        String mensagemExperada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(expect));
        String mensagemRetornada = formatadorDeMensagem.formatarMensagem(converterListaParaArray(result));
        assertEquals(mensagemExperada,mensagemRetornada);
    }

    private Servico extractEntidadeDoResponse() throws IOException {
        return getMapper().readValue(getResultaActions().andReturn().
                        getResponse().getContentAsString(),
                Servico.class);
    }

    private Servico criarServico(String descricao, String sigla, String cor) {
        return  new Servico(descricao, sigla, TipoCor.nameOf(cor) != null ? TipoCor.nameOf(cor).getValue() : null);
    }

    private List<Servico> extractListOfTheResponsePage() throws java.io.IOException {
        JsonNode jsonNode = getMapper().readValue(getResultaActions().andReturn().getResponse().getContentAsString(), JsonNode.class);
        try {
            return getMapper().convertValue(jsonNode, new TypeReference<List<Servico>>(){});
        } catch (Exception e) {
            return getMapper().convertValue(jsonNode.get("content"), new TypeReference<List<Servico>>(){});
        }
    }

    private Servico[] converterListaParaArray(List<Servico> lista) {
        return lista.stream().toArray(Servico[]::new);
    }

    private MultiValueMap<String, String> criarMultiValueMap(String descricao, String sigla, String cor) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("descricao", descricao);
        params.set("sigla", sigla);
        if (VerificadorUtil.naoEstaNuloOuVazio(cor)) {
            params.set("tipoCor",  TipoCor.valueOf(cor).getValue());
        }
        return params;
    }

    private List<Servico> criarListaDoDataTable(io.cucumber.datatable.DataTable dataTable) {
        List<Servico> tipos = new ArrayList<>();
        for (Map<String, String> map: dataTable.asMaps()) {
            Servico servico = new ServicoMapper().map(map);
            servico.setAtivo(true);
            servico.setNumeroAtendimentoAtual(0);
            tipos.add(servico);
        }
        return tipos;
    }
}
