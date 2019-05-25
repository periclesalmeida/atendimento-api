#### Em contrução....
# Controle de Atendimento

Sistema de controle de atendimento ao cliente. Possibilita o cadastro de usuários, localização que podem ser guichês, salas, entre outros tipos. Além disso, permite o controle da geração de senha, senha prioritária, próximo no atendimento, entre outras opções. O sistema não está concluído.
## Guias

* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Swagger](https://swagger.io/)
 

### Instalando

O projeto utiliza a ferramenta de automação de construção Maven. 

* [Maven](https://maven.apache.org/) - Dependency Management

Para instalar o projeto é necessário, executar o comando.
```
./mvnw install -Pdev -DskipTests=true
```


## Executando os testes.

Comando necessário para executar todos os testes da aplicação.

```
./mvnw install -Ptest
```

