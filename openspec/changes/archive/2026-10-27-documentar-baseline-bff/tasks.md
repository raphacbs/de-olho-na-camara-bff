## 1. Revisão

- [x] 1.1 Revisar `proposal.md` e lista de capacidades com o time
- [x] 1.2 Conferir requisitos contra `src/main/resources/swagger.yaml` e README

## 2. Publicar specs principais

- [x] 2.1 Sincronizar deltas desta change para `openspec/specs/<capability>/spec.md` (comando `/opsx:sync` ou fluxo openspec-sync-specs)
- [x] 2.2 Executar `openspec validate` nas specs principais

## 3. Encerramento

- [x] 3.1 Arquivar a change com `/opsx:archive` após aprovação (opcional: manter change aberta como rascunho até revisão)
- [x] 3.2 Em features novas, usar changes com deltas em vez de editar specs principais diretamente

## 4. Melhorias futuras (opcional)

- [ ] 4.1 Adicionar cenários de erro (404, validação) por endpoint crítico
- [ ] 4.2 Documentar requisitos de autorização para endpoints `/sync/*`
- [ ] 4.3 Atualizar `openspec/config.yaml` com contexto de domínio (schemas DB, convenções de pacote)
