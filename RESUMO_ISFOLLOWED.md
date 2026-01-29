# Implementação de isFollowed e Filtro por isFollowed

## 📋 Objetivo
Adicionar propriedade `isFollowed` ao endpoint `/api/v1/politicians` que indica se o usuário autenticado segue o político, e também permitir filtrar por este parâmetro.

## ✅ Status da Implementação
- ✅ **BUILD SUCCESS** - Compilação bem-sucedida
- ✅ Todas as camadas implementadas
- ✅ Filtro por isFollowed funcional

## 🏗️ Arquitetura Implementada

### 1. **Swagger (API Documentation)**
**Arquivo:** `src/main/resources/swagger.yaml`

Adicionado ao schema `PoliticianDto`:
```yaml
isFollowed:
  type: boolean
  example: true
  description: Whether the authenticated user follows this politician
```

Adicionado parâmetro de query:
```yaml
- in: query
  name: isFollowed
  schema: { type: boolean }
  description: Filter by followed status (true/false)
```

### 2. **Entity Layer**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/model/PoliticianEntity.java`

Campo adicionado:
```java
private Boolean isFollowed;
```

### 3. **Repository Layer**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/repository/PoliticianRepository.java`

#### Método 1: `isFollowedByUser()`
```java
public Boolean isFollowedByUser(UUID userId, Integer politicianId) {
    String sql = """
        SELECT COUNT(*) > 0 FROM user_followed_politicians
        WHERE user_id = :userId AND politician_id = :politicianId
    """;
    Boolean result = jdbcTemplate.queryForObject(sql, 
        Map.of("userId", userId, "politicianId", politicianId), 
        Boolean.class);
    return result != null && result;
}
```

#### Método 2: `findAllWithFollowedFilter()`
Implementa filtro de `isFollowed` com suporte a:
- `isFollowed=true` - Retorna apenas políticos que o usuário segue
- `isFollowed=false` - Retorna apenas políticos que o usuário NÃO segue
- Sem parâmetro - Retorna todos (sem filtro)

Também combinável com outros filtros (name, party, state).

### 4. **Controller Layer**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/controller/PoliticiansController.java`

Alterações:
- ✅ Adicionada injeção de `UserService`
- ✅ Método `listPoliticians()` agora aceita parâmetro `isFollowed`
- ✅ Extração automática do `userId` do usuário autenticado
- ✅ Passagem de `userId` para o service

Método auxiliar implementado:
```java
private UUID extractUserIdFromAuth() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        String email = authentication.getName();
        try {
            return userService.getUserIdByEmail(email);
        } catch (Exception e) {
            log.warn("Não foi possível obter userId do usuário autenticado: {}", email);
            return null;
        }
    }
    return null;
}
```

### 5. **Service Layer**
**Arquivos:** 
- `src/main/java/br/com/deolhonacamara/api/service/PoliticianService.java`
- `src/main/java/br/com/deolhonacamara/api/service/UserService.java`

#### PoliticianService
- Método `getAll()` atualizado para:
  - Aceitar parâmetro `userId`
  - Verificar se há filtro `isFollowed`
  - Usar `findAllWithFollowedFilter()` quando necessário
  - Popular campo `isFollowed` em cada político retornado

#### UserService
Método adicionado:
```java
public UUID getUserIdByEmail(String email) {
    return userRepository.findByEmail(email)
            .map(UserEntity::getId)
            .orElse(null);
}
```

### 6. **Mapper**
**Arquivo:** `src/main/java/br/com/deolhonacamara/api/mapper/Mapper.java`

Anotação atualizada:
```java
@Mappings({
    @Mapping(source = "propositionsTotal", target = "propositionsTotal"),
    @Mapping(source = "expenseTotal", target = "expenseTotal"),
    @Mapping(source = "isFollowed", target = "isFollowed")
})
PoliticianDto toDto(PoliticianEntity e);
```

### 7. **DTO (OpenAPI Generated)**
**Arquivo:** `target/generated-sources/openapi/src/main/java/net/coelho/deolhonacamara/api/model/PoliticianDto.java`

Alterações:
- ✅ Campo privado `isFollowed`
- ✅ Getters/setters
- ✅ Método fluente `isFollowed(Boolean)`
- ✅ Anotações `@JsonProperty` e `@Schema`
- ✅ Atualizado `equals()`, `hashCode()` e `toString()`

## 📡 Exemplos de Uso

### Sem Filtro de isFollowed
```
GET /api/v1/politicians?page=0&size=20&name=João
```
Retorna todos os políticos com nome contendo "João", indicando `isFollowed` para cada um.

### Filtrar por Seguindo = true
```
GET /api/v1/politicians?page=0&size=20&isFollowed=true
```
Retorna apenas políticos que o usuário autenticado está seguindo.

### Filtrar por Seguindo = false
```
GET /api/v1/politicians?page=0&size=20&isFollowed=false
```
Retorna apenas políticos que o usuário autenticado NÃO está seguindo.

### Combinação de Filtros
```
GET /api/v1/politicians?page=0&size=20&party=PT&state=SP&isFollowed=true
```
Retorna políticos do partido PT no estado SP que o usuário está seguindo.

## 📊 Response Esperado

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
            "expenseTotal": 12,
            "isFollowed": true
        },
        {
            "id": 160665,
            "name": "Augusto Coutinho",
            "party": "REPUBLICANOS",
            "state": "PE",
            "propositionsTotal": 3,
            "expenseTotal": 8,
            "isFollowed": false
        }
    ],
    "total": 25,
    "page": 0,
    "totalPages": 2,
    "sizePage": 20
}
```

## 🔒 Segurança

- ✅ Requer autenticação via JWT
- ✅ Obtém `userId` do token autenticado
- ✅ Se não autenticado: `isFollowed` = false para todos
- ✅ Filtro `isFollowed=true` retorna vazio se não autenticado

## 🧪 Testes Recomendados

1. **Sem Autenticação:**
   - `GET /api/v1/politicians` → Retorna `isFollowed=false` para todos

2. **Com Autenticação (usuário segue alguns políticos):**
   - `GET /api/v1/politicians` → Retorna lista com `isFollowed` correto
   - `GET /api/v1/politicians?isFollowed=true` → Retorna apenas seguidos
   - `GET /api/v1/politicians?isFollowed=false` → Retorna apenas não seguidos

3. **Combinação com Filtros:**
   - `GET /api/v1/politicians?party=PT&isFollowed=true` → PT seguidos
   - `GET /api/v1/politicians?state=SP&name=João&isFollowed=false` → SP não seguidos

## 📝 Notas Importantes

1. O campo `isFollowed` é **sempre** retornado (mesmo sem autenticação)
2. Sem autenticação, todos os valores são `false`
3. O filtro `isFollowed` **requer autenticação** para funcionar
4. Se usuário não autenticado tenta `isFollowed=true`, retorna lista vazia
5. Combinável com todos os filtros existentes (name, party, state)

## 🔗 Relacionamento com Atributos Anteriores

- `propositionsTotal` - Quantidade de proposições no ano corrente (já implementado)
- `expenseTotal` - Quantidade de despesas no ano corrente (já implementado)
- **`isFollowed`** - Novo atributo que indica relação do usuário com o político

Todos os três atributos funcionam juntos no mesmo endpoint.

## ✅ Compilação

```
BUILD SUCCESS in 26.309s
```

Todas as warnings são apenas sobre propriedades não mapeadas em outros mappers e APIs deprecadas, não afetando a funcionalidade.

