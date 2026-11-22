## ADDED Requirements

### Requirement: Listagem paginada de deputados

O sistema SHALL expor `GET /api/v1/politicians` com paginação (`page`, `size`) e filtros opcionais por nome, partido (sigla) e UF, retornando dados sincronizados da Câmara.

#### Scenario: Lista com filtros

- **WHEN** o cliente autenticado solicita a lista com `party` e `state`
- **THEN** o sistema retorna apenas deputados que atendem aos filtros, paginados

### Requirement: Detalhe de deputado

O sistema SHALL expor `GET /api/v1/politicians/{id}` retornando os dados do deputado identificado pelo id interno ou identificador definido no contrato OpenAPI.

#### Scenario: Deputado existente

- **WHEN** o id corresponde a um deputado persistido
- **THEN** o sistema retorna o detalhe completo conforme `PoliticianDto`

#### Scenario: Deputado inexistente

- **WHEN** o id não existe
- **THEN** o sistema responde com erro apropriado (ex.: 404) conforme implementação

### Requirement: Listar políticos seguidos

O sistema SHALL expor `GET /api/v1/followed` para o usuário autenticado, listando deputados associados em `user_followed_politicians`, com paginação.

#### Scenario: Usuário com seguidos

- **WHEN** o usuário possui vínculos de follow
- **THEN** o sistema retorna a lista paginada de políticos seguidos

### Requirement: Seguir deputado

O sistema SHALL expor `POST /api/v1/followed/{politicianId}` criando vínculo entre usuário autenticado e deputado, sem duplicar o relacionamento.

#### Scenario: Primeiro follow

- **WHEN** o usuário segue um deputado válido que ainda não segue
- **THEN** o sistema persiste o vínculo e confirma sucesso

### Requirement: Deixar de seguir deputado

O sistema SHALL expor `DELETE /api/v1/followed/{politicianId}` removendo o vínculo de follow do usuário autenticado.

#### Scenario: Unfollow

- **WHEN** o usuário deixa de seguir um deputado que seguia
- **THEN** o sistema remove o registro de follow
