Plan: Separar job de tramitações

TL;DR: Criaremos um novo job `PropositionTramitationSyncJob` que roda diariamente e, para cada proposição existente no banco, busca tramitações filtradas por `dataInicio` e `dataFim` (formato YYYY-MM-DD — preenchidos com a data do dia atual), persistindo os resultados via `PropositionTramitationService`. Removeremos a busca de tramitações do `PropositionSyncJob` para isolar responsabilidades.

### Steps
1. Criar `src/main/java/br/com/deolhonacamara/scheduler/PropositionTramitationSyncJob.java` — componente agendado (`@Component`) com método `@Scheduled` diário; injetar `PropositionRepository`, `PropositionTramitationService`, `CamaraDeputadosService` e `SyncProgressService` (opcional).
2. Implementar no novo job: obter lista/página de proposições (`propositionRepository.findAll(...)`), para cada proposição:
   - montar `dataInicio` e `dataFim` como String `YYYY-MM-DD` usando LocalDate.now();
   - chamar `camaraDeputadosService.getTramitacoesByPropositionId(propositionId, dataInicio, dataFim)`; 
   - converter resposta em `PropositionTramitationEntity` e chamar `propositionTramitationService.upsertTramitationEntities(...)`.
3. Adicionar/alterar assinatura em `src/main/java/br/com/deolhonacamara/api/service/CamaraDeputadosService.java`:
   - adicionar novo método `getTramitacoesByPropositionId(Integer propositionId, String dataInicio, String dataFim)` (ou sobrecarga);
   - compor endpoint com query params `?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` e usar o mesmo mecanismo HTTP existente para buscar.
4. Remover do `src/main/java/br/com/deolhonacamara/scheduler/PropositionSyncJob.java` a lógica que, após salvar uma proposição, chamava `camaraDeputadosService.getTramitacoesByPropositionId(...)` e persistia `propositionTramitationService.upsertTramitationEntities(...)`.
5. Tratar erros e casos de borda no novo job: exceptions por proposição não devem parar o processamento; logar falhas; pular quando resposta vazia; considerar paginação/limites se API retornar muitas tramitações.
6. Atualizar imports e adicionar logs informativos (início, progresso, erros, resumo). Opcional: adicionar `SyncProgress` similar ao outro job para monitoramento por execução.

### Further Considerations
1. API external pode não suportar `dataInicio` / `dataFim` — opção A: confirmar endpoint externo e parâmetros; opção (Já existe o endpoint no projeto, apenas adione o filtro dos parâmetros que são do tipo query)
2. Performance: se houver muitas proposições, usar paginação (`PageRequest`) e evitar carregar tudo em memória; considerar paralelismo controlado (executor) se necessário. As preposições serão carregadas do repository  
3. Timezone e horário: usar LocalDate na timezone esperada (America/Sao_Paulo) para gerar `YYYY-MM-DD`; definir cron no horário desejado.

### Assumptions
- Nome do novo plano/arquivo: `plan-separatePropositionTramitationJob.prompt.md` (camelCase escolhido: `separatePropositionTramitationJob`).
- O endpoint da Câmara aceita `dataInicio`/`dataFim` como query params no formato `YYYY-MM-DD`. Se não aceitar, a estratégia será filtrar localmente.

### Next steps (opcional)
- Implementar `PropositionTramitationSyncJob` e a sobrecarga em `CamaraDeputadosService` seguindo o plano acima.
- Remover a lógica de tramitações de `PropositionSyncJob` e rodar testes locais (build/mvn test) e validação rápida (compilação).

---
Saved plan for further refinement.

