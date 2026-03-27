# De Olho na Câmara - BFF

Backend For Frontend (BFF) que fornece uma API REST para monitoramento de deputados federais, integrando dados da API pública da Câmara dos Deputados e permitindo que cidadãos acompanhem o desempenho e comportamento de seus representantes.

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Banco de Dados](#banco-de-dados)
- [Endpoints da API](#endpoints-da-api)
- [Server-Driven UI (SDUI)](#-server-driven-ui-sdui)
- [Schedulers](#schedulers)
- [Configuração](#configuração)
- [Como Executar](#como-executar)
- [Documentação da API](#documentação-da-api)
- [Desenvolvimento](#desenvolvimento)

## 🎯 Visão Geral

**De Olho na Câmara** é um sistema que permite aos cidadãos monitorar o desempenho e comportamento de deputados federais de forma contínua e personalizada. O BFF sincroniza diariamente dados da API pública da Câmara dos Deputados e armazena em um banco de dados próprio, permitindo:

- Consulta rápida e eficiente de dados
- Histórico de informações
- Funcionalidades personalizadas
- Análises e comparações
- Notificações sobre atividades dos políticos seguidos

## 🛠 Tecnologias

### Backend
- **Java 17**
- **Spring Boot 3.5.0**
- **Spring Data JDBC** - Acesso ao banco de dados
- **Spring Security** - Autenticação e autorização
- **Spring Scheduler** - Tarefas agendadas
- **Lombok** - Redução de boilerplate
- **MapStruct** - Mapeamento de objetos
- **JWT** - Autenticação baseada em tokens

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Liquibase** - Versionamento de schema

### Documentação
- **OpenAPI 3.0** - Especificação da API
- **Swagger UI** - Interface de documentação interativa

### Ferramentas
- **Maven** - Gerenciamento de dependências
- **Docker** (opcional) - Containerização

## 🏗 Arquitetura

O projeto segue uma arquitetura em camadas:

```
┌─────────────────────────────────────┐
│         Controllers (REST)          │
│   (Implementam interfaces Swagger)  │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│            Services                 │
│   (Lógica de negócio)               │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│         Repositories                 │
│   (Acesso ao banco de dados)         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│      PostgreSQL Database             │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│      CamaraDeputadosService         │
│   (Integração com API externa)      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│   API Câmara dos Deputados          │
│   (https://dadosabertos.camara.leg.br)│
└─────────────────────────────────────┘
```

## 📁 Estrutura do Projeto

```
src/main/java/br/com/deolhonacamara/
├── api/
│   ├── config/              # Configurações (Security, CORS, etc.)
│   ├── controller/          # Controllers REST
│   ├── dto/                 # DTOs da API da Câmara
│   ├── handler/             # Tratamento de exceções
│   ├── interceptor/         # Interceptadores HTTP
│   ├── mapper/              # Mapeamento Entity ↔ DTO
│   ├── model/               # Entidades do domínio
│   ├── repository/          # Repositórios de dados
│   └── service/              # Serviços de negócio
├── exception/               # Exceções customizadas
├── request/                 # Cliente HTTP customizado
└── scheduler/               # Jobs agendados
```

## 🗄 Banco de Dados

### Schemas

O banco de dados está organizado em schemas:

- **`authentication`** - Dados de autenticação e usuários
- **`camara_deputados`** - Dados dos deputados e atividades

### Tabelas Principais

#### `authentication.users`
Armazena informações dos usuários do sistema.

#### `camara_deputados.politicians`
Informações básicas dos deputados sincronizadas da API da Câmara.

#### `camara_deputados.user_followed_politicians`
Relaciona usuários com os políticos que seguem.

#### `camara_deputados.politician_expense`
Despesas dos deputados.

#### `camara_deputados.vote` e `camara_deputados.politician_vote`
Votações e posicionamentos dos deputados.

#### `camara_deputados.speech`
Discursos dos deputados na Câmara.

#### `camara_deputados.proposition` e `camara_deputados.politician_proposition`
Proposições legislativas e seus autores.

#### `camara_deputados.presence`
Registros de presença dos deputados.

#### `camara_deputados.user_device`
Dispositivos dos usuários para notificações push.

### Migrações

As migrações do banco de dados são gerenciadas pelo Liquibase e estão localizadas em:
```
src/main/resources/db/changelog/
```

## 🔌 Endpoints da API

### Autenticação

#### `POST /api/v1/auth/register`
Registra um novo usuário.

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "password": "senha123",
  "fullName": "Nome Completo"
}
```

#### `POST /api/v1/auth/login`
Realiza login e retorna tokens de acesso.

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expireIn": 900
}
```

### Políticos

#### `GET /api/v1/politicians`
Lista todos os políticos com paginação e filtros.

**Query Parameters:**
- `page` (opcional, default: 0) - Número da página
- `size` (opcional, default: 20) - Tamanho da página
- `name` (opcional) - Busca parcial por nome
- `party` (opcional) - Filtro por sigla do partido
- `state` (opcional) - Filtro por UF

**Exemplo:**
```
GET /api/v1/politicians?page=0&size=20&party=PT&state=SP
```

#### `GET /api/v1/politicians/{id}`
Retorna detalhes de um político específico.

### Seguindo Políticos

#### `GET /api/v1/followed`
Lista os políticos seguidos pelo usuário autenticado.

**Headers:**
- `Authorization: Bearer {token}`

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)

#### `POST /api/v1/followed/{politicianId}`
Segue um político.

**Headers:**
- `Authorization: Bearer {token}`

#### `DELETE /api/v1/followed/{politicianId}`
Deixa de seguir um político.

**Headers:**
- `Authorization: Bearer {token}`

### Despesas

#### `GET /api/v1/politicians/{id}/expenses`
Retorna as despesas de um político.

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)
- `year` (opcional) - Filtro por ano
- `month` (opcional) - Filtro por mês (1-12)

### Votações

#### `GET /api/v1/politicians/{id}/votes`
Retorna as votações de um político.

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)

### Discursos

#### `GET /api/v1/politicians/{id}/speeches`
Retorna os discursos de um político.

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)

### Proposições

#### `GET /api/v1/politicians/{id}/propositions`
Retorna as proposições de um político.

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)

### Presenças

#### `GET /api/v1/politicians/{id}/presence`
Retorna os registros de presença de um político.

**Query Parameters:**
- `page` (opcional, default: 0)
- `size` (opcional, default: 20)

### Dispositivos

#### `POST /api/v1/devices`
Registra ou atualiza um dispositivo do usuário para notificações push.

**Headers:**
- `Authorization: Bearer {token}`

**Request Body:**
```json
{
  "fcmToken": "token-do-firebase-cloud-messaging"
}
```

#### `DELETE /api/v1/devices?fcmToken={token}`
Remove o registro de um dispositivo.

**Headers:**
- `Authorization: Bearer {token}`

## 📱 Server-Driven UI (SDUI)

O BFF expõe endpoints SDUI que retornam definições de tela em vez de dados brutos. O cliente mobile renderiza cada componente com base no campo `type`, eliminando a necessidade de atualizar o app para pequenas mudanças de layout.

### Conceitos fundamentais

| Elemento | Descrição |
|---|---|
| `HomeScreenResponse` | Envelope raiz — contém `screenId`, `version` e a lista de `components` |
| `ScreenComponent` | Unidade de renderização — possui `id`, `type` e `properties` (shape varia por tipo) |
| Propriedades | POJO com `@Builder` no pacote `sdui/model/properties/` que define os dados do componente |
| `ClientInfo` | Record que agrupa os headers de contexto do dispositivo/app enviados pelo cliente |

**Exemplo de resposta:**
```json
{
  "screenId": "politicians",
  "version": "1.0",
  "components": [
    {
      "id": "politicians-search",
      "type": "SEARCH_BAR",
      "properties": { "placeholder": "Pesquisar por nome..." }
    },
    {
      "id": "politicians-list",
      "type": "POLITICIAN_CARD_LIST",
      "properties": {
        "items": [ { "id": 204379, "name": "Acácio Favacho", "party": "MDB", "state": "AP", ... } ],
        "total": 513, "currentPage": 0, "totalPages": 26
      }
    }
  ]
}
```

### Endpoints SDUI disponíveis

| Endpoint | `screenId` | Componentes retornados |
|---|---|---|
| `GET /api/v1/sdui/home` | `home` | `YEAR_SELECTOR_BANNER`, `GREETING_HEADER`, `STATS_GRID`, `QUICK_ACCESS_GRID`, `SECTION_HEADER_WITH_BADGE` |
| `GET /api/v1/sdui/politicians` | `politicians` | `SEARCH_BAR`, `FILTER_SECTION`, `POLITICIAN_CARD_LIST` |
| `GET /api/v1/sdui/propositions` | `propositions` | `PROPOSITION_CARD_LIST` |
| `GET /api/v1/sdui/propositions/{id}` | `proposition-detail-{id}` | `PROPOSITION_DETAIL_HEADER`, `DETAIL_SECTION` ×N, `TEXT_LINK_SECTION`, `AUTHOR_CARD_LIST` |
| `GET /api/v1/sdui/politicians/{id}/expenses` | `politician-expenses-{id}` | `EXPENSE_CARD_LIST` |

### Headers de contexto do cliente (todos os endpoints SDUI)

Todos os endpoints SDUI aceitam os seguintes headers opcionais para identificação e contextualização do dispositivo/app. Os dados são usados para logging e análise; nenhum header é obrigatório — clientes mais antigos que não os enviem continuam funcionando normalmente.

| Header | Tipo | Exemplo | Descrição |
|---|---|---|---|
| `X-App-Version` | `string` | `"1.2.3"` | Versão do aplicativo cliente |
| `X-App-Platform` | `string` (enum) | `"android"` / `"ios"` / `"web"` | Sistema operacional / plataforma do dispositivo |
| `X-OS-Version` | `string` | `"14.0"` | Versão do sistema operacional |
| `X-Device-Model` | `string` | `"Samsung Galaxy S21"` | Modelo do hardware do dispositivo |
| `X-Device-Id` | `string` (UUID) | `"a1b2c3d4-..."` | Identificador único e estável do dispositivo (usado para analytics) |
| `X-App-Language` | `string` (BCP-47) | `"pt-BR"` | Locale/idioma configurado no app |

**Exemplo de requisição com headers:**
```http
GET /api/v1/sdui/home HTTP/1.1
Authorization: Bearer <token>
X-App-Version: 1.2.3
X-App-Platform: android
X-OS-Version: 13
X-Device-Model: Samsung Galaxy S21
X-Device-Id: a1b2c3d4-e5f6-7890-abcd-ef1234567890
X-App-Language: pt-BR
```

### Tipos de componentes e suas propriedades

#### Componentes de Home

| `type` | Classe de propriedades | Campos principais |
|---|---|---|
| `YEAR_SELECTOR_BANNER` | `YearSelectorBannerProperties` | `title`, `subtitle`, `selectedYear`, `buttonBackgroundColor` |
| `GREETING_HEADER` | `GreetingHeaderProperties` | `greeting`, `subtitle` |
| `STATS_GRID` | `StatsGridProperties` | `columns`, `items` (lista de `StatCardItem` com `id`, `icon`, `value`, `label`, `backgroundColor`, `action`) |
| `QUICK_ACCESS_GRID` | `QuickAccessGridProperties` | `title`, `columns`, `items` (lista de `QuickAccessItem` com `id`, `icon`, `label`, `action`) |
| `SECTION_HEADER_WITH_BADGE` | `SectionHeaderWithBadgeProperties` | `title`, `badgeCount`, `badgeBackgroundColor`, `action` |

#### Componentes de Deputados

| `type` | Classe de propriedades | Campos principais |
|---|---|---|
| `SEARCH_BAR` | `SearchBarProperties` | `placeholder` |
| `FILTER_SECTION` | `FilterSectionProperties` | `groups` (lista de `FilterGroup` com `label` e `chips`), `applyButtonLabel`, `clearButtonLabel` |
| `POLITICIAN_CARD_LIST` | `PoliticianCardListProperties` | `items` (lista de `PoliticianCardProperties`), `total`, `currentPage`, `totalPages` |

#### Componentes de Proposições

| `type` | Classe de propriedades | Campos principais |
|---|---|---|
| `PROPOSITION_CARD_LIST` | `PropositionCardListProperties` | `items` (lista de `PropositionCardProperties`), `total`, `currentPage`, `totalPages` |
| `PROPOSITION_DETAIL_HEADER` | `PropositionDetailHeaderProperties` | `title`, `presentationDate` |
| `DETAIL_SECTION` | `DetailSectionProperties` | `title`, `content` |
| `TEXT_LINK_SECTION` | `TextLinkSectionProperties` | `title`, `linkLabel`, `url` |
| `AUTHOR_CARD_LIST` | `AuthorCardListProperties` | `title`, `searchPlaceholder`, `items` (lista de `AuthorCardProperties`) |

#### Componentes de Despesas

| `type` | Classe de propriedades | Campos principais |
|---|---|---|
| `EXPENSE_CARD_LIST` | `ExpenseCardListProperties` | `items` (lista de `ExpenseCardProperties`), `total`, `currentPage`, `totalPages` |

#### `ComponentAction`

Todos os botões/navegação usam `ComponentAction`:
```json
{ "type": "NAVIGATE", "route": "/politicians/204379" }
```

---

### Como implementar um novo endpoint e tela SDUI

Siga os 5 passos abaixo usando o endpoint de deputados como referência.

#### Passo 1 — Definir o endpoint no `swagger.yaml`

Adicione o path dentro da seção `paths:`, com a tag `SDUI` e operationId único. Referencie sempre os 6 headers de contexto do cliente definidos em `components/parameters`:

```yaml
/api/v1/sdui/minha-tela:
  get:
    tags: [ SDUI ]
    summary: Get SDUI minha-tela screen definition
    operationId: getSduiMinhaTela          # nome do método gerado
    security:
      - bearerAuth: []
    parameters:
      - name: filtro
        in: query
        required: false
        schema:
          type: string
      # headers de contexto do cliente (obrigatório em todos os endpoints SDUI)
      - $ref: '#/components/parameters/XAppVersion'
      - $ref: '#/components/parameters/XAppPlatform'
      - $ref: '#/components/parameters/XOsVersion'
      - $ref: '#/components/parameters/XDeviceModel'
      - $ref: '#/components/parameters/XDeviceId'
      - $ref: '#/components/parameters/XAppLanguage'
    responses:
      '200':
        description: Minha tela SDUI definition
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/HomeScreenResponse'
      '401':
        description: Unauthorized
```

#### Passo 2 — Gerar as interfaces

```bash
mvn generate-sources
```

Isso cria (ou atualiza) `SduiApi` em `target/generated-sources/openapi/...` com o método `getSduiMinhaTela(...)`.

#### Passo 3 — Criar classes de propriedades

Para cada novo tipo de componente, crie um POJO em `src/main/java/br/com/deolhonacamara/sdui/model/properties/`:

```java
// src/.../sdui/model/properties/MinhaCardProperties.java
package br.com.deolhonacamara.sdui.model.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MinhaCardProperties {
    private Integer id;
    private String title;
    private String subtitle;
    private ComponentAction action;   // inclua se o componente for clicável
}
```

Use o padrão `*ListProperties` se o componente exibir uma coleção paginada:

```java
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MinhaCardListProperties {
    private List<MinhaCardProperties> items;
    private Integer total;
    private Integer currentPage;
    private Integer totalPages;
}
```

#### Passo 4 — Criar o service da tela

Crie em `src/main/java/br/com/deolhonacamara/sdui/service/`:

```java
// MinhaTelaScreenService.java
@Service
@RequiredArgsConstructor
@Log4j2
public class MinhaTelaScreenService {

    private static final String SCREEN_VERSION = "1.0";
    private final MeuServicoExistente meuServico;   // injete os services da camada api/

    public HomeScreenResponse buildMinhaTelaScreen(/* parâmetros */) {
        log.info("Building SDUI minha-tela screen");

        // 1. buscar dados via services existentes
        var dados = meuServico.getDados();

        // 2. mapear para propriedades de componente
        var items = dados.stream().map(d -> MinhaCardProperties.builder()
                .id(d.getId())
                .title(d.getTitulo())
                .action(ComponentAction.builder().type("NAVIGATE").route("/minha-tela/" + d.getId()).build())
                .build()).toList();

        // 3. montar resposta
        var response = new HomeScreenResponse();
        response.setScreenId("minha-tela");
        response.setVersion(SCREEN_VERSION);
        response.setComponents(List.of(
                component("minha-lista", "MINHA_CARD_LIST",
                        MinhaCardListProperties.builder().items(items).build())
        ));
        return response;
    }

    private ScreenComponent component(String id, String type, Object properties) {
        var comp = new ScreenComponent();
        comp.setId(id);
        comp.setType(type);
        comp.setProperties(properties);
        return comp;
    }
}
```

#### Passo 5 — Implementar o método no controller

Em `HomeScreenController.java`, adicione o campo injetado e sobrescreva o método gerado. Aceite os 6 headers de contexto do cliente e construa um `ClientInfo` para logging:

```java
private final MinhaTelaScreenService minhaTelaScreenService;

@Override
public ResponseEntity<HomeScreenResponse> getSduiMinhaTela(
        String filtro,
        String xAppVersion, String xAppPlatform, String xOSVersion,
        String xDeviceModel, String xDeviceId, String xAppLanguage) {
    var clientInfo = ClientInfo.of(xAppVersion, xAppPlatform, xOSVersion, xDeviceModel, xDeviceId, xAppLanguage);
    log.info("Fetching SDUI minha-tela screen [client={}]", clientInfo);
    var screen = minhaTelaScreenService.buildMinhaTelaScreen(filtro);
    return ResponseEntity.ok(screen);
}
```

> **Nota:** Use `jwtService.extractUserId(rawToken)` sempre que a tela precisar de contexto do usuário autenticado (como filtrar por seguidos). Veja `getSduiPoliticiansScreen` como referência.

#### Verificar

```bash
mvn compile   # deve compilar sem erros
mvn test      # todos os testes devem passar
```

---

## ⏰ Schedulers

O sistema possui vários schedulers que sincronizam dados da API da Câmara dos Deputados:

### `PoliticianSyncJob`
- **Horário:** 23:00 (Brasília)
- **Função:** Sincroniza a lista de todos os deputados
- **Frequência:** Diária

### `ExpenseSyncJob`
- **Horário:** 01:00 (Brasília)
- **Função:** Sincroniza despesas de todos os deputados
- **Frequência:** Diária

### `VoteSyncJob`
- **Horário:** 02:00 (Brasília)
- **Função:** Sincroniza votações e posicionamentos
- **Frequência:** Diária

### `SpeechSyncJob`
- **Horário:** 03:00 (Brasília)
- **Função:** Sincroniza discursos dos deputados
- **Frequência:** Diária

### `PropositionSyncJob`
- **Horário:** 04:00 (Brasília)
- **Função:** Sincroniza proposições legislativas
- **Frequência:** Diária

### `PresenceSyncJob`
- **Horário:** 05:00 (Brasília)
- **Função:** Sincroniza registros de presença
- **Frequência:** Diária

## ⚙️ Configuração

### Arquivo `application.properties`

```properties
spring.application.name=de-olho-na-camara-bff

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/abre-olho-camara-dev
spring.datasource.username=dev_user
spring.datasource.password=dev_pass
spring.datasource.driver-class-name=org.postgresql.Driver

# JWT
jwt.secret=supersecretkey
jwt.expiration.ms=604800000

# Domain
domain.url=https://seusite.com

# Câmara dos Deputados API
camara-deputados.api.base-url=https://dadosabertos.camara.leg.br/api/v2/
camara-deputados.api.timeout=10s

# Swagger
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.url=/v3/api-docs.yaml
```

### Variáveis de Ambiente

As configurações podem ser sobrescritas por variáveis de ambiente:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET`
- `JWT_EXPIRATION_MS` (padrão: 604800000 ms — 7 dias)
- `CAMARA_DEPUTADOS_API_BASE_URL`

## 🚀 Como Executar

### Pré-requisitos

- **Java 17** ou superior
- **Maven 3.8+**
- **PostgreSQL 12+**
- **Docker** (opcional)

### 1. Clonar o Repositório

```bash
git clone https://github.com/raphacbs/de-olho-na-camara-bff.git
cd de-olho-na-camara-bff
```

### 2. Configurar o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE abre_olho_camara_dev;
```

### 3. Configurar as Propriedades

Copie o arquivo de configuração local:

```bash
cp src/main/resources/application-local.properties src/main/resources/application.properties
```

Ajuste as configurações conforme necessário.

### 4. Executar Migrações do Liquibase

```bash
mvn liquibase:update -Plocal
```

### 5. Executar a Aplicação

#### Com Maven Wrapper:
```bash
./mvnw spring-boot:run
```

#### Com Maven:
```bash
mvn spring-boot:run
```

#### Com Docker (se disponível):
```bash
docker-compose up
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

### Swagger UI

Acesse a documentação interativa da API:

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs
- **OpenAPI YAML:** http://localhost:8080/v3/api-docs.yaml

### Especificação OpenAPI

A especificação completa da API está em:
```
src/main/resources/swagger.yaml
```

## 💻 Desenvolvimento

### Estrutura de Código

O projeto segue os seguintes padrões:

- **Controllers:** Implementam interfaces geradas pelo OpenAPI Generator
- **Services:** Contêm a lógica de negócio
- **Repositories:** Usam `NamedParameterJdbcTemplate` para acesso ao banco
- **Entities:** Classes de modelo com Lombok
- **DTOs:** Objetos de transferência de dados
- **Mappers:** Usam MapStruct para conversão Entity ↔ DTO

### Adicionando Novos Endpoints REST

1. Atualize o arquivo `swagger.yaml`
2. Execute `mvn compile` para gerar as interfaces
3. Crie o controller implementando a interface gerada
4. Crie o service com a lógica de negócio
5. Crie o repository se necessário
6. Atualize o mapper se necessário

### Adicionando Novos Endpoints e Telas SDUI

Consulte a seção **[Server-Driven UI (SDUI)](#-server-driven-ui-sdui)** deste README para o guia passo a passo completo.

### Executando Testes

```bash
mvn test
```

### Build do Projeto

```bash
mvn clean package
```

O arquivo JAR será gerado em: `target/de-olho-na-camara-bff-1.0.0.jar`

### Executando o JAR

```bash
java -jar target/de-olho-na-camara-bff-1.0.0.jar
```

## 🔐 Segurança

- Autenticação baseada em JWT
- Senhas armazenadas com hash (BCrypt)
- CORS configurado
- Validação de entrada
- Tratamento de exceções global

## 📊 Integração com API da Câmara

O serviço `CamaraDeputadosService` fornece métodos para acessar a API pública da Câmara dos Deputados:

- `getDeputados()` - Lista de deputados
- `getExpenses(politicianId, year, month)` - Despesas
- `getVotes(politicianId)` - Votações
- `getSpeeches(politicianId)` - Discursos
- `getPropositions(politicianId)` - Proposições
- `getPresence(politicianId)` - Presenças

## 🐛 Troubleshooting

### Erro de Conexão com Banco de Dados

Verifique se o PostgreSQL está rodando e as credenciais estão corretas.

### Erro ao Gerar Código do Swagger

Execute:
```bash
mvn clean compile
```

### Schedulers Não Executam

Verifique se o `@EnableScheduling` está habilitado na classe principal.

## 📝 Licença

Este projeto é de código aberto.

## 👥 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📧 Contato

- **Autor:** Raphael Coelho
- **Email:** contato@raphaelcoelho.dev
- **Repositório:** https://github.com/raphacbs/de-olho-na-camara-bff

---

**De Olho na Câmara** - Monitorando nossos representantes! 👁️🏛️
