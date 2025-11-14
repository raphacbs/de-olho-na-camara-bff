package br.com.deolhonacamara.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.ms}")
    private  Integer jwtExpirationMs;

    @Value("${spring.datasource.url}")
    private String databaseUrl;
    @Value("${spring.datasource.username}")
    private String databaseUsername;
    @Value("${spring.datasource.password}")
    private String databasePassword;
    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverClassName;

    @Value("${domain.url}")
    private String domainUrl;

    @Value("${camara-deputados.api.base-url}")
    private String apiCamaraBaseUrl ;
    @Value("${camara-deputados.api.timeout}")
    private String timeout ;

}
