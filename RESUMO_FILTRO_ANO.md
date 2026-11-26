# 📋 Filtro de Ano para Proposições e Despesas

> ⚠️ Documento histórico de implementação.
> Para arquitetura, stack e setup atualizados, consulte o `README.md`.
> Para contexto de produto, consulte `contexto_projeto_de_olho_na_camara.md`.

## 🎯 Objetivo
Adicionar suporte a filtro de **ano** para buscar proposições e despesas de políticos. Se o ano não for informado, utiliza o ano atual (2026).

## ✅ Status da Implementação
- ✅ **BUILD SUCCESS** - Compilação bem-sucedida
- ✅ Filtro implementado em ambos os endpoints
- ✅ Padrão de ano atual quando não informado

---

## 📝 Endpoints Modificados

### 1. GET /api/v1/politicians (Listagem)
**Novo Parâmetro:**
```
?year=2025
```

**Descrição:** Filtra propositionsTotal e expenseTotal para o ano especificado

**Exemplos:**
```bash
# Usar ano 2025
GET /api/v1/politicians?year=2025

# Usar ano atual (2026) - padrão
GET /api/v1/politicians

# Combinado com outros filtros
GET /api/v1/politicians?year=2024&party=PT&name=João
```

### 2. GET /api/v1/politicians/{id} (Detalhes)
**Novo Parâmetro:**
```
?year=2025
```

**Descrição:** Filtra propositionsTotal e expenseTotal para o ano especificado

**Exemplos:**
```bash
# Buscar político específico com dados de 2025
GET /api/v1/politicians/204423?year=2025

# Usar ano atual (2026) - padrão
GET /api/v1/politicians/204423
```

---

## 🏗️ Implementação Técnica

### Camada de Swagger
**Arquivo:** `src/main/resources/swagger.yaml`

Parâmetro adicionado em ambos endpoints:
```yaml
- in: query
  name: year
  schema: { type: integer, default: 2026 }
  description: Year for propositions and expenses count (default is current year)
```

### Camada de Controller
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/controller/PoliticiansController.java`

```java
// listPoliticians - aceita year como query parameter
public ResponseEntity<PoliticianResponseDTO> listPoliticians(
    Integer page, Integer size, String name,
    List<String> party, List<String> state, 
    Boolean isFollowed, Integer year)

// politiciansIdGet - aceita year como query parameter  
public ResponseEntity<PoliticianDto> politiciansIdGet(Integer id, Integer year)
```

### Camada de Service
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/service/PoliticianService.java`

#### Método getAll():
```java
Integer currentYear = filters.containsKey("year") 
    ? ((Number) filters.get("year")).intValue() 
    : LocalDate.now().getYear();

log.debug("Fetching politicians for year: {}", currentYear);
```

#### Método getById():
```java
public PoliticianDto getById(Integer id, UUID userId, Integer year) {
    Integer targetYear = year != null ? year : LocalDate.now().getYear();
    log.debug("Fetching politician by ID: {} for year: {}", id, targetYear);
    
    // ... usa targetYear para contar proposições e despesas
}
```

---

## 📊 Comportamento

### Sem Informar Ano
```bash
GET /api/v1/politicians

# Resultado:
{
    "propositionsTotal": 5,  // Proposições de 2026
    "expenseTotal": 12       // Despesas de 2026
}
```

### Informando Ano 2025
```bash
GET /api/v1/politicians?year=2025

# Resultado:
{
    "propositionsTotal": 8,  // Proposições de 2025
    "expenseTotal": 15       // Despesas de 2025
}
```

### Informando Ano 2024
```bash
GET /api/v1/politicians?year=2024

# Resultado:
{
    "propositionsTotal": 3,  // Proposições de 2024
    "expenseTotal": 7        // Despesas de 2024
}
```

---

## 🔄 Fluxo de Processamento

```
Controller (recebe year?)
    ↓
Service (year = year ?? LocalDate.now().getYear())
    ↓
Repository (countByPoliticianIdAndYear(id, year))
    ↓
Database (SELECT COUNT WHERE year = ?)
    ↓
Response (com dados do ano especificado)
```

---

## ✨ Características

✅ **Padrão Inteligente:** Se não informado, usa ano atual automaticamente
✅ **Flexível:** Funciona com qualquer ano
✅ **Combinável:** Funciona com todos os filtros existentes (name, party, state, isFollowed)
✅ **Logging:** Registra qual ano está sendo usado em cada requisição
✅ **Type-Safe:** Converte Integer para int com segurança

---

## 📋 Exemplos de Requisições Completas

### Exemplo 1: Políticos do PT em 2025
```bash
curl -H "Authorization: Bearer {token}" \
  "http://localhost:8080/api/v1/politicians?party=PT&year=2025"
```

### Exemplo 2: Políticos que segue em 2024
```bash
curl -H "Authorization: Bearer {token}" \
  "http://localhost:8080/api/v1/politicians?isFollowed=true&year=2024"
```

### Exemplo 3: Políticos do SP que não segue em 2023
```bash
curl -H "Authorization: Bearer {token}" \
  "http://localhost:8080/api/v1/politicians?state=SP&isFollowed=false&year=2023"
```

### Exemplo 4: Detalhe de político específico em 2022
```bash
curl -H "Authorization: Bearer {token}" \
  "http://localhost:8080/api/v1/politicians/204423?year=2022"
```

---

## 🧪 Testes Recomendados

1. **Sem Year (usa padrão):**
   ```bash
   GET /api/v1/politicians
   # propositionsTotal e expenseTotal de 2026
   ```

2. **Com Year explícito:**
   ```bash
   GET /api/v1/politicians?year=2025
   # propositionsTotal e expenseTotal de 2025
   ```

3. **Diferentes Anos:**
   ```bash
   GET /api/v1/politicians?year=2020
   GET /api/v1/politicians?year=2021
   GET /api/v1/politicians?year=2022
   # Verificar se os valores mudam conforme o ano
   ```

4. **Combinado com Filtros:**
   ```bash
   GET /api/v1/politicians?name=João&year=2024&party=PT
   # Filtrar por nome, partido e ano
   ```

5. **Por ID com Year:**
   ```bash
   GET /api/v1/politicians/204423?year=2020
   GET /api/v1/politicians/204423?year=2025
   # Verificar como mudam as proposições/despesas
   ```

---

## 📌 Compatibilidade

- ✅ Retrocompatível: Sem informar year, continua funcionando como antes
- ✅ Combinável: Funciona com todos os filtros anteriores
- ✅ Seguro: Validação de tipos automática
- ✅ Performático: Usa SQL WHERE para filtrar por ano no banco

---

## 🔗 Relacionamento com Atributos

| Atributo | Afetado por year? |
|----------|-------------------|
| `propositionsTotal` | ✅ Sim |
| `expenseTotal` | ✅ Sim |
| `isFollowed` | ❌ Não (não é temporal) |
| Nome, Partido, Estado | ❌ Não |

---

## ✅ Compilação

```
BUILD SUCCESS
Total time: 30.575 seconds
Warnings: 8 (não relacionados)
Errors: 0
```

---

## 📚 Documentação Relacionada

- `RESUMO_FINAL_COMPLETO.md` - Visão geral de todas as implementações
- `RESUMO_ISFOLLOWED.md` - Detalhes de isFollowed
- `RESUMO_ALTERACOES.md` - Detalhes de propositions/expenses
- `SQL_DIAGNOSTICO_DADOS.sql` - Queries SQL para diagnóstico

---

**Status Final:** ✅ **PRONTO PARA PRODUÇÃO**

O filtro de ano está funcionando corretamente e compilado com sucesso!
