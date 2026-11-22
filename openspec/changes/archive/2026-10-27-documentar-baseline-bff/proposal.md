## Why

O BFF **De Olho na Câmara** já implementa um conjunto amplo de APIs, jobs de sincronização e telas SDUI, mas não havia especificações formais no OpenSpec. Registrar o baseline permite alinhar time, agentes de IA e o app mobile sobre comportamento esperado, facilitar mudanças futuras com deltas rastreáveis e usar cenários como base de testes.

## What Changes

- Documentação apenas: nenhuma alteração de código ou contrato HTTP nesta change.
- Criação de specs de capacidade descrevendo o que o sistema **já faz** hoje (requisitos com cenários WHEN/THEN).
- Referência à arquitetura existente (Spring Boot, PostgreSQL, API da Câmara, JWT) em `design.md`.
- Após revisão, sincronizar deltas para `openspec/specs/` (specs principais do repositório).

## Capabilities

### New Capabilities

- `auth`: registro, login e tokens JWT; rotas públicas vs autenticadas.
- `politicians`: listagem e detalhe de deputados; seguir e deixar de seguir políticos.
- `legislative-data`: despesas, votos, discursos, proposições, tramitação, presença, partidos e agregados de votação.
- `data-sync`: sincronização manual via API e jobs agendados com a API Dados Abertos da Câmara.
- `dashboard`: estatísticas agregadas para o painel do usuário autenticado.
- `push-devices`: registro de dispositivos FCM para notificações.
- `sdui`: endpoints que retornam definições de tela (Server-Driven UI) para o app mobile.

### Modified Capabilities

- (nenhuma — não existiam specs principais antes desta change)

## Impact

- **Documentação**: `openspec/specs/*` e artefatos em `openspec/changes/documentar-baseline-bff/`.
- **Código**: nenhum impacto em runtime.
- **API**: fonte de verdade operacional continua sendo `src/main/resources/swagger.yaml`; as specs devem permanecer alinhadas a ela em mudanças futuras.
