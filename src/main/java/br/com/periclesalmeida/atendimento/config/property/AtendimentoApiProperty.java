package br.com.periclesalmeida.atendimento.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "atendimento")
@Configuration
public class AtendimentoApiProperty {

    private ApiInfo apiInfo = new ApiInfo();

    public ApiInfo getApiInfo() {
        return apiInfo;
    }
    public void setApiInfo(ApiInfo apiInfo) {
        this.apiInfo = apiInfo;
    }

    public static class ApiInfo {
        private String title;
        private String description;
        private String version;
        private String termsOfServiceUrl;
        private String license;
        private String licenseUrl;

        private Contact contact = new Contact();

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        public String getVersion() {
            return version;
        }
        public void setVersion(String version) {
            this.version = version;
        }

        public Contact getContact() {
            return contact;
        }
        public void setContact(Contact contact) {
            this.contact = contact;
        }

        public String getTermsOfServiceUrl() {
            return termsOfServiceUrl;
        }
        public void setTermsOfServiceUrl(String termsOfServiceUrl) {
            this.termsOfServiceUrl = termsOfServiceUrl;
        }

        public String getLicense() {
            return license;
        }
        public void setLicense(String license) {
            this.license = license;
        }

        public String getLicenseUrl() {
            return licenseUrl;
        }
        public void setLicenseUrl(String licenseUrl) {
            this.licenseUrl = licenseUrl;
        }

        public static class Contact {
            private String nome;
            private String url;
            private String email;

            public String getNome() {
                return nome;
            }
            public void setNome(String nome) {
                this.nome = nome;
            }

            public String getUrl() {
                return url;
            }
            public void setUrl(String url) {
                this.url = url;
            }

            public String getEmail() {
                return email;
            }
            public void setEmail(String email) {
                this.email = email;
            }
        }
    }
}
