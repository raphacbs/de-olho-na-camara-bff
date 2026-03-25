package br.com.deolhonacamara.api.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class PropertiesConfig {
    public static final long DEFAULT_JWT_EXPIRATION_MS = 604800000L; // 7 days

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.expiration.ms:#{T(br.com.deolhonacamara.api.config.PropertiesConfig).DEFAULT_JWT_EXPIRATION_MS}}")
    private Long jwtExpirationMs;

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

    // New: chunk size for proposition tramitation sync job
    @Value("${proposition.sync.chunk-size:500}")
    private Integer propositionSyncChunkSize;

    // Max concurrent propositions fetched per chunk when syncing tramitations
    @Value("${proposition.sync.max-in-flight:50}")
    private Integer propositionSyncMaxInFlight;

    // Number of days to look back when syncing recent propositions/tramitations
    @Value("${proposition.recent.sync.days:3}")
    private Integer propositionRecentSyncDays;

    // Max concurrent vote save tasks
    @Value("${vote.sync.max-parallel-tasks:20}")
    private Integer voteSyncMaxParallelTasks;

    // Timeout (minutes) to wait vote tasks completion
    @Value("${vote.sync.timeout.minutes:5}")
    private Integer voteSyncTimeoutMinutes;

}
