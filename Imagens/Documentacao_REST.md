# RunUp Backend API

## Descrição do Projeto
O **RunUp** é uma aplicação backend desenvolvida em **Java com Spring Boot**, cujo objetivo é apoiar utilizadores na prática de corrida e caminhada, permitindo:

- Registo e autenticação de utilizadores
- Geração de rotas personalizadas
- Utilização de rotas predefinidas
- Registo e finalização de corridas
- Definição e acompanhamento de metas
- Consulta de estatísticas e progresso do utilizador

Este projeto foi desenvolvido no contexto académico e segue o padrão **MVC (Model–View–Controller)**.

---

## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- MySQL
- Maven
- Arquitetura REST

---

## Base URL
http://localhost:8080


Todas as rotas da API retornam dados no formato **JSON**.

---

## Códigos HTTP Utilizados
- **200 OK** – operação realizada com sucesso  
- **201 Created** – recurso criado  
- **204 No Content** – operação realizada sem retorno de corpo  
- **400 Bad Request** – dados inválidos  
- **401 Unauthorized** – não autorizado  
- **404 Not Found** – recurso não encontrado  
- **409 Conflict** – conflito de dados  
- **500 Internal Server Error** – erro interno do servidor  

---

# AUTHENTICATION

## POST /api/auth/register
Regista um novo utilizador.

### Request
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "123456",
  "dataDeNascimento": "1995-08-20",
  "sexo": "M",
  "peso": 72.5,
  "altura": 1.78,
  "experiencia": "INICIANTE"
}

