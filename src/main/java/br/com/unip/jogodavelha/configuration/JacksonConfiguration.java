package br.com.unip.jogodavelha.configuration;

import br.com.unip.jogodavelha.service.DatabaseInitializationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfiguration {

    @Getter
    @Autowired
    private DatabaseInitializationService databaseInitializationService;

    @Getter
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String databaseStrategy;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.configure(this.objectMapper());
        return builder;
    }

    @Bean
    public boolean instantiateDatabse() {
        if (!"create".equals(this.getDatabaseStrategy())) {
            return false;
        }
        this.getDatabaseInitializationService().inicialization();
        return true;
    }

}
