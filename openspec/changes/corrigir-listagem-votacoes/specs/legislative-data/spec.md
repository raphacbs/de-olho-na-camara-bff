## MODIFIED Requirements

### Requirement: Votos enriquecidos com proposição

O sistema SHALL expor `GET /api/v1/politicians/{id}/votes-with-proposition` pela implementação de `VotesApi`, agregando contexto da proposição relacionada à votação, conforme contrato OpenAPI. A resposta SHALL ser HTTP 200 com `PoliticianVoteWithPropositionResponseDTO`. O parâmetro `page` SHALL ser base 0, sem conversão para base 1.

#### Scenario: Voto com contexto legislativo

- **WHEN** existem votos com proposição associada para o político informado
- **THEN** o sistema retorna HTTP 200 com itens que trazem dados de votação e proposição

#### Scenario: Página em base 0

- **WHEN** o cliente envia `page=0` e `size=20`
- **THEN** o sistema devolve o primeiro bloco de resultados
- **AND** uma chamada seguinte com `page=1` devolve o bloco seguinte, sem repetir o primeiro

#### Scenario: Político sem votos enriquecidos

- **WHEN** não há votos com proposição para o político informado
- **THEN** o sistema retorna HTTP 200 com lista vazia e total zero

### Requirement: Votações com votos

O sistema SHALL expor `GET /api/v1/votings-with-votes` pela implementação de `VotesApi`, para consulta paginada de votações com respectivos votos, conforme OpenAPI. A resposta SHALL ser HTTP 200 com `VotingWithVotesResponseDTO`. O parâmetro `page` SHALL ser base 0, sem conversão para base 1. Cada item SHALL incluir `id`, `date`, `description`, `organAcronym` e a lista `votes`.

#### Scenario: Listagem de votações

- **WHEN** o cliente autenticado solicita a listagem com `page` e `size`
- **THEN** o sistema retorna HTTP 200 e `VotingWithVotesResponseDTO` com as votações da página, ordenadas da mais recente para a mais antiga

#### Scenario: Primeira página do app

- **WHEN** o cliente envia `page=0` e `size=20`
- **THEN** o sistema devolve até 20 votações a partir do início da ordenação
- **AND** `page` na resposta é 0

#### Scenario: Página seguinte

- **WHEN** o cliente envia `page=1` depois de ter recebido a página 0
- **THEN** o sistema devolve o bloco seguinte
- **AND** os ids desse bloco não repetem os da página 0

#### Scenario: Nenhuma votação carregada

- **WHEN** não há registros em `camara_deputados.voting` para os filtros informados
- **THEN** o sistema retorna HTTP 200 com `data` vazio, `total` 0 e `totalPages` 0
