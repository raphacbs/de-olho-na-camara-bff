# Contexto Completo do Projeto **De Olho na Câmara**

Este documento reúne todo o contexto atual do projeto **De Olho na Câmara**, incluindo objetivos, arquitetura, funcionalidades implementadas e planejadas, além das integrações e estratégias futuras.

---

## 📌 **Visão Geral do Projeto**
**De Olho na Câmara** é um sistema cujo objetivo é acompanhar o desempenho e comportamento de políticos, permitindo que cidadãos monitorem seus representantes com facilidade. A plataforma fornece dados atualizados diariamente, armazenados em um banco de dados próprio para permitir análises, histórico e funcionalidades personalizadas.

O projeto segue uma arquitetura composta por:
- **Backend em Spring Boot (BFF)**
- **Banco de dados PostgreSQL**
- **Atualização diária de dados via Scheduler**
- **Tabela principal:** `politician`
- **Tabela de relação com usuário:** `politician_user`
- **Integração com a API da Câmara dos Deputados**

---

## 🎯 **Objetivos do Sistema**
### Objetivo principal
Criar um serviço que permita monitorar deputados de forma contínua e personalizada.

### Objetivos secundários
- Atualizar diariamente os dados oficiais dos parlamentares.
- Registrar quais políticos cada usuário segue.
- Possibilitar futuras análises, comparações e alertas.
- Tornar o sistema intuitivo, memorável e confiável.

---

## 🧱 **Arquitetura Atual**
### 🔹 **Tecnologias utilizadas**
- Java 17
- Spring Boot 3
- Spring Scheduler
- Spring Web
- Spring Data JPA
- PostgreSQL
- Docker
- Lombok

### 🔹 **Camadas do BFF**
- `controller`
- `service`
- `client`
- `entity`
- `repository`
- `scheduler`
- `mapper`
- `config`

---

## 🗃️ **Modelagem do Banco de Dados**
### Tabela `politician`
Contém informações oficiais do parlamentar, como:
- ID na Câmara
- Nome
- Partido
- UF
- Status
- Foto
- Atualizado_em

A atualização diária mantém as informações sincronizadas com a API pública.

### Tabela `politician_user`
Relaciona usuários com políticos acompanhados.

Estrutura típica:
- `id`
- `user_id`
- `politician_id`
- `created_at`

---

## 🔄 **Atualização Diária via Scheduler**
Você implementou um scheduler que:
1. Consulta a API da Câmara
2. Converte os dados para o formato interno (DTO → Entity)
3. Executa `INSERT ON CONFLICT` para atualizar mudanças
4. Garante idempotência

Esse processo mantém a tabela `politician` sempre atualizada.

### Próximos schedulers sugeridos
- Scheduler para carregar **detalhes adicionais** (gastos, presenças, votações)
- Scheduler para armazenar **histórico diário** (opcional e valioso)
- Scheduler para atualizar **partidos** e **frentes parlamentares**

---

## 🔌 **Integração com a API da Câmara dos Deputados**
Você já possui:
- `CamaraClient`
- DTOs adequados
- Conversores para entidades

Possíveis endpoints futuros:
- `/deputados/{id}/despesas`
- `/deputados/{id}/votacoes`
- `/deputados/{id}/frentes`

Essas informações podem expandir bastante o valor da plataforma.

---

## 📈 **Funcionalidades Futuras Planejadas**
### 🔹 Curto prazo
- Rota para seguir e deixar de seguir políticos
- Retornar detalhes de um político usando dados já armazenados
- Dashboard básico

### 🔹 Médio prazo
- Histórico de gastos
- Histórico de posição em votações
- Ranking de políticos por categoria

### 🔹 Longo prazo
- Notificações push
- Monitoramento personalizado
- IA para resumir comportamento recente dos políticos
- App mobile dedicado

---

## 🧠 **Sugestões de Nome Memorável (Estilo Reclame Aqui)**
Além de **De Olho na Câmara**, outros nomes fortes:
- **Fiscaliza Aí**
- **PoliWatch**
- **Quem Me Representa**
- **Radar Político**
- **De Olho neles**
- **Vigia Brasília**
- **Fala Deputado**

O nome atual é excelente, mas caso queira maior apelo viral, "Fiscaliza Aí" e "Radar Político" funcionam muito bem.

---

## ✔️ **Status Atual do Projeto**
- Estrutura do BFF está correta e organizada
- Scheduler principal implementado corretamente
- Uso correto de `RestClient`
- Código limpo (boa separação de responsabilidades)
- Projeto pronto para expansão

---

## 🚀 **Próximos passos recomendados**
1. Criar endpoints REST para listar políticos
2. Criar endpoint para o usuário seguir políticos
3. Criar scheduler secundário com dados adicionais (gastos, votações)
4. Criar logs mais detalhados nos schedulers
5. Adicionar testes unitários e integração
6. Configurar Swagger/OpenAPI
7. Configurar GitHub Actions para build e validações

---

## 📂 **Link do Repositório**
https://github.com/raphacbs/de-olho-na-camara-bff

---

Se quiser, posso gerar:
- documentação Swagger
- diagrama de classes
- diagrama de arquitetura
- template README.md profissional
- roadmap

Só pedir! 😊