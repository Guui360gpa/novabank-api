# 🏦 NovaBank API

API REST de um banco digital desenvolvida com **Java 21** e **Spring Boot**, simulando as principais operações de uma conta digital: cadastro de clientes, movimentações financeiras, transferências via PIX e consulta de extrato.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-database-blue)
![Swagger](https://img.shields.io/badge/OpenAPI-Swagger-85EA2D)

<!-- CONFIRMAR: versão do Spring Boot no pom.xml (ajuste o badge acima) -->

---

## 📑 Sumário

- [Sobre o projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Arquitetura](#-arquitetura)
- [Modelagem](#-modelagem)
- [Como executar](#-como-executar)
- [Documentação da API](#-documentação-da-api)
- [Endpoints](#-endpoints)
- [Regras de negócio](#-regras-de-negócio)
- [Tratamento de erros](#-tratamento-de-erros)
- [Testes](#-testes)
- [Próximos passos](#-próximos-passos)
- [Autor](#-autor)

---

## 📖 Sobre o projeto

O **NovaBank** é um projeto back-end que reproduz o núcleo de um banco digital. Ele permite criar clientes e contas, realizar depósitos, saques e transferências PIX, gerenciar chaves PIX e consultar o histórico de transações.

O objetivo foi praticar boas práticas de desenvolvimento com Spring Boot: arquitetura em camadas, validação de dados, persistência com JPA e documentação automática da API com OpenAPI.

## 📌 Funcionalidades

- 👤 **Clientes:** cadastro e consulta de clientes
- 💳 **Contas:** criação e gerenciamento de contas bancárias
- ⬇️ **Depósito:** crédito de valores na conta
- ⬆️ **Saque:** débito de valores, respeitando o saldo disponível
- ⚡ **Transferência PIX:** envio de valores entre contas usando chave PIX
- 🔑 **Chaves PIX:** cadastro e gerenciamento de chaves por conta
- 🧾 **Extrato:** consulta de movimentações da conta
- 🕘 **Histórico de transações:** registro de todas as operações realizadas

## 🚀 Tecnologias

| Tecnologia | Uso |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot** | Framework da aplicação |
| **Spring Data JPA / Hibernate** | Persistência e mapeamento objeto-relacional |
| **PostgreSQL** | Banco de dados relacional |
| **Bean Validation** | Validação dos dados de entrada |
| **Lombok** | Redução de código repetitivo |
| **Swagger / OpenAPI** | Documentação interativa da API |
| **Maven** | Gerenciamento de dependências e build |

## 🏗️ Arquitetura

O projeto segue a arquitetura em camadas:

```
Controller  →  Service  →  Repository  →  Database
```

| Camada | Responsabilidade |
|---|---|
| **Controller** | Expõe os endpoints REST e recebe/retorna DTOs |
| **Service** | Concentra as regras de negócio |
| **Repository** | Acesso a dados via Spring Data JPA |
| **Database** | PostgreSQL |

O diagrama de classes está disponível em [`apibancaria.plantuml`](./apibancaria.plantuml).

<!-- CONFIRMAR: se existir pacote dto/exception/config, adicionar linhas na tabela acima -->

## 🗂️ Modelagem

Principais entidades do domínio:

<!-- CONFIRMAR: ajuste conforme as entidades reais. Sugestão baseada nas funcionalidades: -->

| Entidade | Descrição |
|---|---|
| **Cliente** | Titular da conta |
| **Conta** | Conta bancária com saldo, vinculada a um cliente |
| **ChavePix** | Chave PIX vinculada a uma conta |
| **Transação** | Registro de depósito, saque ou transferência |

## ▶️ Como executar

### Pré-requisitos

- [Java 21+](https://adoptium.net/)
- [Maven 3.9+](https://maven.apache.org/) (ou o wrapper `./mvnw`, se disponível)
- [PostgreSQL](https://www.postgresql.org/) em execução

### Passo a passo

**1. Clone o repositório**

```bash
git clone https://github.com/Guui360gpa/novabank-api.git
cd novabank-api/apibancaria
```

**2. Crie o banco de dados**

```sql
CREATE DATABASE novabank;
```

**3. Configure a conexão** em `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/novabank
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
```

<!-- CONFIRMAR: nome do banco e demais propriedades usadas no seu application.properties -->

> 💡 Evite versionar credenciais reais. Use variáveis de ambiente, por exemplo `${DB_USER}` e `${DB_PASSWORD}`.

**4. Execute a aplicação**

```bash
mvn spring-boot:run
```

A API estará disponível em **http://localhost:8080**.

## 📚 Documentação da API

Com a aplicação em execução, a documentação interativa fica em:

👉 **http://localhost:8080/swagger-ui/index.html**

<!-- CONFIRMAR: caminho do Swagger (pode ser /swagger-ui.html) -->

## 🔌 Endpoints

<!-- CONFIRMAR: preencha com as rotas reais dos seus controllers. Modelo: -->

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/clientes` | Cadastra um cliente |
| `GET` | `/clientes/{id}` | Consulta um cliente |
| `POST` | `/contas` | Cria uma conta |
| `POST` | `/contas/{id}/deposito` | Realiza um depósito |
| `POST` | `/contas/{id}/saque` | Realiza um saque |
| `POST` | `/pix/chaves` | Cadastra uma chave PIX |
| `POST` | `/pix/transferencias` | Realiza uma transferência PIX |
| `GET` | `/contas/{id}/extrato` | Consulta o extrato |

### Exemplo de requisição

```http
POST /pix/transferencias
Content-Type: application/json

{
  "chaveOrigem": "...",
  "chaveDestino": "...",
  "valor": 150.00
}
```

<!-- CONFIRMAR: ajuste o corpo conforme seu DTO -->

## 📏 Regras de negócio

<!-- CONFIRMAR: mantenha só as regras que realmente estão implementadas -->

- O saque e a transferência não podem exceder o saldo disponível
- Valores de depósito, saque e transferência devem ser positivos
- Uma chave PIX é única no sistema
- Toda operação financeira gera um registro no histórico de transações

## ⚠️ Tratamento de erros

<!-- CONFIRMAR: se houver @RestControllerAdvice, descreva o formato. Exemplo: -->

Erros de negócio e de validação retornam respostas padronizadas com o status HTTP adequado (`400`, `404`, `409`, `422`) e uma mensagem descritiva.

## 🧪 Testes

<!-- CONFIRMAR: se houver testes, mantenha o comando; se não, mova para "Próximos passos" -->

```bash
mvn test
```

## 🔭 Próximos passos

- [ ] Autenticação e autorização com Spring Security + JWT
- [ ] Testes unitários e de integração
- [ ] Containerização com Docker e Docker Compose
- [ ] Migrations de banco com Flyway
- [ ] Deploy em ambiente público

## 👨‍💻 Autor

**Guilherme Paiva Alves**

[![GitHub](https://img.shields.io/badge/GitHub-Guui360gpa-181717?logo=github)](https://github.com/Guui360gpa)
