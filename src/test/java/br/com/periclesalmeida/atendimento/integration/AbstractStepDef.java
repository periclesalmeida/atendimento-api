package br.com.periclesalmeida.atendimento.integration;

import br.com.periclesalmeida.atendimento.SistemaDeAtendimentoApplication;
import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
@ContextConfiguration(classes = {SistemaDeAtendimentoApplication.class} )
public abstract class AbstractStepDef {

    protected final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
    private final String SENHA_PADRAO = "1";

    @Autowired
    protected MongoTemplate mongoTemplate;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    protected ResultActions actions;

    @Autowired
    protected ObjectMapper mapper;

    @Value("${oauth2.clientId}")
    private String clientId;

    @Value("${oauth2.secret}")
    private String secret;
    private String tokenDeAcesso;

    @Before
    public void setUp() {
        limparBancoDeDados();
    }

    protected void obterAccessToken(String username) {
        MultiValueMap<String, String> params = createParametersOauthAuthentication(username, SENHA_PADRAO);
        String resultString = null;
        try {
            resultString = mockMvc.perform(post("/oauth/token")
                    .params(params)
                    .with(httpBasic(clientId, secret))
                    .accept(APPLICATION_JSON_CHARSET_UTF_8))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8))
                    .andReturn().getResponse().getContentAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.tokenDeAcesso = new JacksonJsonParser().parseMap(resultString).get("access_token").toString();
    }

    protected void inserirUsuarioComSenhaIhPermissao(String login, String... permissoes) {
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

    protected String convertObjectToJsonString(Object object) throws IOException {
        return mapper.writeValueAsString(object);
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

