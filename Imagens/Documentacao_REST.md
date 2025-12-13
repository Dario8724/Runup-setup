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
````

### Response
```json
{
  "userId": 1,
  "nome": "João Silva",
  "email": "joao@email.com"
}
````
## POST /api/auth/login
Autentica um utilizador.

### Request
```json
{
  "email": "joao@email.com",
  "senha": "123456"
}
````
### Response
```json
{
  "userId": 1,
  "nome": "João Silva",
  "email": "joao@email.com"
}
````

# CORRIDAS

## POST /api/corridas/gerar
Gera uma corrida personalizada.

### Request
```json
{
  "userId": 1,
  "routeName": "Corrida Matinal",
  "tipoAtividade": "RUN",
  "distanceKm": 5.0,
  "startLatitude": 38.722252,
  "startLongitude": -9.139337,
  "filtros": ["ENSOLARADO"]
}
````
### Response
```json
{
  "corridaId": 10,
  "userId": 1,
  "routeName": "Corrida Matinal",
  "tipoAtividade": "RUN",
  "distanceKm": 5.02,
  "estimatedDurationSeconds": 1500,
  "paceMinPerKm": 5.0,
  "estimatedCalories": 420,
  "totalElevationGain": 38.5,
  "dataCriacao": "2025-06-01",
  "weatherCondition": "Sunny",
  "ensolaradaAtendida": true
}
````

## GET /api/corridas/historico?userId={userId}
Lista o histórico de corridas de um utilizador.

### Response
```json
[
  {
    "corridaId": 10,
    "data": "2025-06-01",
    "distanciaKm": 5.02,
    "duracaoSegundos": 1500,
    "paceMinPorKm": 5.0,
    "kcal": 420,
    "tipo": "RUN",
    "routeName": "Corrida Matinal",
    "totalElevationGain": 38.5
  }
]
```

## POST /api/corridas/{id}/finalizar
Finaliza uma corrida.

### Request
```json
{
  "userId": 1,
  "distanciaRealKm": 5.12,
  "duracaoSegundos": 1480,
  "kcal": 435
}
```

### Response

204 No Content

# ROTAS PREDEFINIDAS

## GET /api/rotas/predefinidas
Lista rotas predefinidas.

### Response
```json
[
  {
    "rotaId": 1,
    "nome": "Parque das Nações",
    "distanciaKm": 5.0,
    "tipo": "Corrida",
    "dificuldade": "Fácil",
    "descricao": "Rota plana à beira-rio."
  }
]
````

## POST /api/rotas/predefinidas/{rotaId}/iniciar
Inicia uma corrida a partir de uma rota predefinida.

### Response
```json
{
  "corridaId": 10,
  "userId": 1,
  "routeName": "Parque das Nações",
  "tipoNome": "Corrida",
  "distanceKm": 5.0,
  "tempoSegundos": 1500,
  "paceMinPorKm": 5.0,
  "kcal": 420,
  "elevacaoTotal": 35.2,
  "data": "2025-06-01"
}
```

# METAS
## GET /api/goals/{userId}
Obtém metas do utilizador.

### Response
```json
[
  {
    "nome": "Meta Semanal",
    "total": 20.0,
    "progresso": 12.5
  }
]
```

## PUT /api/goals/users/{userId}
Atualiza metas do utilizador.

### Response
```json
{
  "metaSemanalKm": 25.0,
  "metaMensalKm": 100.0
}
```

# PERFIL DO UTILIZADOR

## GET /api/usuario/{id}/profile
Obtém o perfil do utilizador.

### Response
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "dataNascimento": "1995-08-20",
  "peso": 72.5,
  "altura": 1.78,
  "sexo": "M",
  "idade": 29
}
```

## PUT /api/usuario/{id}/profile
Atualiza o perfil do utilizador.

## DELETE /api/usuario/{id}
Remove um utilizador.

# ESTATÍSTICAS

## GET /api/usuario/{id}/stats
### Response
```json
{
  "totalCorridas": 42,
  "totalKm": 215.6,
  "totalTempoSegundos": 68400
}
```

## GET /api/usuario/{id}/weekly-stats
### Response
```json
{
  "distanciaTotalKm": 24.8,
  "caloriasTotais": 1980,
  "tempoTotalSegundos": 8400
}
```

## GET /api/usuario/{id}/records/distance
### Response
```json
{
  "maiorDistanciaKm": 12.5,
  "dataCorrida": "2025-05-12",
  "corridaId": 27
}
```

## GET /api/usuario/{id}/today-summary
### Response
```json
{
  "distanciaTotalKm": 6.4,
  "tempoTotalSegundos": 2100,
  "paceMedioSegundosPorKm": 328.1,
  "caloriasTotais": 520
}
```

# Conclusão

Este backend implementa uma API REST organizada segundo o padrão MVC, permitindo a gestão completa de utilizadores, corridas, metas e estatísticas, cumprindo os requisitos funcionais do projeto mobile.

