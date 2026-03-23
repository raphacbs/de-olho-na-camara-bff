# AGENTS.md — Guidance for AI coding agents

Purpose
- Short, actionable directions so an AI can make safe, correct changes in this repo.

Where to look (quick)
- Controllers / endpoints: `src/main/java/br/com/deolhonacamara/api/controller` (e.g. `PoliticiansController.java`)
- Services / business logic: `src/main/java/br/com/deolhonacamara/api/service`
- DTOs / models: `src/main/java/br/com/deolhonacamara/api/dto` and generated OpenAPI models under `target/generated-sources/openapi`
- Schedulers: `src/main/java/br/com/deolhonacamara/scheduler` (jobs use `@Component` + `@Scheduled`)
- DB migrations: `src/main/resources/db/changelog` (Liquibase)
- Config, OpenAPI and mocks: `src/main/resources` (see `application-*.properties`, `swagger.yaml`, `mocks/`)

- Note: there are two swagger files present: `src/main/resources/swagger.yaml` and `src/main/resources/swagger-camara.yaml` (use `README.md` or resources to confirm the authoritative file).

- Application config holder: `src/main/java/br/com/deolhonacamara/api/config/PropertiesConfig.java` — contains keys used by runtime and schedulers (examples: `jwt.secret`, `jwt.expiration.hours`, `camara-deputados.api.base-url`, `proposition.sync.chunk-size`).

Note: some runtime properties use milliseconds (for example `jwt.expiration.ms`) and additional keys like `camara-deputados.api.timeout` appear in `application-*.properties` and `README.md`. Check `src/main/resources/application-*.properties` and `src/main/java/br/com/deolhonacamara/api/config/PropertiesConfig.java` for authoritative key names before using a property.

Key patterns (concrete)
- Controllers implement generated OpenAPI interfaces (package `net.coelho.deolhonacamara.api`) — implement method signatures and return `ResponseEntity`.
  - Example: controller implements `PoliticiansApi` and calls a service: `politicianService.find(...)`.
- Layering: controller -> service -> repository (follow `br.com.deolhonacamara` package layout).
- Schedulers: use `@Component`, constructor injection (`@RequiredArgsConstructor`) and `@Scheduled(cron = "...", zone = "America/Sao_Paulo")`.
  - Long-running sync jobs persist/resume progress using `SyncProgressService` / `SyncProgressEntity`. When implementing schedulers that page or chunk external data, follow the existing resume pattern (persist current page / last processed id) and honor the chunk-size property (`proposition.sync.chunk-size`) from `PropertiesConfig`.
- DB: add Liquibase changeSets to `src/main/resources/db/changelog`; do not edit existing changeSets — create a new one.

Build / run / tests (developer commands)
- Build and tests: `mvn clean install` or `mvn test`
- Generate OpenAPI sources (as in `pom.xml`): `mvn generate-sources`
- Run locally with a profile: `mvn spring-boot:run -Dspring-boot.run.profiles=local` or pass `--spring.profiles.active=local`

- Note: project targets Java 17 and Spring Boot 3.x — see `README.md` for exact version details and environment recommendations.

Integration points & external deps
- External API: Câmara dos Deputados (base URL configured via properties; look for camara-deputados config)
- DB: PostgreSQL managed with Liquibase changelogs in `src/main/resources/db/changelog`
- OpenAPI: spec at `src/main/resources/swagger.yaml`, codegen configured in `pom.xml` (openapi-generator)
  - Generated server interfaces and models appear under `target/generated-sources/openapi/src/main/java/net/coelho/deolhonacamara/api` after `mvn generate-sources`.

Agent rules (must-follow)
- If you change the HTTP surface, update `swagger.yaml` and add a matching OpenAPI change or coordinate with maintainers before regenerating sources.
- Always add new Liquibase changeSet files (follow existing folder structure and naming style).
- Implement generated API interfaces (prefer controller implementations over editing generated sources).
- Run `mvn -DskipTests=false test` after code changes and fix failing tests before committing.
- Use `src/main/resources/mocks/*` for offline integration testing (sample payloads exist).
- Authentication/authorization: the project uses JWT utilities in `br.com.deolhonacamara.api.service.JwtService` and reads JWT properties from `PropertiesConfig`. Controllers may extract user id either from Spring Security's `SecurityContextHolder` (see `PoliticiansController`) or by manually parsing the `Authorization` header (see `DashboardController`).

Verification & communication
- After OpenAPI/schema or DB changes, run full `mvn clean install` and include test output in PR.
- Ask maintainers about conventions if unclear: OpenAPI regeneration policy and preferred Liquibase authorship/naming.

Files that exemplify the patterns
- `pom.xml` (openapi-generator, mapstruct config)
- `src/main/resources/swagger.yaml` (API spec)
- `src/main/resources/db/changelog` (Liquibase changelogs)
- `src/main/resources/mocks/proposition.json` (mock payload)
- `src/main/java/br/com/deolhonacamara/scheduler/PoliticianSyncJob.java` (scheduler example)
- `src/main/java/br/com/deolhonacamara/scheduler/PropositionTramitationSyncJob.java` (long-running sync + resume example)
- `src/main/java/br/com/deolhonacamara/api/service/SyncProgressService.java` and `src/main/java/br/com/deolhonacamara/api/model/SyncProgressEntity.java` (sync progress persistence/resume pattern)
- `src/main/java/br/com/deolhonacamara/api/config/PropertiesConfig.java` (centralized runtime properties)
- `src/main/java/br/com/deolhonacamara/api/service/JwtService.java` (JWT utilities used by controllers)

End.
