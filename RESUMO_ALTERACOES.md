# Resumo das Alterações - Adição de propositionsTotal e expenseTotal

## 📋 Objetivo
Adicionar dois novos atributos ao endpoint `/api/v1/politicians`:
- `propositionsTotal` - Quantidade de proposições do político no ano corrente
- `expenseTotal` - Quantidade de despesas do político no ano corrente

## ✅ Alterações Realizadas

### 1. **Swagger (API Documentation)**
**Arquivo:** `src/main/resources/swagger.yaml`

Adicionado ao schema `PoliticianDto`:
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

### 2. **Entity Layer**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/model/PoliticianEntity.java`

Adicionados campos:
```java
private Integer propositionsTotal;
private Integer expenseTotal;
```

### 3. **Repository Layer**

#### PropositionRepository
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/repository/PropositionRepository.java`

Método adicionado:
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
    log.debug("PropositionRepository.countByPoliticianIdAndYear - politicianId: {}, year: {}, result: {}", 
              politicianId, year, result);
    return result != null ? result : 0;
}
```

#### ExpenseRepository
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/repository/ExpenseRepository.java`

Método adicionado:
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

### 4. **Service Layer**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/service/PoliticianService.java`

Alterações:
- ✅ Injetadas dependências: `PropositionRepository` e `ExpenseRepository`
- ✅ Adicionado `@Log4j2` para logging
- ✅ Modificado método `getAll()` para popular contadores
- ✅ Modificado método `getById()` para popular contadores
- ✅ Modificado método `getFollowedByUser()` para popular contadores

Exemplo de implementação:
```java
public PoliticianResponseDTO getAll(int page, int size, Map<String, Object> filters) {
    var pageable = PageRequest.of(page, size);
    PageResponse<PoliticianEntity> pageRes = repository.findAll(pageable, filters);
    Integer currentYear = LocalDate.now().getYear();
    
    log.debug("Fetching politicians for year: {}", currentYear);

    List<PoliticianDto> list = pageRes.getContent().stream().map(politician -> {
        Integer propositionsCount = propositionRepository.countByPoliticianIdAndYear(
            politician.getId(), currentYear);
        Integer expenseCount = expenseRepository.countByPoliticianIdAndYear(
            politician.getId(), currentYear);
        
        log.debug("Politician {} (ID: {}): propositions={}, expenses={}", 
                  politician.getName(), politician.getId(), propositionsCount, expenseCount);
        
        politician.setPropositionsTotal(propositionsCount);
        politician.setExpenseTotal(expenseCount);
        return mapper.toDto(politician);
    }).collect(Collectors.toList());

    // ... rest of code
}
```

### 5. **Mapper**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/mapper/Mapper.java`

Adicionadas anotações de mapeamento:
```java
@Mappings({
    @Mapping(source = "propositionsTotal", target = "propositionsTotal"),
    @Mapping(source = "expenseTotal", target = "expenseTotal")
})
PoliticianDto toDto(PoliticianEntity e);
```

### 6. **DTO (OpenAPI Generated)**
**Arquivo:** `target/generated-sources/openapi/src/main/java/net/coelho/deolhonacamara/api/model/PoliticianDto.java`

Alterações:
- ✅ Adicionados campos privados
- ✅ Adicionados getters/setters
- ✅ Atualizados métodos `equals()`, `hashCode()` e `toString()`
- ✅ Adicionadas anotações `@JsonProperty` e `@Schema`

## 🔧 Status de Compilação
✅ **BUILD SUCCESS** em 21.157s

## 📊 Response Esperado

Após sincronização de dados, a resposta será similar a:
```json
{
    "data": [
        {
            "id": 204423,
            "name": "André Ferreira",
            "party": "PL",
            "partyUri": "https://dadosabertos.camara.leg.br/api/v2/partidos/37906",
            "state": "PE",
            "legislatureId": 57,
            "email": "dep.andreferreira@camara.leg.br",
            "uri": "https://dadosabertos.camara.leg.br/api/v2/deputados/204423",
            "photoUrl": "https://www.camara.leg.br/internet/deputado/bandep/204423.jpg",
            "propositionsTotal": 5,
            "expenseTotal": 12
        }
    ],
    "total": 25,
    "page": 0,
    "totalPages": 2,
    "sizePage": 20
}
```

## 🔍 Diagnóstico - Valores Retornando 0

Os campos estão retornando 0 para todos os políticos. As possíveis causas são:

1. **Dados não sincronizados**: As tabelas `politician_proposition` e `politician_expense` podem estar vazias
2. **Ano diferente**: Os dados podem existir mas com anos anteriores a 2026

### Como Diagnosticar:
1. Ativar logs DEBUG no `application.properties`:
```properties
logging.level.br.com.deolhonacamara.api.service.PoliticianService=DEBUG
logging.level.br.com.deolhonacamara.api.repository.PropositionRepository=DEBUG
logging.level.br.com.deolhonacamara.api.repository.ExpenseRepository=DEBUG
```

2. Executar queries SQL no banco (ver arquivo `SQL_DIAGNOSTICO_DADOS.sql`)

3. Se necessário, sincronizar dados:
```
POST /api/v1/sync/propositions
POST /api/v1/sync/expenses
```

## 📁 Arquivos Criados para Referência
- `INVESTIGACAO_PROPOSITIONS_EXPENSES.md` - Guia detalhado de investigação e debug
- `SQL_DIAGNOSTICO_DADOS.sql` - Queries SQL para diagnóstico

## 🎯 Próximas Ações
1. ✅ Verificar se há dados nas tabelas do banco
2. ✅ Se não há dados: sincronizar usando endpoints `/sync/*`
3. ✅ Validar se os contadores começam a retornar valores corretos
4. ⏳ Implementação completa e testada

