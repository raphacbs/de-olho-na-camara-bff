# Contexto do Projeto — De Olho na Câmara

Este documento resume o **contexto de produto e domínio** do projeto.  
Para stack, arquitetura técnica e setup detalhado, consulte o `README.md` (fonte de verdade técnica).

---

## 📌 Visão do Produto

**De Olho na Câmara** é uma plataforma para monitorar a atuação de deputados federais de forma contínua e personalizada, com dados sincronizados da API pública da Câmara.

O objetivo é reduzir fricção para cidadãos acompanharem seus representantes por meio de:

- consultas rápidas e filtros úteis;
- histórico de atividades parlamentares;
- acompanhamento de políticos seguidos;
- base para notificações e análises futuras.

---

## 🎯 Objetivos do Sistema

### Objetivo principal

Oferecer uma API confiável para acompanhamento de parlamentares e suas atividades.

### Objetivos secundários

- manter sincronização recorrente de dados oficiais;
- permitir personalização por usuário (seguindo políticos);
- suportar evolução contínua do produto sem romper clientes;
- manter base organizada para futuras features analíticas.

---

## 🧱 Contexto de Arquitetura (alto nível)

No nível de produto, o sistema opera com:

- backend BFF em camadas;
- integração com a API da Câmara dos Deputados;
- persistência em PostgreSQL com versionamento de schema;
- jobs agendados para sincronização periódica de dados.

> Detalhes técnicos atualizados (dependências, camadas de código, endpoints e comandos) ficam centralizados no `README.md`.

---

## 🔄 Operação e Evolução

O projeto evolui com foco em:

- melhoria de observabilidade dos jobs de sincronização;
- expansão de cobertura funcional (mais recortes e visões de dados);
- robustez para onboarding de novos contribuidores por documentação coesa.

Arquivos `RESUMO_*.md` permanecem como histórico de entregas pontuais e podem refletir o estado da época de cada implementação.

---

## 📂 Repositório

https://github.com/raphacbs/de-olho-na-camara-bff
