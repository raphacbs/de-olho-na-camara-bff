# dashboard

## Purpose

Métricas agregadas para o painel do usuário autenticado no app.

## Requirements

### Requirement: Estatísticas do painel

O sistema SHALL expor `GET /api/v1/dashboard/stats` para usuário autenticado, retornando agregados sobre políticos, proposições, despesas, votos e políticos seguidos, com filtro opcional de ano (`ano`; padrão ano corrente).

#### Scenario: Stats do ano corrente

- **WHEN** o cliente autenticado chama o endpoint sem `ano`
- **THEN** o sistema retorna `DashboardStatsDto` calculado para o ano atual

#### Scenario: Stats de ano específico

- **WHEN** o cliente informa `ano` na query
- **THEN** o sistema filtra ou calcula métricas referentes a esse ano

### Requirement: Identificação do usuário no dashboard

O sistema SHALL resolver o usuário a partir do JWT (header Authorization ou contexto de segurança) para personalizar métricas relacionadas a políticos seguidos.

#### Scenario: Token válido

- **WHEN** o Bearer token é válido
- **THEN** o dashboard inclui informações pertinentes ao usuário autenticado
