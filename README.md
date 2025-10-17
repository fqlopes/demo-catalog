# Spring Boot: Catálogo com OAuth2

Projeto **Spring Boot 3.5.5**, catálogo simples de produtos e categorias com autenticação **OAuth2**. Arquétipo REST seguro com banco relacional.

## Tecnologias

* Java 17, Spring Boot 3.5.5 (Web, Data JPA, Security + OAuth2)
* H2 (teste) / PostgreSQL (runtime)
* Lombok, Maven

## Entidades

* **User ↔ Role**: N:N, controle de perfis
* **Product ↔ Category**: N:N, catálogo de produtos

## Segurança

* Authorization Server próprio, JWT, Password Grant
* Configuração: `client-id`, `client-secret`, `jwt.duration`, `cors.origins`

## Banco

* **test** → H2 (memória)
* **dev/prod** → PostgreSQL (persistente)

H2 console: `http://localhost:8080/h2-console`

## Endpoints principais

* `GET /products`, `GET /categories`
* `POST /oauth2/token`
* `GET /users/me`

**Objetivo:** modelo didático de REST seguro em Spring Boot.

