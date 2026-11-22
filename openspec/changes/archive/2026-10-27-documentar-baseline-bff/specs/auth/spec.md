## ADDED Requirements

### Requirement: Registro de usuário

O sistema SHALL aceitar registro com e-mail, senha e nome completo em `POST /api/v1/auth/register`, persistir o usuário no schema `authentication` com senha hasheada (BCrypt) e retornar resposta de sucesso conforme contrato OpenAPI.

#### Scenario: Registro com dados válidos

- **WHEN** o cliente envia e-mail, senha e nome completo válidos
- **THEN** o sistema cria o usuário e responde com status de sucesso definido no OpenAPI

### Requirement: Login e emissão de tokens

O sistema SHALL autenticar credenciais em `POST /api/v1/auth/login` e, em caso de sucesso, retornar `accessToken`, `refreshToken`, `tokenType` (Bearer) e tempo de expiração do access token.

#### Scenario: Login bem-sucedido

- **WHEN** e-mail e senha correspondem a um usuário existente
- **THEN** o sistema retorna tokens JWT válidos para uso nas demais rotas

#### Scenario: Login inválido

- **WHEN** as credenciais não são válidas
- **THEN** o sistema rejeita a autenticação sem emitir token

### Requirement: Rotas públicas de autenticação

O sistema SHALL permitir acesso anônimo apenas a registro, login, documentação Swagger/OpenAPI e endpoints de auth auxiliares configurados em `SecurityConfig` (`/api/v1/auth/register`, `/api/v1/auth/login`, refresh/forgot-password quando existirem).

#### Scenario: Acesso sem token a login

- **WHEN** o cliente chama `POST /api/v1/auth/login` sem header Authorization
- **THEN** a requisição é processada normalmente

### Requirement: Proteção das demais rotas

O sistema SHALL exigir JWT válido no header `Authorization: Bearer` para todas as requisições que não estejam na lista `permitAll`.

#### Scenario: API protegida sem token

- **WHEN** o cliente chama um endpoint autenticado sem Bearer token
- **THEN** o sistema responde não autorizado via `JwtAuthenticationEntryPoint`
