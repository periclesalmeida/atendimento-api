package br.com.periclesalmeida.atendimento.config;

import br.com.periclesalmeida.atendimento.config.security.UsuarioSecurity;
import br.com.periclesalmeida.atendimento.domain.Usuario;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		UsuarioSecurity usuario = (UsuarioSecurity) authentication.getPrincipal();

		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put("id", usuario.getUsuario().getSequencial());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);
		return accessToken;
	}

}
