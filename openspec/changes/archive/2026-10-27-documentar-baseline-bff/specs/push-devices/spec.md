## ADDED Requirements

### Requirement: Registrar dispositivo FCM

O sistema SHALL expor `POST /api/v1/devices` para usuário autenticado, aceitando token FCM e persistindo em `user_device` para envio futuro de notificações push.

#### Scenario: Novo dispositivo

- **WHEN** o cliente envia `fcmToken` válido com JWT
- **THEN** o sistema associa o dispositivo ao usuário

#### Scenario: Atualização de token

- **WHEN** o mesmo dispositivo envia um token atualizado
- **THEN** o sistema atualiza o registro existente conforme regra de negócio do serviço

### Requirement: Remover dispositivo

O sistema SHALL expor `DELETE /api/v1/devices` com query `fcmToken`, removendo o vínculo do dispositivo para o usuário autenticado.

#### Scenario: Logout ou desinstalação

- **WHEN** o cliente chama DELETE com o token FCM a remover
- **THEN** o sistema remove o registro correspondente
