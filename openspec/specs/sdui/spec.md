# sdui

## Purpose

Server-Driven UI: o backend entrega definições de tela para o app mobile renderizar componentes tipados.

## Requirements

### Requirement: Envelope de tela SDUI

O sistema SHALL retornar respostas SDUI com identificador de tela (`screenId`), `version` e lista ordenada de `components`, cada um com `id`, `type` e `properties` tipadas no pacote `sdui`, permitindo renderização no app sem hardcode de layout.

#### Scenario: Estrutura mínima

- **WHEN** qualquer endpoint SDUI responde 200
- **THEN** o JSON contém `screenId`, `version` e `components` não vazio ou vazio conforme estado da tela

### Requirement: Tela inicial SDUI

O sistema SHALL expor `GET /api/v1/sdui/home` (autenticado) montando seções como estatísticas, atalhos e políticos seguidos, com filtro opcional `ano` e leitura de headers de contexto do cliente (`XAppVersion`, `XAppPlatform`, `XOsVersion`, `XDeviceModel`, `XDeviceId`, `XAppLanguage`).

#### Scenario: Home autenticada

- **WHEN** o cliente envia Bearer token e headers de contexto
- **THEN** o sistema retorna `HomeScreenResponse` personalizado quando aplicável

### Requirement: Lista de políticos SDUI

O sistema SHALL expor `GET /api/v1/sdui/politicians` com filtros opcionais (`name`, `party`, `state`, paginação) retornando componentes para listagem de deputados.

#### Scenario: Filtro por partido

- **WHEN** o cliente informa `party` na query
- **THEN** a definição de tela reflete apenas deputados do partido

### Requirement: Proposições SDUI

O sistema SHALL expor `GET /api/v1/sdui/propositions` e `GET /api/v1/sdui/propositions/{id}` para listagem e detalhe de proposições em formato SDUI.

#### Scenario: Detalhe de proposição

- **WHEN** o id da proposição existe
- **THEN** o endpoint de detalhe retorna componentes descrevendo a proposição e tramitação relevante

### Requirement: Despesas SDUI por político

O sistema SHALL expor `GET /api/v1/sdui/politicians/{id}/expenses` retornando componentes para visualização de despesas do deputado.

#### Scenario: Tela de despesas

- **WHEN** o deputado possui despesas sincronizadas
- **THEN** a resposta SDUI inclui componentes com dados de despesas conforme propriedades definidas no backend

### Requirement: Não autorizado em SDUI

O sistema SHALL responder `401` nos endpoints SDUI protegidos quando o JWT estiver ausente ou inválido.

#### Scenario: Home sem token

- **WHEN** o cliente chama `/api/v1/sdui/home` sem Authorization
- **THEN** o sistema retorna 401 conforme OpenAPI
