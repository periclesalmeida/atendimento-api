package br.com.periclesalmeida.atendimento.integration;

import br.com.periclesalmeida.atendimento.SistemaDeAtendimentoApplication;
import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import br.com.periclesalmeida.atendimento.integration.formatador.FormatadorDeMensagem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ContextConfiguration(classes = {SistemaDeAtendimentoApplication.class} )
public abstract class AbstractStepDef<OBJETO> {

    private final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    private final String SENHA_PADRAO = "1";

    protected FormatadorDeMensagem<OBJETO> formatadorDeMensagem;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ResultActions resultaActions;

    @Autowired
    private ObjectMapper mapper;

    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.secret}")
    private String secret;
    private String tokenDeAcesso;

    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    protected ResultActions getResultaActions() {
        return resultaActions;
    }

    protected MockMvc getMockMvc() {
        return mockMvc;
    }

    protected  ObjectMapper getMapper() {
        return mapper;
    }

    protected void inserirUsuarioIhPermissao(String login, String... permissoes) {
        Set<Permissao> permissaoList = criarSetDePermissoes(permissoes);
        mongoTemplate.insert(new Usuario(login, passwordEncoder.encode(SENHA_PADRAO) , true, permissaoList));
    }

    protected void limparBancoDeDados() {
        for (String collectionName : mongoTemplate.getCollectionNames()) {
            mongoTemplate.dropCollection(collectionName);
        }
    }

    protected String getTokenDeAcesso() {
        return tokenDeAcesso;
    }

    protected String convertObjectToJsonString(OBJETO object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    protected void post(String resource, OBJETO entidade) throws Exception {
        this.resultaActions = mockMvc.perform(MockMvcRequestBuilders.post(resource)
                .content(convertObjectToJsonString(entidade))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(getHttpHeaders()));
    }

    protected void get(String resource, MultiValueMap<String, String> params) throws Exception {
        this.resultaActions = mockMvc.perform(MockMvcRequestBuilders.get(resource, getPageable())
                .params(params)
                .accept(MediaType.APPLICATION_JSON)
                .headers(getHttpHeaders()));
    }

    protected void put(String resource, OBJETO entidade) throws Exception{
        this.resultaActions = mockMvc.perform(MockMvcRequestBuilders.put(resource)
                .content( convertObjectToJsonString(entidade))
                .contentType(MediaType.APPLICATION_JSON)
                .headers(getHttpHeaders()));
    }

    protected void expectativaSucesso() throws Exception {
        getResultaActions().andExpect(status().isOk());
    }

    protected void expectativaObjetoCriado() throws Exception{
        getResultaActions().andExpect(status().isCreated());
    }

    protected void expectativaDeErro() throws Exception {
        getResultaActions().andExpect(status().is4xxClientError());
    }

    protected Pageable getPageable() {
        return new PageRequest(0, Integer.MAX_VALUE);
    }

    protected HttpHeaders getHttpHeaders() {
        return  new HttpHeaders() {{
            String authHeader = "Bearer " + getTokenDeAcesso();
            set("Authorization", authHeader );
        }};
    }

    protected void prepararContextoDeSeguranca(String username) throws Exception {
        MultiValueMap<String, String> params = createParametersOauthAuthentication(username, SENHA_PADRAO);
        String resultString =
                mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")
                .params(params)
                .with(httpBasic(clientId, secret))
                .accept(APPLICATION_JSON_CHARSET_UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                .andReturn().getResponse().getContentAsString();

        this.tokenDeAcesso = new JacksonJsonParser().parseMap(resultString).get("access_token").toString();
    }


    private Set<Permissao> criarSetDePermissoes(String[] permissoes) {
        Set<Permissao> permissaoList = new HashSet<>();
        for (String permissoe : permissoes) {
            permissaoList.add(new Permissao(permissoe,permissoe));
        }
        return permissaoList;
    }

    private MultiValueMap<String, String> createParametersOauthAuthentication(String username, String password) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "angular");
        params.add("username", username);
        params.add("password", password);
        return params;
    }
}

