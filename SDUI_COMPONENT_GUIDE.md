# Guia de Desenvolvimento: Adicionando Novos Tipos de Componentes SDUI

## Visão Geral

Este documento descreve o processo para adicionar novos tipos de componentes ao sistema Server-Driven UI (SDUI) do projeto "De Olho na Câmara".

## Processo de Adição de Novo Tipo de Componente

### 1. Modificar a Especificação OpenAPI

**Arquivo:** `src/main/resources/swagger.yaml`

**Localização:** Esquema `SDUIComponent`, propriedade `type`

**Ação:** Adicionar o novo tipo ao enum da propriedade `type`:

```yaml
SDUIComponent:
  type: object
  properties:
    type:
      type: string
      enum: [Container, TextBlock, Button, Card, Image, Spacer, Input, Avatar, NOVO_TIPO_AQUI]
      example: Container
```

**Exemplo prático:**
```yaml
enum: [Container, TextBlock, Button, Card, Image, Spacer, Input, Avatar, Badge]
```

### 2. Regenerar o Código Java

Execute o comando Maven para regenerar os arquivos gerados pelo OpenAPI:

```bash
mvn clean compile
```

Este comando irá:
- Gerar o arquivo `SDUIComponent.java` atualizado
- Incluir o novo tipo no enum `TypeEnum`
- Atualizar as classes compiladas em `target/classes/`

### 3. Verificar a Geração do Código

**Arquivo gerado:** `target/generated-sources/openapi/src/main/java/net/coelho/deolhonacamara/api/model/SDUIComponent.java`

Verifique se o enum `TypeEnum` foi atualizado:

```java
public enum TypeEnum {
    CONTAINER("Container"),
    TEXT_BLOCK("TextBlock"),
    BUTTON("Button"),
    CARD("Card"),
    IMAGE("Image"),
    SPACER("Spacer"),
    INPUT("Input"),
    AVATAR("Avatar"),
    BADGE("Badge");  // ← Novo tipo adicionado
    // ...
}
```

### 4. Copiar Arquivo para Classes Compiladas (Opcional)

Se necessário, copie o arquivo gerado para o diretório de classes:

```bash
cp target/generated-sources/openapi/src/main/java/net/coelho/deolhonacamara/api/model/SDUIComponent.java \
   target/classes/net/coelho/deolhonacamara/api/model/SDUIComponent.java
```

### 5. Implementar Factory do Componente (Se necessário)

Se o novo componente requer lógica específica de criação, implementar um factory correspondente:

**Localização:** `src/main/java/br/com/deolhonacamara/api/service/sdui/components/`

**Exemplo:** Para um componente `Badge`, criar `BadgeFactory.java`:

```java
package br.com.deolhonacamara.api.service.sdui.components;

import net.coelho.deolhonacamara.api.model.SDUIComponent;
import org.springframework.stereotype.Component;

@Component
public class BadgeFactory implements SDUIComponentFactory {

    @Override
    public SDUIComponent createComponent(ComponentRequest request) {
        return new SDUIComponent()
            .id(request.getId())
            .type(SDUIComponent.TypeEnum.BADGE)
            .text(request.getText())
            // ... outras propriedades específicas do badge
            .build();
    }

    @Override
    public boolean supports(SDUIComponent.TypeEnum type) {
        return SDUIComponent.TypeEnum.BADGE.equals(type);
    }
}
```

### 6. Registrar Factory no SDUIComponentFactoryImpl

**Arquivo:** `SDUIComponentFactoryImpl.java`

Adicionar o novo factory à lista de factories suportadas:

```java
@Autowired
private BadgeFactory badgeFactory;

// ...

@Override
public SDUIComponent createComponent(ComponentRequest request) {
    return switch (request.getType()) {
        case CONTAINER -> containerFactory.createComponent(request);
        case TEXT_BLOCK -> textBlockFactory.createComponent(request);
        case BUTTON -> buttonFactory.createComponent(request);
        case CARD -> cardFactory.createComponent(request);
        case IMAGE -> imageFactory.createComponent(request);
        case SPACER -> spacerFactory.createComponent(request);
        case INPUT -> inputFactory.createComponent(request);
        case AVATAR -> avatarFactory.createComponent(request);
        case BADGE -> badgeFactory.createComponent(request);  // ← Novo factory
    };
}
```

### 7. Implementar Strategy de Tela (Se necessário)

Se o novo componente for usado em telas específicas, implementar ou atualizar as strategies de tela:

**Localização:** `src/main/java/br/com/deolhonacamara/api/service/sdui/screens/`

**Exemplo:** Para usar o componente em uma tela específica, modificar a strategy correspondente.

### 8. Testar a Implementação

1. **Compilar o projeto:**
   ```bash
   mvn clean compile
   ```

2. **Executar testes:**
   ```bash
   mvn test
   ```

3. **Testar manualmente:**
   - Fazer requisição para endpoint SDUI
   - Verificar se o novo tipo é aceito
   - Validar renderização no frontend

## Regras e Boas Práticas

### Nomenclatura
- **Enum no OpenAPI:** `PascalCase` (ex: `TextBlock`, `Image`, `Avatar`)
- **Valor serializado:** Mesmo valor do enum (ex: `"Avatar"`)
- **Classe Java:** `UPPER_SNAKE_CASE` (ex: `AVATAR`)
- **Factory:** `NomeDoComponenteFactory` (ex: `AvatarFactory`)

### Propriedades do Componente
- Reutilizar propriedades existentes quando possível
- Adicionar novas propriedades apenas se necessário
- Documentar todas as propriedades no OpenAPI

### Factory Pattern
- Cada componente deve ter seu próprio factory
- Implementar interface `SDUIComponentFactory`
- Método `supports()` deve retornar `true` apenas para o tipo específico

### Tratamento de Erros
- Validar parâmetros obrigatórios
- Fornecer valores padrão quando apropriado
- Logar erros de criação de componentes

## Exemplo Completo: Adicionando Componente "Badge"

### Passo 1: Modificar swagger.yaml
```yaml
enum: [Container, TextBlock, Button, Card, Image, Spacer, Input, Avatar, Badge]
```

### Passo 2: Regenerar código
```bash
mvn clean compile
```

### Passo 3: Criar BadgeFactory.java
```java
@Component
public class BadgeFactory implements SDUIComponentFactory {

    @Override
    public SDUIComponent createComponent(ComponentRequest request) {
        return new SDUIComponent()
            .id(SDUIIdGenerator.generateId())
            .type(SDUIComponent.TypeEnum.BADGE)
            .text(request.getText())
            .backgroundColor(request.getBackgroundColor() != null ? request.getBackgroundColor() : "#007AFF")
            .color(request.getColor() != null ? request.getColor() : "#FFFFFF")
            .size(request.getSize() != null ? request.getSize() : SDUIComponent.SizeEnum.SMALL)
            .build();
    }

    @Override
    public boolean supports(SDUIComponent.TypeEnum type) {
        return SDUIComponent.TypeEnum.BADGE.equals(type);
    }
}
```

### Passo 4: Registrar no SDUIComponentFactoryImpl
```java
@Autowired
private BadgeFactory badgeFactory;

// No método createComponent:
case BADGE -> badgeFactory.createComponent(request);
```

## Troubleshooting

### Problema: Tipo não aparece no enum TypeEnum
**Solução:** Verificar se a modificação no `swagger.yaml` foi salva e executar `mvn clean compile`.

### Problema: Factory não é reconhecido
**Solução:** Verificar se a classe está anotada com `@Component` e se foi registrada no `SDUIComponentFactoryImpl`.

### Problema: Erro de compilação
**Solução:**
1. Limpar target: `mvn clean`
2. Regenerar: `mvn compile`
3. Verificar se não há erros de sintaxe no `swagger.yaml`

## Conclusão

Seguindo este guia, novos tipos de componentes SDUI podem ser adicionados de forma consistente e mantível. O processo garante que:

- A API permanece consistente
- O código gerado está sempre atualizado
- Os factories seguem o padrão estabelecido
- A manutenção é facilitada
- Os testes podem ser executados adequadamente

Para dúvidas ou problemas específicos, consulte a documentação do OpenAPI Generator ou os padrões estabelecidos no projeto.