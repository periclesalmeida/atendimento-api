package br.com.periclesalmeida.atendimento.config.changelog;

import br.com.periclesalmeida.atendimento.domain.Permissao;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@ChangeLog
public class MongockChangeLog {

    @ChangeSet(id= "1.0", order = "001", author = "periclesalmeida")
    public void changeSet1(MongoTemplate mongoTemplate) {
        List<Permissao> permissoes =
                Arrays.asList(
                        new Permissao("ROLE_TIPO_LOCALIZACAO_CONSULTAR" ,"CONSULTAR TIPO DOCUMENTO"),
                        new Permissao("ROLE_TIPO_LOCALIZACAO_INCLUIR","INCLUIR TIPO DOCUMENTO"),
                        new Permissao("ROLE_TIPO_LOCALIZACAO_ALTERAR","ALTERAR TIPO DOCUMENTO"),
                        new Permissao("ROLE_LOCALIZACAO_CONSULTAR","CONSULTAR LOCALIZAÇÃO"),
                        new Permissao("ROLE_LOCALIZACAO_INCLUIR","INCLUIR LOCALIZAÇÃO"),
                        new Permissao("ROLE_LOCALIZACAO_ALTERAR","ALTERAR LOCALIZAÇÃO"),
                        new Permissao("ROLE_SERVICO_CONSULTAR","CONSULTAR SERVIÇO"),
                        new Permissao("ROLE_SERVICO_INCLUIR","INCLUIR SERVIÇO"),
                        new Permissao("ROLE_SERVICO_ALTERAR","ALTERAR SERVIÇO"),
                        new Permissao("ROLE_PERMISSAO_CONSULTAR","CONSULTAR PERMISSÃO"),
                        new Permissao("ROLE_USUARIO_CONSULTAR","CONSULTAR USUÁRIO"),
                        new Permissao("ROLE_USUARIO_INCLUIR","INCLUIR USUÁRIO"),
                        new Permissao("ROLE_USUARIO_ALTERAR","ALTERAR USUÁRIO")
                );

        mongoTemplate.insertAll(permissoes);
    }

    @Profile("dev")
    @ChangeSet(id= "1.1", order = "002", author = "periclesalmeida")
    public void changeSetDev(MongoTemplate mongoTemplate) {
        List<Permissao> permissoes =
                Arrays.asList(
                        new Permissao("ROLE_TIPO_LOCALIZACAO_CONSULTAR" ,"CONSULTAR TIPO DOCUMENTO"),
                        new Permissao("ROLE_TIPO_LOCALIZACAO_INCLUIR","INCLUIR TIPO DOCUMENTO"),
                        new Permissao("ROLE_TIPO_LOCALIZACAO_ALTERAR","ALTERAR TIPO DOCUMENTO"),
                        new Permissao("ROLE_LOCALIZACAO_CONSULTAR","CONSULTAR LOCALIZAÇÃO"),
                        new Permissao("ROLE_LOCALIZACAO_INCLUIR","INCLUIR LOCALIZAÇÃO"),
                        new Permissao("ROLE_LOCALIZACAO_ALTERAR","ALTERAR LOCALIZAÇÃO"),
                        new Permissao("ROLE_SERVICO_CONSULTAR","CONSULTAR SERVIÇO"),
                        new Permissao("ROLE_SERVICO_INCLUIR","INCLUIR SERVIÇO"),
                        new Permissao("ROLE_SERVICO_ALTERAR","ALTERAR SERVIÇO"),
                        new Permissao("ROLE_PERMISSAO_CONSULTAR","CONSULTAR PERMISSÃO"),
                        new Permissao("ROLE_USUARIO_CONSULTAR","CONSULTAR USUÁRIO"),
                        new Permissao("ROLE_USUARIO_INCLUIR","INCLUIR USUÁRIO"),
                        new Permissao("ROLE_USUARIO_ALTERAR","ALTERAR USUÁRIO")
                );

        mongoTemplate.insert(new Usuario("admin", "{MD5}c4ca4238a0b923820dcc509a6f75849b", true, new HashSet<>(permissoes)));
    }
}
