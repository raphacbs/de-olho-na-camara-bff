Checklist do que vou entregar a seguir
- Resumo executivo do estado atual
- Problemas críticos (segurança, testes, dependências)
- Outras oportunidades de melhoria (qualidade, DevOps, observabilidade)
- Peças faltantes para considerar o projeto concluído
- Plano de ação priorizado com estimativas de esforço
- Comandos práticos imediatos (PowerShell) para mitigar riscos
- Próximos passos recomendados e checklist para PR

Resumo executivo
- Estado atual: projeto funcional e com camadas bem separadas (controllers implementam OpenAPI, serviços, repositórios, schedulers, Liquibase) — aproximadamente 40–60% do trabalho de código concluído.
- Principais blockers: credenciais e secrets expostos no repositório (CRÍTICO), ausência total de testes (CRÍTICO), dependência de JWT desatualizada (ALTA), ausência de CI/CD e containerização (MÉDIO).
- Impacto: sem correções imediatas de segurança e sem testes/CI, o projeto não está pronto para produção.

Problemas críticos (necessitam ação imediata)
1. Secrets hardcoded no repo (CRÍTICO)
   - Arquivos com valores sensíveis encontrados: `src/main/resources/application.properties`, `src/main/resources/application-dev.properties`, `src/main/resources/application-local.properties`.
   - Ex: `spring.datasource.username`, `spring.datasource.password`, `jwt.secret` aparecem em texto plano.
   - Risco: exposição do banco de dados e comprometimento de ambientes.

2. Zero tests (CRÍTICO)
   - `src/test/java` vazio — sem testes unitários nem de integração.
   - Risco: regressões, difícil manutenção e integração contínua inútil.

3. Dependência JJWT desatualizada (ALTA)
   - `pom.xml` usa JJWT 0.9.1 (vulnerável). Recomendo atualizar para 0.12.5+ (ou versão atual disponível compatível com Jakarta/Java17).

4. JwtService com APIs confusas (ALTA)
   - Vários métodos de validação com semântica sobreposta — refatorar para API clara e testável.

Outras oportunidades importantes
- Falta de CI/CD (GitHub Actions ou Jenkins) para executar build/testes/scan em PRs.
- Falta de Dockerfile / docker-compose para dev/test.
- Falta de Spring Boot Actuator e health endpoints.
- Falta de política de secrets (.env, .gitignore, templates `application.properties.example`).
- Falta de validação em DTOs (`@Valid`, `@NotNull`, etc) em alguns pontos.
- Logging/monitoring: adicionar Actuator + métricas (Prometheus) e configuração de logs por ambiente.
- Linting / Static analysis e verificação de vulnerabilidades (OWASP/Dependabot/Snyk).

Peças faltantes para considerar o projeto “concluído”
- Suite de testes com cobertura adequada (unit + integração)
- Pipeline CI que execute `mvn clean test` e scans de segurança
- Estratégia de secrets (env vars, Vault, ou CI secrets)
- Dockerfile e docker-compose para dev/test
- Documentação operacional (README com variáveis obrigatórias, como rodar, testes)
- Health checks e métricas básicas
- Correções de dependências e atualização de bibliotecas críticas

Plano de ação prioritário (com priorização e estimativa)
- Alta prioridade (corrigir hoje/amanhã)
  1. Remover secrets do repo e criar templates — Esforço: pequeno
  2. Atualizar JJWT e dependências críticas — Esforço: pequeno
  3. Criar `.github/workflows/ci.yml` mínimo que rode `mvn -DskipTests=false test` — Esforço: médio
  4. Criar testes básicos (Auth, JwtService, um Service crítico) — Esforço: grande

- Média prioridade (após bloqueios)
  5. Adicionar Actuator + health endpoints — Esforço: pequeno
  6. Dockerfile + docker-compose para dev — Esforço: médio
  7. Refatorar `JwtService` e adicionar testes unitários para ele — Esforço: pequeno-médio

- Baixa prioridade (melhorias contínuas)
  8. Input validation (`@Valid`) e melhoria de handlers de erro — Esforço: médio
  9. Monitoring/metrics e integração com Prometheus — Esforço: médio
  10. Rate limiting e CORS restrito — Esforço: médio

Comandos práticos imediatos (PowerShell) — MITIGAÇÃO rápida dos secrets
- Remover do controle de versão (não deletar localmente) e adicionar templates:
```powershell
# 1) Remover do git cache e adicionar ao .gitignore
git rm --cached src/main/resources/application.properties ; git rm --cached src/main/resources/application-dev.properties ; git rm --cached src/main/resources/application-local.properties
Add-Content -Path .gitignore -Value "`n# Local/secret properties`nsrc/main/resources/application*.properties`n"

# 2) Criar arquivos de template com placeholders (ex.: application.properties.example)
Copy-Item -Path src/main/resources/application.properties -Destination src/main/resources/application.properties.example
# Editar application.properties.example substituindo segredos por placeholders (manualmente ou via script)

# 3) Commit das mudanças
git add .gitignore src/main/resources/application.properties.example
git commit -m "chore: remove sensitive properties from repo and add example template"
```

Sugestão de conteúdo mínimo para `application.properties.example` (substituir valores reais)
- Crie `src/main/resources/application.properties.example` com:
```
spring.datasource.url=jdbc:postgresql://<DB_HOST>:5432/<DB_NAME>
spring.datasource.username=<DB_USER>
spring.datasource.password=<DB_PASSWORD>
jwt.secret=<JWT_SECRET>
# demais propriedades necessárias...
```

Dependências e alterações recomendadas no `pom.xml`
- Atualizar JJWT para 0.12.5+ (compatível com Jakarta/Java17). Verificar breaking changes e ajustar importes (jjwt-api/jjwt-impl/jjwt-jackson).
- Rodar `mvn dependency:tree` e configurar Dependabot/Snyk.

Plano de testes inicial (prioridade)
- Criar testes unitários:
  - `JwtService` (token criação/validação, edge cases)
  - `AuthController` (login/refresh token) — usar MockMvc
  - 2-3 serviços críticos (PoliticianService, PropositionService) com mock de repositórios
- Criar 1 teste de integração leve com banco H2 ou perfil `local` usando `@SpringBootTest` e Liquibase migrations aplicadas.

Modelo de CI simples (GitHub Actions) — exemplo de job básico
- Workflow: on: [pull_request, push:main]
  - steps:
    - Checkout
    - Setup JDK 17
    - Cache Maven
    - mvn -DskipTests=false clean test
    - (opcional) build docker image and push to registry on release

Checklist de segurança antes de abrir PR para produção
- [ ] Removidos todos os secrets do repositório
- [ ] `application*.properties` com placeholders em repo e instruções no README
- [ ] JJWT e dependências críticas atualizadas
- [ ] Testes principais adicionados e pipeline CI verde
- [ ] Actuator / health endpoint mínimo
- [ ] .gitignore atualizado
- [ ] Documentação com variáveis de ambiente obrigatórias

Próximos passos recomendados (3–6 ações concretas)
1. Imediato (agora): Remover arquivos `application*.properties` do Git e adicionar `application.properties.example`. (Eu posso executar os `apply_patch`/commits se quiser.)
2. Atualizar dependências críticas (`jjwt`) e rodar `mvn -DskipTests=false test`. Corrigir quebras. (Posso propor e aplicar patch no `pom.xml`.)
3. Adicionar um workflow de CI mínimo para garantir builds de PRs.
4. Implementar 10-15 testes unitários iniciais (priorizar `JwtService`, `AuthController`, 1-2 serviços).
5. Criar `Dockerfile` multi-stage e `docker-compose.yml` para dev.
6. Refatorar `JwtService` para simplificar API e reduzir duplicação, com testes cobrindo comportamento.

Posso ajudar executando (escolha uma ou várias):
- A: Remover secrets do repo e criar `application.properties.example` + atualizar `.gitignore`.
- B: Atualizar `pom.xml` para JJWT 0.12.5 e ajustar imports.
- C: Criar um workflow GitHub Actions CI mínimo.
- D: Implementar os primeiros testes unitários (JwtService + AuthController).
- E: Gerar um `Dockerfile` + `docker-compose.yml`.

O que prefere que eu faça agora? Posso começar pela ação A (mitigação de secrets) imediatamente e aplicar as alterações no repositório.

