package br.com.deolhonacamara.api.config;

import jakarta.inject.Named;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@AllArgsConstructor
public class DataBaseConfig {
    private final PropertiesConfig propertiesConfig;

    @Bean
    @Named("dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(propertiesConfig.getDatabaseUrl());
        dataSource.setUsername(propertiesConfig.getDatabaseUsername());
        dataSource.setPassword(propertiesConfig.getDatabasePassword());
        dataSource.setDriverClassName(propertiesConfig.getDatabaseDriverClassName());
        return dataSource;
    }

    @Bean
    @Named("namedParameterJdbcTemplate")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Named("dataSource") DriverManagerDataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}