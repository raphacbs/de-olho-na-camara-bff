## Why

A aba Votações do app chama `GET /api/v1/votings-with-votes` e recebe HTTP 501. O OpenAPI gera um método default em `VotesApi` que responde `NOT_IMPLEMENTED`, e o Spring escolhe esse mapeamento em vez da consulta que já existe. A listagem de votações fica inacessível mesmo com dados sincronizados.

## What Changes

- Passar `GET /api/v1/votings-with-votes` e `GET /api/v1/politicians/{id}/votes-with-proposition` a responder 200 pelo `VoteController`, que implementa `VotesApi`.
- Remover os mapeamentos duplicados de `VotingController`, para um único handler por path.
- Tratar `page` como base 0, no mesmo critério dos outros endpoints de lista que o app já envia.
- Fazer o gerador OpenAPI falhar o build quando um método da interface ficar sem override, em vez de publicar um stub 501.

## Capabilities

### New Capabilities

- Nenhuma.

### Modified Capabilities

- `legislative-data`: os dois endpoints de votação passam a ser servidos pela implementação de `VotesApi`, com paginação em base 0 e corpo `VotingWithVotesResponseDTO` / `PoliticianVoteWithPropositionResponseDTO`. O comportamento observável deixa de ser HTTP 501.

## Impact

- BFF: `VoteController`, `VotingController`, `VotingService` e o retorno dos repositórios que hoje usam DTOs manuais em `br.com.deolhonacamara.api.dto`, porque a interface gerada exige os modelos em `net.coelho.deolhonacamara.api.model`.
- `pom.xml`: opção do openapi-generator (`skipDefaultInterface`).
- Contrato HTTP permanece o do `swagger.yaml`. Sem mudança de path, query params ou formato JSON.
- App `deputados-sdui-app`: a aba nativa `VotesScreen` já chama o endpoint e renderiza data, descrição e órgão. Nenhuma change nesse repositório.
- Carga: a query atual anexa o voto de cada deputado em cada votação da página. A aba não exibe esse array. Esta change entrega o contrato existente. Enxugar o payload fica fora do escopo.
