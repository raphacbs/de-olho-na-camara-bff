# data-sync

## Purpose

Ingestão e atualização de dados da API Dados Abertos da Câmara via jobs agendados e endpoints manuais de sincronização.

## Requirements

### Requirement: Sincronização manual por domínio

O sistema SHALL expor endpoints autenticados sob `/api/v1/sync/*` para disparar sincronização sob demanda dos domínios: todos (`/sync/all`), políticos, despesas, votos, discursos, proposições, presença e tramitações, conforme `SyncController` e OpenAPI.

#### Scenario: Sync de políticos

- **WHEN** um cliente autenticado chama `POST /api/v1/sync/politicians`
- **THEN** o sistema inicia o processo de sincronização de deputados com a API da Câmara e retorna confirmação conforme contrato

#### Scenario: Sync completo

- **WHEN** o cliente chama `POST /api/v1/sync/all`
- **THEN** o sistema dispara a sequência de sincronizações configurada para todos os domínios suportados

### Requirement: Sincronização agendada diária

O sistema SHALL executar jobs Spring `@Scheduled` para manter o banco atualizado com a API Dados Abertos, respeitando o fuso `America/Recife` e a ordem cron configurada nos jobs (despesas, votos, discursos, proposições, tramitações, proposições recentes, políticos).

#### Scenario: Job de despesas

- **WHEN** o cron de `ExpenseSyncJob` dispara
- **THEN** o sistema busca e persiste despesas dos deputados de forma incremental ou paginada conforme implementação do serviço

### Requirement: Retomada de sync de tramitação

O sistema SHALL persistir progresso de sincronização de tramitações via `SyncProgressService` / `SyncProgressEntity` para permitir retomada após interrupção, honrando `proposition.sync.chunk-size` de `PropertiesConfig`.

#### Scenario: Interrupção e retomada

- **WHEN** um job de tramitação é interrompido após processar parte das proposições
- **THEN** na próxima execução o sistema continua a partir de `last_proposition_id` e, se houver, `last_tramitacao_id`

### Requirement: Acompanhamento estável do progresso

O sistema SHALL gravar cada execução em `camara_deputados.sync_progress` com `current_page`, `total_pages`, `processed_count`, `error_count` e `last_cursor`. `total_pages` SHALL ser definido uma vez na execução e MUST NOT ser substituído por um total de outro significado. `current_page` SHALL representar a unidade já concluída daquele fluxo (página da API ou deputados processados). Trabalho paralelo interno SHALL usar um executor diferente da thread que conduz o job.

#### Scenario: Deputados e páginas não se misturam

- **WHEN** a sincronização de proposições avança do deputado 10 para o 11
- **THEN** `current_page` passa a 11, `total_pages` permanece a quantidade de deputados e `last_cursor` recebe o id do deputado concluído

#### Scenario: Job não espera a própria fila

- **WHEN** um job dispara gravações paralelas de votos ou proposições
- **THEN** essas tarefas rodam em `syncWorkerExecutor`, e a espera tem limite de 5 minutos

### Requirement: Fonte externa única para dados legislativos

O sistema SHALL obter dados legislativos exclusivamente da API pública da Câmara dos Deputados (base URL configurável, ex.: `camara-deputados.api.base-url`), mapeando para entidades locais antes de servir as APIs REST.

#### Scenario: Integração com API externa

- **WHEN** um job ou endpoint de sync executa
- **THEN** o `CamaraDeputadosService` (ou equivalente) consome a API externa e grava no PostgreSQL
