# Verificação de Endpoints - CamaraDeputadosService

## Endpoints Verificados

### ✅ `/deputados`
- **Status**: EXISTE
- **Descrição**: Lista todos os deputados
- **Método**: GET
- **Documentação**: https://dadosabertos.camara.leg.br/swagger/api.html

### ✅ `/deputados/{id}/despesas`
- **Status**: EXISTE
- **Descrição**: Retorna as despesas de um deputado específico
- **Método**: GET
- **Parâmetros**: `ano` (opcional), `mes` (opcional)
- **Documentação**: https://dadosabertos.camara.leg.br/swagger/api.html

### ✅ `/deputados/{id}/votacoes`
- **Status**: EXISTE
- **Descrição**: Retorna as votações de um deputado específico
- **Método**: GET
- **Documentação**: https://dadosabertos.camara.leg.br/swagger/api.html

### ✅ `/deputados/{id}/discursos`
- **Status**: EXISTE
- **Descrição**: Retorna os discursos de um deputado específico
- **Método**: GET
- **Documentação**: https://dadosabertos.camara.leg.br/swagger/api.html

### ✅ `/deputados/{id}/proposicoes`
- **Status**: EXISTE
- **Descrição**: Retorna as proposições de um deputado específico
- **Método**: GET
- **Documentação**: https://dadosabertos.camara.leg.br/swagger/api.html

### ❌ `/deputados/{id}/presencas`
- **Status**: NÃO EXISTE
- **Descrição**: Este endpoint não existe na API REST v2
- **Alternativa**: 
  - Web service SOAP: `ListarPresencasParlamentar` ou `ListarPresencasDia`
  - Endpoint de eventos: `/eventos/{id}/presencas` (requer ID do evento)
  - Possível alternativa: `/deputados/{id}/frequencias` (precisa verificar)

### ❓ `/deputados/{id}/frequencias`
- **Status**: PRECISA VERIFICAR
- **Descrição**: Possível alternativa para presenças/frequências
- **Nota**: Mencionado em algumas buscas, mas precisa confirmação oficial

## Resumo

- **Total de endpoints verificados**: 6
- **Endpoints válidos**: 5
- **Endpoints inválidos**: 1 (presencas)
- **Endpoints a verificar**: 1 (frequencias)

## Ações Recomendadas

1. ✅ Manter os 5 endpoints válidos
2. ✅ Manter o método `getPresence()` como `@Deprecated` (já implementado)
3. ❓ Verificar se `/deputados/{id}/frequencias` existe e pode ser usado como alternativa

