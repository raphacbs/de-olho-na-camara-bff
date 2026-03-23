REGRAS DE DESENVOLVIMENTO — De Olho na Câmara (Memory Bank)

Resumo

Este documento centraliza as regras de desenvolvimento utilizadas pelo projeto e serve como "memory bank" para desenvolvedores e para assistentes de código (IA). Objetivo: garantir consistência, legibilidade e segurança do código, facilitar revisões e permitir que a IA produza código compatível.

1. Escopo

- Público: desenvolvedores e assistentes IA que contribuem para este repositório Java Spring Boot.
- Abrangência: convenções de nomenclatura, arquitetura, padrões de código, testes, CI/PR, segurança, migrações de banco (Liquibase), OpenAPI e exemplos.

2. Regras gerais

- Linguagem do código: TODO código (classes, interfaces, enums, métodos, variáveis, pacotes, arquivos fonte) deve estar em inglês.
- Labels/Conteúdo de front-end (textos exibidos ao usuário) podem estar em português.
- Mensagens de commit e PR: preferencialmente em inglês técnico conciso (ex.: feat: add politician search by state). Títulos/descrições em PR podem incluir português para contexto local.
- Comentários: preferencialmente em inglês. Comentários estritamente para o time (por exemplo, explicação de regras de negócio locais) podem ficar em português, mas mantenha-os curtos.
- Não commitar segredos (API keys, senhas, certificados). Use variáveis de ambiente e mecanismos de secret management.

3. Convenções de nomenclatura (must-follow)

- Packages: lower-case, dot-separated (ex.: br.com.deolhonacamara.api.controller).
- Classes e Interfaces: PascalCase (ex.: PoliticianController, PoliticianService, PoliticianRepository).
- Enums: PascalCase e nazament constants em UPPER_SNAKE_CASE (ex.: PoliticalParty, LEFT_RIGHT).
- Métodos: camelCase, verbs first (ex.: getPoliticianById(), listPoliticians()).
- Variáveis locais/atributos: camelCase (ex.: politicianRepository, isFollowed).
- DTOs/Response models: sufixo "Dto" ou "ResponseDto" (ex.: PoliticianDto, PoliticianResponseDto).
- Test classes: sufixo "Test" (ex.: PoliticianServiceTest).
- Arquivos YAML/Configs: snake_case ou kebab-case consistente para nomes de arquivo (ex.: application-local.properties, swagger.yaml).

4. Estrutura do projeto e responsabilidades (guideline)

- controller: expõe endpoints REST e realiza mapeamento simples de DTOs.
- service: contém lógica de negócio. Métodos devem ser pequenos e testáveis.
- repository: interação com o banco (Spring Data JPA ou JDBC templates).
- dto / model / mapper: DTOs usados por API, entidades JPA em model, mappers (MapStruct) para conversões.
- exception: exceções customizadas e handlers (ex.: RestExceptionHandler).
- config: configurações específicas e beans.
- scheduler: tarefas agendadas.

5. Padrões de código e boas práticas Java + Lombok

- Use Lombok para reduzir boilerplate: preferir @RequiredArgsConstructor, @Getter/@Setter quando necessário.
- Evitar @Data em entidades JPA (risco de equals/hashCode e lazy-loading). Prefira @Getter + @Setter ou DTOs imutáveis com @Value.
- Injeção de dependência: preferir construtor (já adotado com @RequiredArgsConstructor).
- Imutabilidade: preferir objetos imutáveis para DTOs quando possível.
- Exceptions: não usar exceções genéricas (RuntimeException) expostas ao usuário; criar exceções específicas e usar handlers.

6. REST API design

- Endpoints REST: seguir nomes RESTful e plurais (ex.: GET /politicians, GET /politicians/{id}).
- Query params: filters, pagination (page, size), sorting (sort), year etc.
- Status codes: usar códigos HTTP corretos (200 OK, 201 Created, 204 No Content, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 500 Internal Server Error).
- Contratos: manter swagger/openapi atualizado. Se gerar código a partir do OpenAPI, manter o arquivo master (swagger.yaml) como fonte de verdade e versionar mudanças.

7. Logging e observabilidade

- Use log4j2 (projeto já utiliza). Logs de debug/info/warn/error claros e em inglês preferencialmente.
- Não logar dados sensíveis (tokens, senhas, CPF, etc.).
- Em mensagens de erro para usuários, retornar mensagens genéricas, registrar detalhes no log.

8. Tratamento de erros

- Centralizar tratamento usando @ControllerAdvice + ExceptionHandler.
- Criar modelos de erro padronizados (error code, message, timestamp, path).
- Validar entradas (javax.validation / jakarta.validation) e mapear erros de validação para 400.

9. Testes

- Unit tests: JUnit 5 + Mockito. Cobertura mínima esperada por módulo: 70% (meta, não bloqueante).
- Integration tests: @SpringBootTest para fluxos críticos. Prefira Testcontainers para dependências (postgres, redis) em CI.
- Test names: [unit/integration] shouldDoSomething_WhenCondition.
- Test data: use builders/factories ou arquivos JSON sob src/test/resources/mocks.

10. CI / PR / commits

- Branches: feature/<short-description>, fix/<id>/<short-description>, hotfix/<short>.
- Pull Request: descreva o que foi feito, como testar, screenshots e links relevantes.
- Code review: pelo menos 1 reviewer; evitar merges sem revisão.
- Commit messages: seguir Conventional Commits (feat:, fix:, chore:, docs:, refactor:, test:, perf:).

11. Dependências e segurança

- Usar Maven para gerenciar dependências (pom.xml presente). Atualize versões com plugin versions-maven-plugin ou Dependabot.
- Executar ferramentas de análise (SpotBugs, Checkstyle, PMD) e scans de dependências no CI.
- Vulnerabilidades: atualizar imediatamente dependências com CVEs; documentar exceção temporária no PR.

12. Banco de dados e Liquibase

- Migrations via Liquibase (folder db/changelog). Cada mudança deve ser uma changeset com id e author claros.
- Não editar changesets já aplicados em produção. Para correções, crie um novo changeset.
- Mantém-se um changelog-master que referencia mudanças por módulo.
- Testar migrations localmente e em ambiente de integração antes de PR.

13. OpenAPI / geração de código

- swagger.yaml em resources é fonte de verdade do contrato público.
- Se gerar modelos/controladores a partir do OpenAPI, comite apenas gerados necessários e mantenha gerador configurado no pom.
- Nomeie schemas em inglês; descrições em português são aceitáveis se necessário.

14. Código gerado e arquivos target

- Não comitar arquivos gerados ou target/ (já no .gitignore). Comitar apenas arquivos de configuração e templates de geração.

15. Memory Bank — mapeamento de termos (PT -> EN)

Este mapa ajuda a IA e desenvolvedores a manter nomes consistentes no código e nos contratos.

- deputado -> politician
- politico -> politician
- proposicao -> proposition
- projeto_de_lei -> bill (ou "proposition" conforme domínio)
- partido -> party
- estado -> state
- despesa -> expense
- gasto -> expense
- usuario -> user
- usuario_autenticado -> authenticatedUser (ou authenticatedUserId)
- acompanhar -> follow
- seguido -> followed
- isFollowed -> isFollowed (boolean)
- gabinete -> office
- mandato -> term
- mandato_atual -> currentTerm
- documento -> document
- nota_fiscal -> invoice (se aplicável)
- relacao -> relationship
- pagina -> page
- tamanho -> size
- total -> total
- ano -> year
- parlamentar -> legislator (ou politician)
- legislatura -> legislature

16. Exemplos de nomenclatura (rápido)

- Controller: PoliticianController
- Service: PoliticianService
- Repository: PoliticianRepository
- DTO: PoliticianDto, PoliticianResponseDto
- Mapper: PoliticianMapper
- Test: PoliticianServiceTest
- Endpoints: GET /politicians?page=0&size=20&name=Silva

17. Snippets úteis (commands)

- Rodar testes locais:

```powershell
mvn test
```

- Executar build e gerar pacotes:

```powershell
mvn clean package -DskipTests=false
```

- Checar vulnerabilidades declaradas (sugestão de plugin):

```powershell
mvn org.owasp:dependency-check-maven:check
```

18. Checklist prático (pre-PR)

- [ ] Código compila e testes unitários passam localmente.
- [ ] Migrations Liquibase novas foram adicionadas e testadas.
- [ ] Swagger/OpenAPI atualizado se houve alteração de contrato.
- [ ] Não há segredos acidentalmente comitados.
- [ ] Mensagens de commit seguem Conventional Commits.
- [ ] Nome de todas as classes/métodos/variáveis em inglês.
- [ ] PR descreve como testar e casos de borda.

19. Armadilhas comuns e recomendações

- Evitar expor entidades JPA diretamente como payloads da API.
- Evitar usar @Data em entidades JPA.
- Validar parâmetros de entrada e evitar NullPointerExceptions por falta de checagens.
- Não alterar changesets já aplicados em produção.
- Prefira pequenos commits e PRs focados (menos mudanças = menos riscos).

20. Próximos passos / Sugestões de automação

- Adicionar Checkstyle e SpotBugs no pipeline CI, com regras derivadas deste documento.
- Habilitar Dependabot ou Renovate para atualização automática de dependências.
- Criar templates de PR e checklist automática no CI (GitHub Actions) que valide: build, tests, lint, dependency scan.

21. Contato e governança

- Esta documentação deve ser mantida no diretório `docs/` e atualizada sempre que houver padrões novos ou mudanças significativas no projeto.
- Para dúvidas de convenção, abra uma issue com a tag "convention" e marque o time.

----

Este documento é o memory bank principal — use-o como fonte de verdade para nomes e decisões rápidas. Se quiser, posso também gerar um arquivo `docs/MAPEAMENTO_TERMS.md` separado com um dicionário mais extenso e exemplos de mapeamento de nomes nos arquivos do projeto.
