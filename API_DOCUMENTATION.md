# Documentação Técnica da API - De Olho na Câmara BFF

## Visão Geral

Esta documentação técnica descreve os endpoints da API BFF (Backend For Frontend) para o aplicativo "De Olho na Câmara". A API utiliza arquitetura Server-Driven UI (SDUI) para fornecer componentes dinâmicos para o frontend mobile.

## Base URL
```
http://localhost:8080/api/v1
https://api.deolhonacamara.com/api/v1
```

## Autenticação
Todos os endpoints SDUI requerem autenticação via header `Authorization` com token JWT:
```
Authorization: Bearer <jwt_token>
```

---

## 1. ENDPOINTS SDUI

### 1.1 GET /api/v1/sdui/home
Retorna a tela inicial (Home) com componentes dinâmicos.

**Parâmetros:** Nenhum (além do header Authorization)

**Resposta:** `SDUIResponse`
```json
{
  "id": "home",
  "title": "De Olho na Câmara",
  "navigation": {...},
  "metadata": {...},
  "components": [...]
}
```

---

### 1.2 GET /api/v1/sdui/deputados
Retorna a tela de deputados com filtros e listagem.

**Parâmetros de Query:**
- `search` (string, opcional): Termo de busca por nome do deputado
- `uf` (string, opcional): Sigla do estado para filtrar deputados
- `page` (integer, opcional, padrão=0): Número da página
- `size` (integer, opcional, padrão=20): Tamanho da página

**Exemplos:**
```
GET /api/v1/sdui/deputados?search=Lula&uf=SP&page=0&size=20
GET /api/v1/sdui/deputados?uf=RJ
```

---

### 1.3 GET /api/v1/sdui/proposicoes
Retorna a tela de proposições com filtros avançados e listagem paginada.

**Parâmetros de Query:**
- `tipo` (string, opcional): Código do tipo de proposição (ver seção 2.1)
- `status` (string, opcional): Status da tramitação (busca parcial)
- `periodo` (string, opcional): Período pré-definido (ver seção 2.2)
- `politico` (string, opcional): Nome do político (busca parcial)
- `dataInicio` (string, opcional): Data inicial no formato YYYY-MM-DD
- `dataFim` (string, opcional): Data final no formato YYYY-MM-DD
- `page` (integer, opcional, padrão=0): Número da página
- `size` (integer, opcional, padrão=20): Tamanho da página

**Lógica de Filtragem:**
1. **Período vs Datas Personalizadas**: Se `dataInicio` e `dataFim` forem fornecidos, eles sobrescrevem o parâmetro `periodo`
2. **Períodos Pré-definidos**: Convertidos automaticamente para datas
3. **Validação de Tipo**: Apenas tipos válidos do enum PropositionType são aceitos
4. **Busca Parcial**: Filtros por `politico` e `status` usam LIKE (%termo%)

**Exemplos:**
```
# Proposições de PL em tramitação no último mês
GET /api/v1/sdui/proposicoes?tipo=PL&status=tramitando&periodo=ultimo_mes

# Proposições de qualquer tipo de Bolsonaro
GET /api/v1/sdui/proposicoes?politico=Bolsonaro

# Proposições PEC aprovadas em período personalizado
GET /api/v1/sdui/proposicoes?tipo=PEC&status=aprovad&dataInicio=2024-01-01&dataFim=2024-12-31
```

---

### 1.4 GET /api/v1/sdui/votacoes
Retorna a tela de votações com filtros por período.

**Parâmetros de Query:**
- `periodo` (string, opcional, padrão="week"): Período das votações
- `page` (integer, opcional, padrão=0): Número da página
- `size` (integer, opcional, padrão=20): Tamanho da página

**Valores possíveis para `periodo`:**
- `"day"`: Votações do dia atual
- `"week"`: Votações da semana atual
- `"month"`: Votações do mês atual
- `"year"`: Votações do ano atual

---

### 1.5 GET /api/v1/sdui/configuracoes
Retorna a tela de configurações do aplicativo.

**Parâmetros:** Nenhum (além do header Authorization)

---

## 2. CONSTANTES E ENUMERAÇÕES

### 2.1 Tipos de Proposições (PropositionType)

**Estrutura do Enum:**
```java
public enum PropositionType {
    // Projetos de Lei
    PL("PL", "Projeto de Lei"),
    PLP("PLP", "Projeto de Lei Complementar"),
    PLV("PLV", "Projeto de Lei de Conversão"),

    // Propostas de Emenda
    PEC("PEC", "Proposta de Emenda Constitucional"),

    // Medidas Provisórias
    MPV("MPV", "Medida Provisória"),

    // Projetos de Decreto
    PDC("PDC", "Projeto de Decreto Legislativo"),

    // Projetos de Lei do Congresso
    PFC("PFC", "Projeto de Lei do Congresso"),

    // Projetos de Resolução
    PRC("PRC", "Projeto de Resolução do Congresso"),

    // Requerimentos
    REQ("REQ", "Requerimento"),
    RIC("RIC", "Requerimento de Informação"),

    // Indicações
    INC("INC", "Indicação"),

    // Recursos
    RCP("RCP", "Recurso"),

    // Mensagens
    MSC("MSC", "Mensagem"),
    MSG("MSG", "Mensagem"),

    // Avisos
    AVN("AVN", "Aviso");
}
```

**Métodos Utilitários:**
- `PropositionType.getDescription(String code)`: Retorna descrição completa
- `PropositionType.fromCode(String code)`: Retorna enum por código
- `PropositionType.isValidCode(String code)`: Valida se código existe

---

### 2.2 Períodos Pré-definidos

**Para Proposições:**
- `"ultima_semana"`: Últimos 7 dias
- `"ultimo_mes"`: Últimos 30 dias
- `"ultimos_3_meses"`: Últimos 90 dias
- `"ultimo_ano"`: Últimos 365 dias
- `"personalizado"`: Usar `dataInicio` e `dataFim`

**Para Votações:**
- `"day"`: Dia atual
- `"week"`: Semana atual
- `"month"`: Mês atual
- `"year"`: Ano atual

---

## 3. ENDPOINTS DE DADOS DIRETOS

### 3.1 GET /api/v1/propositions/politician/{id}
Retorna proposições de um político específico.

**Parâmetros de Path:**
- `id` (integer, obrigatório): ID do político

**Parâmetros de Query:**
- `page` (integer, opcional, padrão=0): Número da página
- `size` (integer, opcional, padrão=20): Tamanho da página
- `year` (integer, opcional): Filtrar por ano específico

**Resposta:** `PropositionResponseDTO`
```json
{
  "data": [...],
  "total": 150,
  "page": 0,
  "totalPages": 8,
  "sizePage": 20
}
```

---

## 4. COMPONENTES SDUI

### 4.1 Estrutura Geral de Resposta
Todos os endpoints SDUI retornam um `SDUIResponse`:

```json
{
  "id": "screen_id",
  "title": "Título da Tela",
  "navigation": {
    "header": {
      "title": "Título do Header",
      "showBack": false
    }
  },
  "metadata": {
    "version": "1.0.0",
    "cache": true,
    "ttl": 1800
  },
  "components": [
    {
      "id": "component_id",
      "type": "CARD|TEXT|CONTAINER|FILTER|etc",
      "properties": {...},
      "children": [...],
      "actions": {...}
    }
  ]
}
```

### 4.2 Tipos de Componentes Comuns

**Filtro Avançado (AdvancedFilter):**
```json
{
  "id": "filter-propositions",
  "type": "ADVANCED_FILTER",
  "properties": {
    "title": "Filtrar Propostas",
    "sections": [
      {
        "id": "tipo",
        "label": "Tipo de Proposição",
        "type": "single",
        "options": [
          {"value": "PL", "label": "Projeto de Lei", "selected": false},
          {"value": "PEC", "label": "Proposta de Emenda Constitucional", "selected": false}
        ]
      }
    ]
  },
  "actions": {
    "apply": {
      "type": "NAVIGATE",
      "target": "proposicoes",
      "params": {"tipo": "$tipo", "status": "$status"}
    }
  }
}
```

**Card de Proposição:**
```json
{
  "id": "proposition-123",
  "type": "CARD",
  "properties": {
    "title": "PL 1234/2024",
    "subtitle": "Ementa da proposição...",
    "elevation": 1,
    "borderRadius": 12,
    "padding": "16"
  },
  "children": [
    {
      "type": "TEXT",
      "properties": {
        "text": "Status: Em tramitação",
        "color": "#FF9800"
      }
    }
  ],
  "actions": {
    "press": {
      "type": "NAVIGATE",
      "target": "proposition_detail",
      "params": {"propositionId": 123}
    }
  }
}
```

---

## 5. VALIDAÇÕES E REGRAS DE NEGÓCIO

### 5.1 Validações de Parâmetros

**Tipos de Proposição:**
- Apenas códigos válidos do enum PropositionType são aceitos
- Códigos inválidos são logados como warning e ignorados

**Datas:**
- Formato: YYYY-MM-DD (ISO 8601)
- `dataInicio` deve ser anterior ou igual a `dataFim`
- Períodos pré-definidos sobrescrevem datas quando ambos são fornecidos

**Paginação:**
- `page`: Deve ser >= 0
- `size`: Deve ser entre 1 e 100
- Valores inválidos usam defaults

### 5.2 Regras de Filtragem

**Proposições:**
- Filtros são aplicados com AND lógico
- Busca por político: LIKE case-insensitive
- Busca por status: LIKE case-insensitive com %
- Tipo: Comparação exata (case-insensitive)
- Múltiplos filtros podem ser combinados

**Deputados:**
- Busca por nome: LIKE case-insensitive
- Filtro por UF: Comparação exata

### 5.3 Cache e Performance

**Metadados de Cache:**
- `version`: Controle de versão do layout
- `cache`: Se a tela pode ser cacheada
- `ttl`: Time-to-live em segundos

---

## 6. EXEMPLOS DE INTEGRAÇÃO

### 6.1 Buscar Proposições Filtradas
```javascript
// Frontend - Buscar PLs em tramitação do último mês
const params = new URLSearchParams({
  tipo: 'PL',
  status: 'tramitando',
  periodo: 'ultimo_mes',
  page: 0,
  size: 20
});

fetch(`/api/v1/sdui/proposicoes?${params}`, {
  headers: {
    'Authorization': `Bearer ${jwtToken}`,
    'Content-Type': 'application/json'
  }
})
.then(response => response.json())
.then(data => {
  // Renderizar componentes SDUI
  renderScreen(data);
});
```

### 6.2 Aplicar Filtros Avançados
```javascript
// Frontend - Aplicar filtros do componente AdvancedFilter
function applyFilters(filterValues) {
  const params = {
    tipo: filterValues.tipo,
    status: filterValues.status,
    periodo: filterValues.periodo,
    politico: filterValues.politico,
    dataInicio: filterValues.dataInicio,
    dataFim: filterValues.dataFim,
    page: 0,
    size: 20
  };

  // Navegar para tela filtrada
  navigateToScreen('proposicoes', params);
}
```

### 6.3 Paginação
```javascript
// Frontend - Carregar próxima página
function loadNextPage(currentPage, filters) {
  const nextPage = currentPage + 1;
  const params = {
    ...filters,
    page: nextPage,
    size: 20
  };

  fetchPropositions(params)
    .then(newData => {
      appendPropositions(newData.components);
    });
}
```

---

## 7. CÓDIGOS DE ERRO

- `400 Bad Request`: Parâmetros inválidos
- `401 Unauthorized`: Token JWT inválido/ausente
- `404 Not Found`: Recurso não encontrado
- `500 Internal Server Error`: Erro interno do servidor

---

## 8. CONSIDERAÇÕES PARA O FRONTEND

1. **Cache**: Respeitar metadados de cache (`ttl`) para reduzir requests
2. **Paginação**: Implementar infinite scroll ou paginação baseada no `totalPages`
3. **Filtros**: Usar componentes SDUI retornados (não hardcode filtros)
4. **Validação**: Validar entradas usando as constantes definidas nesta documentação
5. **Offline**: Considerar cache local para telas frequentemente acessadas
6. **Performance**: Implementar debouncing para buscas em tempo real

---

**Última atualização:** Janeiro 2025
**Versão da API:** 1.0.0