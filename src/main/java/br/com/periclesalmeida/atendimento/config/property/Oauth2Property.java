package br.com.periclesalmeida.atendimento.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:oauth2.properties")
@ConfigurationProperties(prefix = "oauth2")
@Configuration
public class Oauth2Property {

    private String clientId;
    private String secret;
    private int accessTokenSeconds;
    private int refreshTokenSeconds;
    private String jwtSigningKey;
    private String origempermitida;

    public String getOrigempermitida() {
        return origempermitida;
    }
    public void setOrigempermitida(String origempermitida) {
        this.origempermitida = origempermitida;
    }

    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getAccessTokenSeconds() {
        return accessTokenSeconds;
    }
    public void setAccessTokenSeconds(int accessTokenSeconds) {
        this.accessTokenSeconds = accessTokenSeconds;
    }

    public int getRefreshTokenSeconds() {
        return refreshTokenSeconds;
    }
    public void setRefreshTokenSeconds(int refreshTokenSeconds) {
        this.refreshTokenSeconds = refreshTokenSeconds;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }
    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }
}
