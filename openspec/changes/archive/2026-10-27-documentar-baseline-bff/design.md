## Contexto

BFF em **Java 17 / Spring Boot 3.x** que expõe REST sob `/api/v1`, persiste dados em **PostgreSQL** (schemas `authentication` e `camara_deputados`, Liquibase) e integra a **API Dados Abertos da Câmara dos Deputados**. Controllers implementam interfaces geradas a partir do **OpenAPI** (`swagger.yaml`). Autenticação via **JWT** (filtro `JwtAuthenticationFilter`); senhas com BCrypt.

## Decisões (estado atual)

| Área | Decisão |
|------|---------|
| Camadas | Controller → Service → Repository; cliente HTTP para API externa |
| Contrato HTTP | OpenAPI 3 + codegen; alterações de superfície exigem atualizar `swagger.yaml` |
| Dados externos | Sync incremental/paginado; jobs `@Scheduled` (fuso `America/Recife`); tramitação com retomada via `SyncProgressService` |
| SDUI | Respostas com `screenId`, `version` e `components[]` tipados; cliente envia headers de contexto (`XAppVersion`, `XAppPlatform`, etc.) |
| Segurança | `register` e `login` públicos; demais rotas exigem Bearer token |

## Fluxo de dados (visão geral)

```
App mobile ──REST/JWT──▶ BFF ──JDBC──▶ PostgreSQL
                            │
                            └──HTTP──▶ API Câmara (dadosabertos)
```

## Jobs agendados (referência)

| Job | Cron (aprox.) | Domínio |
|-----|----------------|---------|
| ExpenseSyncJob | 01:00 | despesas |
| VoteSyncJob | 02:00 | votações |
| SpeechSyncJob | 03:00 | discursos |
| PropositionSyncJob | 04:00 | proposições |
| PropositionTramitationSyncJob | 05:00 | tramitações |
| RecentPropositionSyncJob | 06:00 | proposições recentes |
| PoliticianSyncJob | 23:00 | deputados |
| PresenceSyncJob | (comentado) | presença |

## Riscos / lacunas conhecidas

- Specs baseline podem não cobrir todos os query params e códigos de erro do Swagger; evoluir via deltas em changes futuras.
- Endpoints de sync manual provavelmente exigem autenticação — validar política de autorização (admin vs usuário) antes de expor em produção.
- `PresenceSyncJob` desabilitado: dados de presença podem estar desatualizados até reativar o job.

## Não-objetivos desta change

- Implementar features novas.
- Substituir o Swagger como contrato machine-readable; OpenSpec complementa com requisitos comportamentais.
