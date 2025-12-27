# Plano de Tarefas: Análise e Padronização de Fluxos de Endpoints

## Visão Geral
Este documento analisa os padrões de implementação dos fluxos de endpoints no projeto "De Olho na Câmara" e estabelece regras para criação de novos fluxos seguindo o padrão identificado.

## Análise dos Padrões Existentes

### Padrão Speech (Referência)
**Arquivos analisados:**
- `SpeechController.java`
- `SpeechService.java`
- `SpeechRepository.java`

**Características do padrão:**
1. **Controller**: Implementa interface gerada da API OpenAPI, método único `getPoliticianSpeeches(Integer id, Integer page, Integer size)`
2. **Service**: Método `getByPoliticianId(Integer politicianId, int page, int size)` que retorna `SpeechResponseDTO`
3. **Repository**: Usa `NamedParameterJdbcTemplate`, método `findByPoliticianId` retorna `PageResponse<SpeechEntity>`

---

## Fluxos que Seguem o Padrão Speech

### ✅ Seguidores Completos (igual ao Speech)
1. **Presence** (`PresenceController`, `PresenceService`, `PresenceRepository`)
   - Seguindo exatamente o padrão Speech
   - Método: `getPoliticianPresence(Integer id, Integer page, Integer size)`

2. **Vote** (`VoteController`, `VoteService`, `VoteRepository`)
   - Seguindo exatamente o padrão Speech
   - Método: `getPoliticianVotes(Integer id, Integer page, Integer size)`

### ✅ Seguidores com Variações Aceitáveis
3. **Expense** (`ExpenseController`, `ExpenseService`, `ExpenseRepository`)
   - **Variação**: Usa `ExpenseInput` para filtros adicionais (year, month)
   - **Controller**: Implementa `InputBuilder` pattern
   - **Mantém estrutura**: Service retorna `ExpenseResponseDTO`, Repository usa `NamedParameterJdbcTemplate`

4. **Proposition** (`PropositionController`, `PropositionService`, `PropositionRepository`)
   - **Variação**: Usa `PropositionInput` para filtros (year)
   - **Controller**: Implementa `InputBuilder` pattern
   - **Mantém estrutura**: Service retorna `PropositionResponseDTO`

5. **Politician** (`PoliticiansController`, `PoliticianService`, `PoliticianRepository`)
   - **Variação**: Múltiplos métodos (list, follow/unfollow, get by user)
   - **Mantém estrutura**: Service retorna `PoliticianResponseDTO`, Repository usa filtros dinâmicos

---

## Fluxos que NÃO Seguem o Padrão Speech

### ❌ Autenticação/Autorização
- **AuthController**: Implementa `AuthApi`, métodos de login/registro
- **UserService**: Lógica específica de autenticação
- **UserRepository**: Acesso direto ao banco para usuários

### ❌ Gerenciamento de Dispositivos
- **DeviceController**: Implementa `DevicesApi`, registro de FCM tokens
- **UserDeviceService**: Lógica específica de dispositivos
- **UserDeviceRepository**: Gerenciamento de tokens FCM

### ❌ Operações de Seguir/Deixar de Seguir
- **FollowedController**: Implementa `FollowedApi`, operações follow/unfollow
- **PoliticianService** (extensões): Métodos `follow()` e `unfollow()`
- **PoliticianRepository** (extensões): Métodos de relacionamento

### ❌ Sincronização de Dados
- **SyncController**: Implementa `SyncApi`, endpoints de sincronização
- **SyncService**: Coordenação de jobs de sincronização
- **Scheduler**: Jobs específicos (`PoliticianSyncJob`, `ExpenseSyncJob`, etc.)

---

## Regra para Criação de Novos Fluxos (Padrão Speech)

### Pré-requisitos
1. **Entidade deve existir**: `NomeDaEntidadeEntity` em `br.com.deolhonacamara.api.model`
2. **DTO deve existir**: `NomeDaEntidadeDto` gerado pelo OpenAPI
3. **ResponseDTO deve existir**: `NomeDaEntidadeResponseDTO` gerado pelo OpenAPI
4. **API Interface deve existir**: `NomeDaEntidadeApi` gerada pelo OpenAPI

### Estrutura de Arquivos a Criar

#### 1. Controller (`NomeDaEntidadeController.java`)
```java
package br.com.deolhonacamara.api.controller;

import br.com.deolhonacamara.api.service.NomeDaEntidadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coelho.deolhonacamara.api.NomeDaEntidadeApi;
import net.coelho.deolhonacamara.api.model.NomeDaEntidadeResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class NomeDaEntidadeController implements NomeDaEntidadeApi {

    private final NomeDaEntidadeService nomeDaEntidadeService;

    @Override
    public ResponseEntity<NomeDaEntidadeResponseDTO> getPoliticianNomeDaEntidade(Integer id, Integer page, Integer size) {
        int pageNum = page != null ? page : 0;
        int pageSize = size != null ? size : 20;

        return ResponseEntity.ok(nomeDaEntidadeService.getByPoliticianId(id, pageNum, pageSize));
    }
}
```

#### 2. Service (`NomeDaEntidadeService.java`)
```java
package br.com.deolhonacamara.api.service;

import br.com.deolhonacamara.api.mapper.Mapper;
import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.NomeDaEntidadeEntity;
import br.com.deolhonacamara.api.repository.NomeDaEntidadeRepository;
import lombok.RequiredArgsConstructor;
import net.coelho.deolhonacamara.api.model.NomeDaEntidadeDto;
import net.coelho.deolhonacamara.api.model.NomeDaEntidadeResponseDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NomeDaEntidadeService {

    private final NomeDaEntidadeRepository repository;
    private final Mapper mapper = Mapper.INSTANCE;

    public NomeDaEntidadeResponseDTO getByPoliticianId(Integer politicianId, int page, int size) {
        var pageable = PageRequest.of(page, size);
        PageResponse<NomeDaEntidadeEntity> pageRes = repository.findByPoliticianId(politicianId, pageable);
        List<NomeDaEntidadeDto> list = pageRes.getContent().stream().map(mapper::toDto).collect(Collectors.toList());

        var responseDto = new NomeDaEntidadeResponseDTO();
        responseDto.data(list);
        responseDto.total((int) pageRes.getTotalElements());
        responseDto.page(pageRes.getNumber());
        responseDto.totalPages(pageRes.getTotalPages());
        responseDto.sizePage(pageRes.getSize());

        return responseDto;
    }
}
```

#### 3. Repository (`NomeDaEntidadeRepository.java`)
```java
package br.com.deolhonacamara.api.repository;

import br.com.deolhonacamara.api.model.PageResponse;
import br.com.deolhonacamara.api.model.NomeDaEntidadeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class NomeDaEntidadeRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PageResponse<NomeDaEntidadeEntity> findByPoliticianId(Integer politicianId, Pageable pageable) {

        String sql = """
            SELECT
                [colunas da tabela]
            FROM camara_deputados.nome_da_entidade
            WHERE politician_id = :politicianId
            ORDER BY [campo de ordenação] DESC
            LIMIT :limit OFFSET :offset
        """;

        String countSql = """
            SELECT COUNT(*)
            FROM camara_deputados.nome_da_entidade
            WHERE politician_id = :politicianId
        """;

        Map<String, Object> params = new HashMap<>();
        params.put("politicianId", politicianId);
        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        List<NomeDaEntidadeEntity> content = jdbcTemplate.query(sql, params, (rs, i) -> mapRow(rs));
        int total = jdbcTemplate.queryForObject(countSql, params, Integer.class);

        return new PageResponse<>(
                content,
                total,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
    }

    public void upsertNomeDaEntidade(NomeDaEntidadeEntity entity) {
        String sql = """
            INSERT INTO camara_deputados.nome_da_entidade (
                [colunas]
            ) VALUES (
                [valores com :parametros]
            )
            ON CONFLICT ([chave primaria]) DO UPDATE SET
                [colunas de update] = EXCLUDED.[colunas de update],
                updated_at = CURRENT_TIMESTAMP;
        """;

        Map<String, Object> params = new HashMap<>();
        // Mapear todos os parâmetros

        jdbcTemplate.update(sql, params);
    }

    private NomeDaEntidadeEntity mapRow(ResultSet rs) throws SQLException {
        return NomeDaEntidadeEntity.builder()
                // Mapear todos os campos
                .build();
    }
}
```

### Regras de Implementação

#### Nomenclatura
- **Entidade**: `NomeDaEntidadeEntity`
- **DTO**: `NomeDaEntidadeDto`
- **ResponseDTO**: `NomeDaEntidadeResponseDTO`
- **API**: `NomeDaEntidadeApi`
- **Controller**: `NomeDaEntidadeController`
- **Service**: `NomeDaEntidadeService`
- **Repository**: `NomeDaEntidadeRepository`

#### Estrutura da Tabela
- Schema: `camara_deputados`
- Nome da tabela: `nome_da_entidade` (snake_case)
- Colunas obrigatórias: `politician_id`, `created_at`, `updated_at`
- Primary Key: Geralmente `(politician_id, [outra_coluna])`

#### Mapper
- Usar `Mapper.INSTANCE` (MapStruct)
- Método: `toDto(NomeDaEntidadeEntity entity)`

#### Paginação
- Padrão: page=0, size=20
- Ordenação: Geralmente por data decrescente
- Retorno: `PageResponse<NomeDaEntidadeEntity>`

#### Tratamento de Parâmetros Opcionais
- `page`: default 0 se null
- `size`: default 20 se null
- Outros filtros: implementar conforme necessidade usando `InputBuilder` pattern

### Quando NÃO Usar Este Padrão

Este padrão **NÃO** deve ser usado para:
- Operações de autenticação/autorização
- Gerenciamento de dispositivos/tokens
- Operações de seguir/deixar de seguir
- Sincronização de dados externos
- Endpoints administrativos
- Operações CRUD básicas de usuários

Para estes casos, implementar padrões específicos conforme necessidade.

### Exemplo Prático de Criação

Para criar um fluxo de "Eventos" do político:

1. **Informar apenas**: "Criar fluxo de eventos do político"
2. **Será criado automaticamente**:
   - `EventController` com `getPoliticianEvents()`
   - `EventService` com `getByPoliticianId()`
   - `EventRepository` com `findByPoliticianId()`
   - Tabela: `camara_deputados.events`
   - DTOs gerados automaticamente pelo OpenAPI

---

## Conclusão

O padrão Speech estabelece uma arquitetura consistente e reutilizável para endpoints que retornam dados paginados de políticos. Fluxos que seguem este padrão garantem:

- **Consistência**: Mesmo contrato de API
- **Reutilização**: Código boilerplate padronizado
- **Manutenibilidade**: Estrutura previsível
- **Performance**: Paginação otimizada
- **Testabilidade**: Estrutura conhecida

Para novos fluxos, basta informar a entidade desejada e seguir as regras estabelecidas neste documento.
