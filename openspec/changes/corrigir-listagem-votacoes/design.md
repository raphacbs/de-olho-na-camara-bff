## Context

A aba Votações do app (`VotesScreen`) chama `GET /api/v1/votings-with-votes?page=0&size=20`. O cliente mostra `HTTP 501` quando o corpo vem vazio.

`VotesApi`, gerada a partir de `swagger.yaml`, declara três operações. `VoteController` só sobrescreve `getPoliticianVotes`. Os outros dois métodos ficam com o default do OpenAPI Generator 7.13, que devolve `HttpStatus.NOT_IMPLEMENTED`. O `pom.xml` passa `skipDefaultInterfaceMethods`, opção que esse generator ignora. A opção efetiva é `skipDefaultInterface`.

`VotingController` repete os mesmos paths e chama `VotingService`, que já consulta `camara_deputados.voting` e `politician_vote`. Esse controller não declara `produces`. O método gerado declara `produces = application/json`. Com `Accept: application/json`, o Spring escolhe o stub.

Os DTOs da consulta manual estão em `br.com.deolhonacamara.api.dto`. A interface exige `net.coelho.deolhonacamara.api.model`. O JSON dos dois lados tem os mesmos campos. O tipo Java não é intercambiável.

`VotingController` converte `page > 0` para base 1 (`page - 1`). O app e `getPoliticianVotes` usam base 0. Página 0 funciona. Página 1 voltaria a consultar o primeiro bloco.

## Goals / Non-Goals

**Goals:**

- `GET /api/v1/votings-with-votes` e `GET /api/v1/politicians/{id}/votes-with-proposition` respondem 200 pela implementação de `VotesApi`.
- Um único mapeamento por path.
- `page` em base 0, igual ao restante da API que o app consome.
- O próximo método de interface sem override quebra o build.

**Non-Goals:**

- Tela SDUI de votações (`GET /api/v1/sdui/votings` não existe no contrato atual).
- Mudança no app. `VotesScreen` já renderiza data, descrição e órgão.
- Enxugar o payload. A query continua anexando os votos de cada deputado da página.
- Novo sync ou mudança de schema.

## Decisions

### Um handler em `VoteController`

`VoteController` sobrescreve `getVotingsWithVotes` e `getPoliticianVotesWithProposition` e delega a `VotingService`. `VotingController` sai, porque só contém esses dois mapeamentos.

Alternativa considerada: manter `VotingController` e apagar os defaults da interface. O generator recria os defaults a cada `generate-sources`, e o conflito de `produces` volta no próximo build.

### Serviço devolve o modelo gerado

`VotingRepository` e `PoliticianVoteRepository` passam a montar `net.coelho.deolhonacamara.api.model.VotingWithVotesResponseDTO` e `PoliticianVoteWithPropositionResponseDTO` (e os itens aninhados correspondentes). `VotingService` repassa esses tipos. Os DTOs manuais em `br.com.deolhonacamara.api.dto` que só serviam a esses dois fluxos são removidos.

Alternativa considerada: mapear no controller entre os dois pacotes. Funciona, e deixa dois modelos para o mesmo JSON.

`page` e `size` seguem para o `PageRequest` sem subtrair 1. `page` nulo vira 0. `size` nulo vira 20. É o mesmo critério de `getPoliticianVotes`.

### Generator sem método default

Trocar `skipDefaultInterfaceMethods` por `skipDefaultInterface=true` no `openapi-generator-maven-plugin`. Com isso, `VotesApi` deixa de compilar se algum método ficar sem override. Depois da troca, o build precisa passar: qualquer outro controller incompleto entra no mesmo ajuste.

## Risks / Trade-offs

- [Payload grande: 20 votações de plenário trazem milhares de votos que a aba não desenha] → Aceito nesta change. O contrato já descreve `votes`. Se a abertura ficar lenta, uma listagem sem o array é outra change.
- [Tabela `voting` vazia] → A resposta é 200 com `data` vazio e `total` 0. O app deixa de mostrar erro. A carga continua no `VoteSyncJob`.
- [`skipDefaultInterface` revela outro método sem override] → Corrigir no mesmo build, no controller que implementa aquela interface. Sem inventar endpoint novo.
- [Dois tipos com o mesmo nome simples] → Imports explícitos do pacote gerado no repositório, no serviço e no controller. Remover os DTOs manuais evita o choque.

## Migration Plan

1. Ajustar repositório, serviço e `VoteController`.
2. Remover `VotingController` e os DTOs manuais sem outro uso.
3. Trocar a flag do generator e rodar `mvn -DskipTests=false test`.
4. Deploy só do BFF. O app em produção passa a listar votações sem release.

Rollback: reverter o deploy do BFF. O app não muda.

## Open Questions

Nenhuma. O escopo é o 501 e a página em base 0.
