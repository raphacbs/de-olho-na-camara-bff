## 1. Modelos da resposta

- [x] 1.1 Fazer `VotingRepository.findVotingsWithVotes` devolver `net.coelho.deolhonacamara.api.model.VotingWithVotesResponseDTO` e os itens gerados, mantendo a query, a ordenação por data e os filtros atuais
- [x] 1.2 Fazer `PoliticianVoteRepository.findVotesWithPropositionByPoliticianId` devolver o `PoliticianVoteWithPropositionResponseDTO` gerado
- [x] 1.3 Ajustar `VotingService` para repassar esses tipos, com `page` nulo como 0 e `size` nulo como 20, sem subtrair 1
- [x] 1.4 Remover os DTOs manuais de votação e de voto com proposição em `br.com.deolhonacamara.api.dto` que ficarem sem uso

## 2. Handler único

- [x] 2.1 Sobrescrever `getVotingsWithVotes` e `getPoliticianVotesWithProposition` em `VoteController`, delegando ao `VotingService` e devolvendo HTTP 200
- [x] 2.2 Remover `VotingController`

## 3. Generator

- [x] 3.1 Trocar `skipDefaultInterfaceMethods` por `skipDefaultInterface=true` no `openapi-generator-maven-plugin`
- [x] 3.2 Rodar `mvn generate-sources` e completar qualquer outro controller cuja interface tenha ficado sem override

## 4. Verificação

- [x] 4.1 Cobrir com teste que `page=0` e `page=1` consultam offsets distintos e que a listagem vazia devolve total 0
- [x] 4.2 Rodar `mvn -DskipTests=false test` e corrigir falhas
