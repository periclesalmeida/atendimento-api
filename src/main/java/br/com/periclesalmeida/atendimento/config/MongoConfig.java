package br.com.periclesalmeida.atendimento.config;

import com.github.cloudyrock.mongock.SpringBootMongock;
import com.github.cloudyrock.mongock.SpringBootMongockBuilder;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Bean
    public SpringBootMongock mongock(ApplicationContext springContext, MongoClient mongoClient) {
        return new SpringBootMongockBuilder(mongoClient, database, "br.com.periclesalmeida.atendimento.config.changelog")
                .setApplicationContext(springContext)
                .setLockQuickConfig()
                .build();
    }
}
