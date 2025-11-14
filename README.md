# De Olho na Câmara - BFF

## Como rodar o projeto

### Requisitos
- Java 22
- Maven 3.8+
- PostgreSQL
- Docker (opcional)

### Rodar com Maven
```bash
./mvnw spring-boot:run
```

### Rodar Liquibase manualmente
```bash
mvn liquibase:update -Plocal
```

### Swagger
- UI: http://localhost:8080/swagger-ui.html
- Docs: http://localhost:8080/v3/api-docs
