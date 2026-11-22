## ADDED Requirements

### Requirement: Despesas do deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}/expenses` com paginação e filtros opcionais de ano e mês, retornando despesas sincronizadas (`politician_expense`).

#### Scenario: Consulta por ano

- **WHEN** o cliente informa `year` na query
- **THEN** o sistema retorna despesas daquele ano para o deputado

### Requirement: Votos do deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}/votes` com paginação, retornando posicionamentos do deputado em votações.

#### Scenario: Lista de votos

- **WHEN** o cliente solicita a primeira página de votos
- **THEN** o sistema retorna votos paginados do deputado

### Requirement: Discursos do deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}/speeches` com paginação.

#### Scenario: Lista de discursos

- **WHEN** o cliente solicita discursos de um deputado existente
- **THEN** o sistema retorna registros de `speech` paginados

### Requirement: Proposições do deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}/propositions` com paginação, relacionando autorias em `politician_proposition`.

#### Scenario: Proposições por autor

- **WHEN** o deputado é autor de proposições persistidas
- **THEN** o sistema lista essas proposições paginadas

### Requirement: Catálogo de proposições

O sistema SHALL expor `GET /api/v1/propositions` com paginação e filtros conforme OpenAPI para busca global de proposições.

#### Scenario: Busca global

- **WHEN** o cliente aplica filtros suportados na listagem
- **THEN** o sistema retorna proposições correspondentes

### Requirement: Tramitação de proposição

O sistema SHALL expor `GET /api/v1/propositions/{id}/tramitation` (histórico) e `GET /api/v1/propositions/{id}/tramitation/last` (último andamento).

#### Scenario: Última tramitação

- **WHEN** existe tramitação para a proposição
- **THEN** o endpoint `tramitation/last` retorna o andamento mais recente

### Requirement: Presença do deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}/presence` com paginação sobre registros de presença.

#### Scenario: Registros de presença

- **WHEN** há dados de presença persistidos para o deputado
- **THEN** o sistema retorna a lista paginada

### Requirement: Partidos políticos

O sistema SHALL expor `GET /api/v1/parties` retornando a lista de partidos disponíveis para filtros e exibição.

#### Scenario: Listar partidos

- **WHEN** o cliente autenticado chama o endpoint
- **THEN** o sistema retorna array de partidos conforme `PartyDto`

### Requirement: Votos enriquecidos com proposição

O sistema SHALL expor `GET /api/v1/politicians/{id}/votes-with-proposition` agregando contexto da proposição relacionada à votação, conforme contrato OpenAPI.

#### Scenario: Voto com contexto legislativo

- **WHEN** existem votos com proposição associada
- **THEN** o sistema retorna itens com dados de votação e proposição

### Requirement: Votações com votos

O sistema SHALL expor `GET /api/v1/votings-with-votes` para consulta paginada de votações com respectivos votos, conforme OpenAPI.

#### Scenario: Listagem de votações

- **WHEN** o cliente solicita a listagem com parâmetros de paginação
- **THEN** o sistema retorna `VotingWithVotesResponseDTO` conforme especificado
