# Regras da API BFF - Fiscaliza AI Câmara Federal

## Visão Geral

Este documento define as regras e especificações para o Backend For Frontend (BFF) do aplicativo Fiscaliza AI. O BFF deve fornecer payloads Server-Driven UI (SDUI) que permitam renderização dinâmica das interfaces do aplicativo mobile.

## Estrutura Base de Response

Todos os endpoints devem retornar uma resposta no formato `SDUIResponse`:

```typescript
interface SDUIResponse {
  screen: SDUIScreen;
  actions?: SDUIAction[];
  metadata?: {
    version: string;
    cache?: boolean;
    ttl?: number;
  };
}
```

### Campos obrigatórios:
- `screen`: Configuração completa da tela SDUI
- `actions`: Ações disponíveis na tela (opcional)
- `metadata`: Metadados da resposta (opcional)

## Endpoints por Tela

### 1. GET /api/screens/home

**Descrição**: Tela inicial com resumos e estatísticas da semana.

**Estrutura do Payload**:

```json
{
  "screen": {
    "id": "home",
    "title": "De Olho na Câmara",
    "components": [
      {
        "id": "container-header-main",
        "type": "Container",
        "direction": "column",
        "padding": 0,
        "sticky": true,
        "style": {
          "backgroundColor": "#009C3B",
          "paddingTop": 20,
          "paddingBottom": 30,
          "borderBottomLeftRadius": 20,
          "borderBottomRightRadius": 20
        },
        "children": [
          {
            "id": "textblock-welcome-main",
            "type": "TextBlock",
            "text": "🇧🇷 Bem-vindo ao",
            "variant": "body",
            "color": "#FFFFFF",
            "textAlign": "center",
            "style": { "marginBottom": 8 }
          },
          {
            "id": "textblock-title-main",
            "type": "TextBlock",
            "text": "De Olho na Câmara",
            "variant": "display",
            "color": "#FFFFFF",
            "fontSize": 28,
            "fontWeight": "700",
            "textAlign": "center",
            "letterSpacing": -0.5,
            "style": { "marginBottom": 8 }
          },
          {
            "id": "textblock-subtitle-main",
            "type": "TextBlock",
            "text": "Acompanhe em tempo real as atividades legislativas",
            "variant": "body",
            "color": "#E8F5E8",
            "textAlign": "center",
            "fontSize": 14
          }
        ]
      },
      {
        "id": "spacer-header-main",
        "type": "Spacer",
        "size": "large"
      },
      {
        "id": "container-propositions-main",
        "type": "Container",
        "direction": "column",
        "spacing": 20,
        "padding": "0 20",
        "children": [
          {
            "id": "container-propositions-header-main",
            "type": "Container",
            "direction": "row",
            "justifyContent": "space-between",
            "alignItems": "center",
            "children": [
              {
                "id": "textblock-propositions-title-main",
                "type": "TextBlock",
                "text": "Últimas Proposições",
                "variant": "title",
                "color": "#1a1a1a",
                "fontSize": 20,
                "fontWeight": "600"
              },
              {
                "id": "button-view-all-propositions-main",
                "type": "Button",
                "title": "Ver todas",
                "variant": "ghost",
                "size": "small",
                "onPress": "navigate_propositions"
              }
            ]
          }
        ]
      },
      {
        "id": "container-stats-main",
        "type": "Container",
        "direction": "column",
        "spacing": 16,
        "padding": "0 20",
        "children": [
          {
            "id": "textblock-stats-title-main",
            "type": "TextBlock",
            "text": "📊 Estatísticas da Semana",
            "variant": "title",
            "color": "#1a1a1a",
            "fontSize": 20,
            "fontWeight": "600",
            "style": { "marginBottom": 8 }
          },
          {
            "id": "container-stats-grid-main",
            "type": "Container",
            "direction": "row",
            "spacing": 16,
            "scrollable": true,
            "horizontal": true,
            "style": { "paddingHorizontal": 20 },
            "children": [
              // Cards de estatísticas (até 3 cards visíveis)
            ]
          }
        ]
      },
      {
        "id": "spacer-bottom-main",
        "type": "Spacer",
        "size": "large"
      }
    ]
  },
  "actions": [
    {
      "type": "navigate",
      "payload": {
        "screen": "propositions"
      }
    }
  ],
  "metadata": {
    "version": "1.0.0",
    "cache": true,
    "ttl": 3600
  }
}
```

**Regras específicas para Home**:
- Sempre incluir header fixo com gradiente brasileiro
- Seção de últimas proposições: máximo 3 cards
- Seção de estatísticas: máximo 3 cards em scroll horizontal
- Botões devem usar ações de navegação

---

### 2. GET /api/screens/deputados

**Descrição**: Lista de deputados federais com filtros e busca.

**Estrutura do Payload**:

```json
{
  "screen": {
    "id": "deputados",
    "title": "Deputados(as)",
    "navigation": {
      "header": {
        "title": "Deputados(as)",
        "showBack": false,
        "actions": [
          {
            "id": "search-action",
            "type": "icon",
            "icon": "search",
            "action": "toggle_search"
          }
        ]
      }
    },
    "components": [
      {
        "id": "container-search-main",
        "type": "Container",
        "direction": "row",
        "padding": "16 20",
        "children": [
          {
            "id": "input-search-main",
            "type": "Input",
            "placeholder": "Buscar deputado...",
            "inputType": "text"
          }
        ]
      },
      {
        "id": "container-filters-main",
        "type": "Container",
        "direction": "row",
        "spacing": 12,
        "scrollable": true,
        "horizontal": true,
        "padding": "0 20 16 20",
        "children": [
          {
            "id": "button-filter-all-main",
            "type": "Button",
            "title": "Todos",
            "variant": "secondary",
            "size": "small",
            "onPress": "filter_deputados",
            "actionParams": { "filter": "all" }
          },
          {
            "id": "button-filter-uf-main",
            "type": "Button",
            "title": "Por UF",
            "variant": "outline",
            "size": "small",
            "onPress": "filter_deputados",
            "actionParams": { "filter": "uf" }
          }
        ]
      },
      {
        "id": "container-deputados-list-main",
        "type": "Container",
        "direction": "column",
        "scrollable": true,
        "children": [
          // Cards de deputados
        ]
      }
    ]
  },
  "actions": [
    {
      "type": "api",
      "payload": {
        "endpoint": "/api/deputados/search",
        "method": "GET"
      }
    }
  ]
}
```

**Estrutura do Card de Deputado**:

```json
{
  "id": "deputado-{{id}}",
  "type": "Card",
  "title": "{{nome}}",
  "subtitle": "{{partido}} - {{uf}}",
  "elevation": 1,
  "borderRadius": 12,
  "padding": 16,
  "margin": "0 20 8 20",
  "backgroundColor": "#FFFFFF",
  "onPress": "open_deputy_detail",
  "actionParams": {
    "deputyId": "{{id}}",
    "deputyName": "{{nome}}",
    "apiEndpoint": "/api/deputados/{{id}}"
  },
  "children": [
    {
      "id": "deputado-photo",
      "type": "Image",
      "source": "{{urlFoto}}",
      "width": 60,
      "height": 60,
      "resizeMode": "cover",
      "style": {
        "borderRadius": 30,
        "marginBottom": 12
      }
    },
    {
      "id": "deputado-info",
      "type": "TextBlock",
      "text": "{{situacao}} • {{email}}",
      "variant": "caption",
      "color": "#666",
      "fontSize": 12
    }
  ]
}
```

**Regras específicas para Deputados**:
- Campo de busca sempre visível no topo
- Filtros horizontais scrolláveis
- Lista vertical infinita de deputados
- Cada deputado deve ter foto, nome, partido-UF
- Suporte a paginação

---

### 3. GET /api/screens/proposicoes

**Descrição**: Lista de proposições legislativas com filtros e detalhes.

**Estrutura do Payload**:

```json
{
  "screen": {
    "id": "proposals",
    "title": "Proposições",
    "navigation": {
      "header": {
        "title": "Proposições",
        "showBack": false,
        "actions": [
          {
            "id": "filter-action",
            "type": "icon",
            "icon": "filter",
            "action": "toggle_filters"
          }
        ]
      }
    },
    "components": [
      {
        "id": "container-filters-main",
        "type": "Container",
        "direction": "column",
        "padding": "16 20",
        "children": [
          {
            "id": "container-filters-row-main",
            "type": "Container",
            "direction": "row",
            "spacing": 8,
            "children": [
              {
                "id": "button-filter-tipo-main",
                "type": "Button",
                "title": "Tipo",
                "variant": "outline",
                "size": "small",
                "onPress": "filter_proposals",
                "actionParams": { "filter": "tipo" }
              },
              {
                "id": "button-filter-status-main",
                "type": "Button",
                "title": "Status",
                "variant": "outline",
                "size": "small",
                "onPress": "filter_proposals",
                "actionParams": { "filter": "status" }
              }
            ]
          }
        ]
      },
      {
        "id": "container-proposals-list-main",
        "type": "Container",
        "direction": "column",
        "scrollable": true,
        "children": [
          // Cards de proposições
        ]
      }
    ]
  }
}
```

**Estrutura do Card de Proposição**:

```json
{
  "id": "proposition-{{id}}",
  "type": "Card",
  "title": "{{siglaTipo}} {{numero}}/{{ano}}",
  "subtitle": "{{ementa}}",
  "elevation": 1,
  "borderRadius": 12,
  "padding": 16,
  "margin": "0 20 12 20",
  "backgroundColor": "#FFFFFF",
  "onPress": "open_proposition_detail",
  "actionParams": {
    "propositionId": "{{id}}",
    "propositionType": "{{siglaTipo}}",
    "year": "{{ano}}",
    "title": "{{ementa}}",
    "status": "{{statusProposicao}}",
    "apiEndpoint": "/api/proposicoes/{{id}}"
  },
  "children": [
    {
      "id": "prop-status",
      "type": "Container",
      "direction": "row",
      "justifyContent": "space-between",
      "alignItems": "center",
      "children": [
        {
          "id": "prop-status-text",
          "type": "TextBlock",
          "text": "{{statusProposicao.descricaoTramitacao}}",
          "variant": "caption",
          "color": "{{statusColor}}",
          "fontSize": 12,
          "fontWeight": "600"
        },
        {
          "id": "prop-date",
          "type": "TextBlock",
          "text": "{{dataApresentacao}}",
          "variant": "caption",
          "color": "#666",
          "fontSize": 12
        }
      ]
    },
    {
      "id": "prop-author",
      "type": "TextBlock",
      "text": "Autor: {{nomeAutor}}",
      "variant": "body",
      "color": "#666",
      "fontSize": 14,
      "style": { "marginTop": 8 }
    }
  ]
}
```

**Regras específicas para Proposições**:
- Filtros por tipo (PL, PEC, RIC, etc.) e status
- Ordenação por data de apresentação (mais recente primeiro)
- Suporte a paginação infinita
- Status color-coded (verde para aprovado, amarelo para tramitação, vermelho para rejeitado)

---

### 4. GET /api/screens/votacoes

**Descrição**: Votações recentes e históricas com resultados detalhados.

**Estrutura do Payload**:

```json
{
  "screen": {
    "id": "votes",
    "title": "Votações",
    "navigation": {
      "header": {
        "title": "Votações",
        "showBack": false,
        "actions": [
          {
            "id": "calendar-action",
            "type": "icon",
            "icon": "calendar",
            "action": "filter_by_date"
          }
        ]
      }
    },
    "components": [
      {
        "id": "container-period-selector-main",
        "type": "Container",
        "direction": "row",
        "spacing": 8,
        "padding": "16 20",
        "scrollable": true,
        "horizontal": true,
        "children": [
          {
            "id": "button-period-today-main",
            "type": "Button",
            "title": "Hoje",
            "variant": "primary",
            "size": "small",
            "onPress": "filter_votes",
            "actionParams": { "period": "today" }
          },
          {
            "id": "button-period-week-main",
            "type": "Button",
            "title": "Esta Semana",
            "variant": "outline",
            "size": "small",
            "onPress": "filter_votes",
            "actionParams": { "period": "week" }
          }
        ]
      },
      {
        "id": "container-votes-list-main",
        "type": "Container",
        "direction": "column",
        "scrollable": true,
        "children": [
          // Cards de votações
        ]
      }
    ]
  }
}
```

**Estrutura do Card de Votação**:

```json
{
  "id": "vote-{{id}}",
  "type": "Card",
  "title": "{{titulo}}",
  "subtitle": "{{objVotacao}}",
  "elevation": 1,
  "borderRadius": 12,
  "padding": 16,
  "margin": "0 20 12 20",
  "backgroundColor": "#FFFFFF",
  "onPress": "open_vote_detail",
  "actionParams": {
    "voteId": "{{id}}",
    "voteTitle": "{{titulo}}",
    "apiEndpoint": "/api/votacoes/{{id}}"
  },
  "children": [
    {
      "id": "vote-result",
      "type": "Container",
      "direction": "row",
      "justifyContent": "space-between",
      "alignItems": "center",
      "style": { "marginTop": 8 },
      "children": [
        {
          "id": "vote-approval",
          "type": "TextBlock",
          "text": "✅ Aprovado",
          "variant": "body",
          "color": "#28a745",
          "fontSize": 14,
          "fontWeight": "600"
        },
        {
          "id": "vote-date",
          "type": "TextBlock",
          "text": "{{data}} às {{hora}}",
          "variant": "caption",
          "color": "#666",
          "fontSize": 12
        }
      ]
    },
    {
      "id": "vote-stats",
      "type": "Container",
      "direction": "row",
      "spacing": 16,
      "style": { "marginTop": 12 },
      "children": [
        {
          "id": "sim-count",
          "type": "TextBlock",
          "text": "Sim: {{qtdeVotosSim}}",
          "variant": "caption",
          "color": "#28a745",
          "fontSize": 12
        },
        {
          "id": "nao-count",
          "type": "TextBlock",
          "text": "Não: {{qtdeVotosNao}}",
          "variant": "caption",
          "color": "#dc3545",
          "fontSize": 12
        },
        {
          "id": "abstencao-count",
          "type": "TextBlock",
          "text": "Abstenção: {{qtdeVotosAbstencao}}",
          "variant": "caption",
          "color": "#6c757d",
          "fontSize": 12
        }
      ]
    }
  ]
}
```

**Regras específicas para Votações**:
- Filtros por período (hoje, semana, mês)
- Resultados com contadores de votos (Sim/Não/Abstenção)
- Status visual (aprovado/rejeitado)
- Detalhes devem incluir lista de deputados e seus votos

---

### 5. GET /api/screens/configuracoes

**Descrição**: Configurações do usuário e personalização do app.

**Estrutura do Payload**:

```json
{
  "screen": {
    "id": "settings",
    "title": "Configurações",
    "components": [
      {
        "id": "container-profile-main",
        "type": "Container",
        "direction": "column",
        "padding": "20",
        "children": [
          {
            "id": "textblock-profile-title-main",
            "type": "TextBlock",
            "text": "Perfil",
            "variant": "title",
            "color": "#1a1a1a",
            "fontSize": 20,
            "fontWeight": "600",
            "style": { "marginBottom": 16 }
          },
          {
            "id": "container-notifications-setting-main",
            "type": "Container",
            "direction": "row",
            "justifyContent": "space-between",
            "alignItems": "center",
            "padding": "12 0",
            "children": [
              {
                "id": "textblock-notifications-label-main",
                "type": "TextBlock",
                "text": "Notificações",
                "variant": "body",
                "color": "#1a1a1a",
                "fontSize": 16
              },
              {
                "id": "button-notifications-toggle-main",
                "type": "Button",
                "title": "Ativado",
                "variant": "secondary",
                "size": "small",
                "onPress": "toggle_notifications"
              }
            ]
          }
        ]
      },
      {
        "id": "spacer-profile-app-main",
        "type": "Spacer",
        "size": "medium"
      },
      {
        "id": "container-app-main",
        "type": "Container",
        "direction": "column",
        "padding": "20",
        "children": [
          {
            "id": "textblock-app-title-main",
            "type": "TextBlock",
            "text": "Aplicativo",
            "variant": "title",
            "color": "#1a1a1a",
            "fontSize": 20,
            "fontWeight": "600",
            "style": { "marginBottom": 16 }
          },
          {
            "id": "container-theme-setting-main",
            "type": "Container",
            "direction": "row",
            "justifyContent": "space-between",
            "alignItems": "center",
            "padding": "12 0",
            "children": [
              {
                "id": "textblock-theme-label-main",
                "type": "TextBlock",
                "text": "Tema",
                "variant": "body",
                "color": "#1a1a1a",
                "fontSize": 16
              },
              {
                "id": "button-theme-selector-main",
                "type": "Button",
                "title": "Sistema",
                "variant": "outline",
                "size": "small",
                "onPress": "select_theme"
              }
            ]
          }
        ]
      },
      {
        "id": "spacer-app-about-main",
        "type": "Spacer",
        "size": "medium"
      },
      {
        "id": "container-about-main",
        "type": "Container",
        "direction": "column",
        "padding": "20",
        "children": [
          {
            "id": "textblock-about-title-main",
            "type": "TextBlock",
            "text": "Sobre",
            "variant": "title",
            "color": "#1a1a1a",
            "fontSize": 20,
            "fontWeight": "600",
            "style": { "marginBottom": 16 }
          },
          {
            "id": "textblock-version-info-main",
            "type": "TextBlock",
            "text": "Versão 1.0.0",
            "variant": "caption",
            "color": "#666",
            "fontSize": 14
          }
        ]
      }
    ]
  },
  "actions": [
    {
      "type": "custom",
      "payload": {
        "action": "update_user_settings",
        "endpoint": "/api/user/settings"
      }
    }
  ]
}
```

**Regras específicas para Configurações**:
- Seções organizadas (Perfil, Aplicativo, Sobre)
- Toggles e seletores para configurações
- Informações sobre versão do app
- Ações para persistir configurações

---

## Regras Gerais de Implementação

### 1. IDs Únicos
- Todos os componentes devem ter IDs únicos por tela
- IDs devem seguir padrão: `{{tipo}}-{{identificador}}`
- Exemplo: `deputado-123`, `proposition-PL1234`

### 2. Ações e Navegação
- Usar `onPress` para ações de componentes
- `actionParams` deve conter todos os dados necessários
- Ações padrão: `navigate_*`, `open_*_detail`, `filter_*`, `toggle_*`

### 3. Estilos Consistentes
- Seguir Design System brasileiro (verde #009C3B)
- Espaçamentos baseados em múltiplos de 4px
- Bordas arredondadas de 12px para cards
- Sombras elevation 1-2 para profundidade

### 4. Performance
- Implementar paginação para listas grandes
- Usar `scrollable: true` para listas virtuais
- Cache com TTL apropriado por tela
- Lazy loading para imagens

### 5. Tratamento de Estados
- Loading states com `loading: true`
- Empty states com componentes apropriados
- Error handling com `SDUIError`

### 6. Responsividade
- Componentes devem se adaptar a diferentes tamanhos de tela
- Scroll horizontal para grids pequenos
- Scroll vertical para listas longas

---

## Validação e Testes

### Validação de Schema
- Todos os payloads devem validar contra `SDUIResponse` schema
- Campos obrigatórios não podem ser null/undefined
- Tipos devem corresponder às interfaces TypeScript

### Testes de Integração
- Testar renderização completa de cada tela
- Validar ações e navegação
- Verificar estados de loading/error
- Testar responsividade em diferentes dispositivos

---

## Versionamento da API

- Usar semantic versioning (MAJOR.MINOR.PATCH)
- Incluir `version` em metadata de cada response
- Manter compatibilidade backward quando possível
- Documentar breaking changes
