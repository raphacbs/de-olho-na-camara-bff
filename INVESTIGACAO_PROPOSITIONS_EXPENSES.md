# Investigação: propositionsTotal e expenseTotal retornando 0

## Situação Atual
Os campos `propositionsTotal` e `expenseTotal` estão sendo retornados como 0 para todos os políticos no endpoint `/api/v1/politicians`.

## O que foi implementado
✅ Adicionado campo `propositionsTotal` (Integer) em `PoliticianEntity`
✅ Adicionado campo `expenseTotal` (Integer) em `PoliticianEntity`
✅ Adicionado método `countByPoliticianIdAndYear()` em `PropositionRepository`
✅ Adicionado método `countByPoliticianIdAndYear()` em `ExpenseRepository`
✅ Atualizado `PoliticianService` para popular os contadores em todas as operações
✅ Adicionado logging DEBUG para rastrear valores
✅ Atualizado Swagger com novos campos
✅ Atualizado PoliticianDto com novos campos
✅ Compilação bem-sucedida ✓

## Possíveis Causas

### 1. Dados não sincronizados
Os valores retornam 0 porque pode não haver registros nas tabelas:
- `politician_proposition` (tabela de relacionamento entre políticos e proposições)
- `politician_expense` (tabela de despesas)

**Solução:** Executar os jobs de sincronização:
```
POST /api/v1/sync/propositions
POST /api/v1/sync/expenses
```

### 2. Mismatch de anos
Se há dados nas tabelas mas com anos diferentes do ano corrente (2026), os contadores retornarão 0.

**Para verificar:**
```sql
-- Verificar proposições existentes
SELECT DISTINCT p.year, COUNT(*) as qty
FROM politician_proposition pp
INNER JOIN proposition p ON p.id = pp.proposition_id
GROUP BY p.year
ORDER BY p.year DESC;

-- Verificar despesas existentes
SELECT DISTINCT year, COUNT(*) as qty
FROM politician_expense
GROUP BY year
ORDER BY year DESC;
```

## Como Debugar

### 1. Verificar logs
Os logs DEBUG agora mostram:
```
Fetching politicians for year: 2026
Politician {name} (ID: {id}): propositions={count}, expenses={count}
```

Ative o nível DEBUG no arquivo `application.properties`:
```properties
logging.level.br.com.deolhonacamara.api.service.PoliticianService=DEBUG
logging.level.br.com.deolhonacamara.api.repository.PropositionRepository=DEBUG
logging.level.br.com.deolhonacamara.api.repository.ExpenseRepository=DEBUG
```

### 2. Testar diretamente no banco
```sql
-- Teste para um político específico (ex: ID 204423)
SELECT COUNT(*) as propositions_2026
FROM politician_proposition pp
INNER JOIN proposition p ON p.id = pp.proposition_id
WHERE pp.politician_id = 204423
AND p.year = 2026;

SELECT COUNT(*) as expenses_2026
FROM politician_expense
WHERE politician_id = 204423
AND year = 2026;
```

### 3. Chamar endpoint GET /api/v1/politicians/{id}
Para ver os logs de um único político:
```
GET /api/v1/politicians/204423
```

## Próximos Passos

1. **Se não há dados:** Sincronizar as tabelas usando os endpoints `/sync/propositions` e `/sync/expenses`
2. **Se há dados com ano diferente:** Decidir se usar o ano corrente ou permitir filtro por ano
3. **Validar:** Após sincronização, fazer nova requisição ao endpoint

## Código de Contagem

### PropositionRepository.countByPoliticianIdAndYear()
```java
public Integer countByPoliticianIdAndYear(Integer politicianId, Integer year) {
    String sql = """
        SELECT COUNT(*) FROM politician_proposition pp
        INNER JOIN proposition p ON p.id = pp.proposition_id
        WHERE pp.politician_id = :politicianId
        AND p.year = :year
    """;
    Integer result = jdbcTemplate.queryForObject(sql,
        Map.of("politicianId", politicianId, "year", year),
        Integer.class);
    return result != null ? result : 0;
}
```

### ExpenseRepository.countByPoliticianIdAndYear()
```java
public Integer countByPoliticianIdAndYear(Integer politicianId, Integer year) {
    String sql = """
        SELECT COUNT(*) FROM politician_expense
        WHERE politician_id = :politicianId
        AND year = :year
    """;
    Integer result = jdbcTemplate.queryForObject(sql, 
        Map.of("politicianId", politicianId, "year", year), 
        Integer.class);
    return result != null ? result : 0;
}
```

## Mudanças Realizadas em Detalhes

### 1. PoliticianService
- Injetadas dependências de `PropositionRepository` e `ExpenseRepository`
- Adicionado `@Log4j2` para logging
- Método `getAll()`: Popular `propositionsTotal` e `expenseTotal` para cada político
- Método `getById()`: Popular os contadores ao buscar um político específico
- Método `getFollowedByUser()`: Popular os contadores para políticos seguidos

### 2. PoliticianEntity
```java
private Integer propositionsTotal;
private Integer expenseTotal;
```

### 3. Mapper
Adicionadas anotações @Mapping para mapear os novos campos:
```java
@Mappings({
    @Mapping(source = "propositionsTotal", target = "propositionsTotal"),
    @Mapping(source = "expenseTotal", target = "expenseTotal")
})
PoliticianDto toDto(PoliticianEntity e);
```

### 4. PoliticianDto (OpenAPI gerado)
- Adicionados campos privados `propositionsTotal` e `expenseTotal`
- Adicionados getters/setters
- Atualizado `equals()`, `hashCode()` e `toString()`

### 5. Swagger
Adicionados aos schema `PoliticianDto`:
```yaml
propositionsTotal:
  type: integer
  example: 15
  description: Total quantity of propositions in the current year
expenseTotal:
  type: integer
  example: 25
  description: Total quantity of expenses in the current year
```

