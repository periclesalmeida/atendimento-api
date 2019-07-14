package br.com.periclesalmeida.atendimento.config;

import br.com.periclesalmeida.atendimento.config.property.AtendimentoApiProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile("dev")
@Configuration
@EnableSwagger2
public class SwaggerConfig  {

    @Autowired
    private AtendimentoApiProperty apiProperty;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.periclesalmeida.atendimento"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo())
                .securityContexts(Collections.singletonList(securityContext()))
                .securitySchemes(Arrays.asList(securitySchema()));
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title(apiProperty.getApiInfo().getTitle())
                .description(apiProperty.getApiInfo().getDescription())
                .version(apiProperty.getApiInfo().getVersion())
                .termsOfServiceUrl(apiProperty.getApiInfo().getTermsOfServiceUrl())
                .contact(new Contact(apiProperty.getApiInfo().getContact().getNome(), apiProperty.getApiInfo().getContact().getUrl(), apiProperty.getApiInfo().getContact().getEmail()))
                .license(apiProperty.getApiInfo().getLicense())
                .licenseUrl(apiProperty.getApiInfo().getLicenseUrl())
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        return Collections.singletonList(new SecurityReference("oauth2schema", criarArrayDeAuthorizationScope()));
    }

    private OAuth securitySchema() {
        List<GrantType> grantTypes = new ArrayList<>();
        grantTypes.add(new ResourceOwnerPasswordCredentialsGrant("http://localhost:8083/oauth/token"));
        OAuth oAuth = new OAuth("oauth2schema", Arrays.asList(criarArrayDeAuthorizationScope()), grantTypes);
        return oAuth;
    }

    @Bean
    public SecurityConfiguration securityInfo() {
        return new SecurityConfiguration(null, null, null, null, null,
                ApiKeyVehicle.HEADER,"Authorization",": Bearer");
    }

    private AuthorizationScope[] criarArrayDeAuthorizationScope() {
        AuthorizationScope[] authorizationScopesArray = new AuthorizationScope[2];
        authorizationScopesArray[0] = new AuthorizationScope("read", "read");
        authorizationScopesArray[1] = new AuthorizationScope("write", "write");
        return authorizationScopesArray;
    }
}
