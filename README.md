# RunUp

**Licenciatura em Engenharia Informática | LEIF02 | 24-25**  
**UC:** Projeto de Desenvolvimento Móvel  
**Docente:** Pedro Miguel Gomes Silva Rosa 

**Link Repósitorio:** [https://github.com/Dario8724/RunUp](https://github.com/Dario8724/RunUp)

**Grupo 2** **Turma D02**

**Autores:**
- Gabriel Lima Rezende - 20240343  
- Dário Biaguê Bandanhe – 20241751  
- Edmilson Alberto Marcos Tudo – 20241542  
- Francisco Rocha Zolana – 20240801  

**Palavras Chave**
Corrida, Caminhada, RunUp, Saúde, Fitness

---

## Índice
- [Descrição da App](#descrição-da-app)
- [Requisitos Funcionais e Não Funcionais](#requisitos-funcionais-e-não-funcionais)
- [Objetivos e Motivação](#descrição-dos-objetivos-e-da-motivação-do-trabalho)
- [Público-Alvo](#identificação-de-público-alvo)
- [Pesquisa de Mercado](#pesquisa-de-mercado)
- [Guiões Preliminares](#uma-versão-preliminar-de-três-guiões)
- [Diagrama de classes](#diagrama-de-classes)
- [Documentação REST](#documentação-rest)
- [Dicionario de dados](#dicionário-de-dados)
- [Estutura de dados](#estrutura-dos-dados)
- [Solução a Implementar](#descrição-da-solução-a-implementar)
- [Planeamento e Calendarização](#planeamento-e-calendarização)
- [Mockup da Aplicação](#mockup-da-aplicação)
- [Conclusão](#conclusão)
- [Poster do Aplicativo](#poster)
- [Bibliografia](#bibliografia)

---

## Descrição da App

O nosso grupo pretende desenvolver uma aplicação de corridas com o foco na comunidade de corredores e praticantes de desporto para o controle e gestão dos seus treinos, com o foco principal em corredores e caminhantes iniciantes na criação de rotas e armazenamentos de treinos anteriores.

**Funcionalidades previstas:**
- Criar rotas personalizadas (filtros: km, elevação, parques, praia, sol, etc.)
- Histórico de corridas
- Sugestão de metas para iniciantes
- Corrida ou caminhada
- Gravação de treinos (pace, tempo, recordes, etc.)
- Comunidade para partilha de corridas
- Registo de utilizadores
- Histórico de elevação
- Definir metas pessoais

Pretende-se com isso diminuir a falta de variedade e dificuldades no planejamento de distâncias e percursos nos treinos, sugerindo rotas/ percursos novas personalizadas para os utilizadores e calculando a quilometragem desejada. Também pretendemos aumentar o engajamento social entre os utilizadores através da aba da comunidade.

---

## Requisitos Funcionais e Não Funcionais

**Funcionais:**
- Criar rotas personalizadas  
- Registrar histórico de corridas  
- Sugerir metas  
- Selecionar tipo de atividade  
- Gravar estatísticas  
- Aba da comunidade  
- Definir metas pessoais  

**Não Funcionais:**
- Interface responsiva  
- Baixo consumo de bateria  
- Segurança de dados  
- Permissões de sistema  
- Escalabilidade  
- Privacidade  

---

## Descrição dos Objetivos e da Motivação do Trabalho

A prática regular da caminhada e corrida é um dos mais simples e acessíveis modos de se manter uma vida saudável, ajudando a: diminuir a inatividade; prevenir doenças cardiovasculares; minimizar o estresse e a ansiedade; aumentar a qualidade do sono e o humor. O projeto oferece incentivo a estes hábitos de maneira prática, controlada e motivadora.
Acompanhamento de desempenho e progresso, a falta de progresso é um dos fatores que afastam as pessoas da atividade. O projeto pode contribuir com isso ao: Ressaltar as métricas (distância, tempo, velocidade, calorias); proporcionar comparações com atividades anteriores; propor desafios pessoais e em grupo. Esse acompanhamento estimula a continuidade da atividade, visto que os usuários conseguem visualizar sua progressão ao longo do tempo. Inclusão digital e acessibilidade nos dias atuais 95% das pessoas possui um smartphone com GPS. O projeto utiliza essa tecnologia disponível para oferecer: Um sistema acessível (sem custos com equipamentos caros), fácil de usar por pessoas de todas as idades, possibilidade de participação em programas sociais, escolares ou comunitários, integração social e comunitária. 

---

## Identificação de Público-Alvo

- Corredores iniciantes e caminhantes  
- Pessoas que buscam saúde e lazer  
- Comunidade digital e social ligada ao desporto  

---

## Pesquisa de Mercado

Strava  Registra Rotas, Quilômetros, Pagina de Comunidade porém maioria das opções são pagas.

Adidas Running = Registra Quilômetros, Elevação, Kcal.

Nike Running = Faz tudo que os demais e define metas.

Marcas como PUMA,Under Armor e New Balance também possuem aplicações.

Nenhuma das aplicações são totalmente gratuitas, para conseguir acessar todas funcionalidades precisa pagar uma assinatura. 

---

## Uma Versão Preliminar de Três Guiões

1. **Novo usuário (23 anos, sedentário,estudante universitário, nunca correu antes, quer melhorar saúde):**
   
    • Cena: Ele baixa o app motivado por anúncios sobre “começar a correr de forma gradual”.
   
    •	Ação na app:
        o	Cria uma conta e responde um questionário inicial sobre nível de atividade. 
        o	A app sugere metas iniciais: caminhada leve de 2 km 3x por semana.
        o	Explora o filtro de rotas para encontrar rotas curtas, seguras e arborizadas. 
        o	Usa a aba de comunidade para ver depoimentos de outros iniciantes.
        o	Salva o histórico para acompanhar evolução semana a semana.
   
    •	Objetivo: Ganhar motivação, sentir-se seguro ao começar devagar, criar hábito. 

2. **Corredor experiente (35 anos, já corre 5x/semana, usa outros apps, mas quer algo mais completo e gratuito.):**
   
    •	Cena: Ele baixa a app depois de ver que há filtros de rota “à beira-mar” e “mais ensolaradas”.

    •	Ação na app: 
        o	Usa o filtro avançado para criar rotas longas (10–15 km) com paisagens variadas. 
        o	Testa a funcionalidade “criar sua própria rota” para um treino específico.
        o	Define metas de performance (tempo/km). 
        o	Interage na comunidade postando resultados e vendo desafios semanais.
   
    •	Objetivo: Avaliar se a app oferece mais personalização e motivação que os concorrentes.
   
3. **Corredor motivando amigo (28 anos, já corre há 3 anos; o amigo é sedentário, mas curioso.):**
   
    •	Cena: Encontro num parque. Ele abre a app para mostrar as funções ao amigo.
   
    •	Ação na app: 
        o	Mostra ao amigo os filtros de rota para iniciantes e caminhadas leves.
        o	Demonstra a aba de metas personalizadas para diferentes níveis. 
        o	Cria uma rota curta compartilhável para fazerem juntos. 
        o	Mostra a aba da comunidade, onde é possível acompanhar progresso e receber incentivos. 
        o	Agenda uma meta conjunta de corrida leve no fim de semana.
   
    •	Objetivo: Tornar a app atraente para o amigo e motivá-lo a começar com metas realistas.

---
![Persona1](../../Imagens/Persona1.png)

---
![Persona2](../../Imagens/Persona2.png)

---
## Diagrama de classes

classDiagram

    %% =======================
    %% ENTIDADES
    %% =======================
    class Usuario {
        +Long id
        +String nome
        +String email
        +String senha
        +LocalDateTime dataCriacao
    }
    class Meta {
        +Long id
        +String descricao
        +LocalDate dataInicio
        +LocalDate dataFim
        +Usuario usuario
    }
    class Corrida {
        +Long id
        +String nome
        +Double originLat
        +Double originLng
        +Double destLat
        +Double destLng
        +Double desiredDistanceKm
        +Boolean preferTrees
        +Boolean nearBeach
        +Boolean nearPark
        +Boolean sunnyRoute
        +Boolean avoidHills
        +String tipo
        +Usuario usuario
    }
    class Postagem {
        +Long id
        +String titulo
        +String conteudo
        +LocalDateTime dataPostagem
        +Usuario usuario
    }
    %% =======================
    %% SERVIÇOS E CONTROLADORES
    %% =======================
    class RouteService {
        +Route generateRoute(RouteRequest request)
        +List<Route> getSavedRoutes(Usuario usuario)
    }
    class RouteController {
        +ResponseEntity<Route> generateRoute(RouteRequest request)
    }
    %% =======================
    %% RELAÇÕES
    %% =======================
    Usuario "1" --> "*" Meta : possui >
    Usuario "1" --> "*" Corrida : realiza >
    Usuario "1" --> "*" Postagem : cria >
    RouteController --> RouteService : usa >
    RouteService --> Corrida : gera >
---
## Documentação REST
A seguir é apresentada a documentação da principal rota disponível na API RunUp.

Requisição
URL base:
http://localhost:8080/api/routes/generate

Método HTTP:
POST

Cabeçalhos (Headers):
Content-Type: application/json
Accept: application/json

Corpo (Body JSON):
{
  "nome": "Corrida na Foz",
  "originLat": 41.1540,
  "originLng": -8.6535,
  "destLat": 41.1501,
  "destLng": -8.6800,
  "desiredDistanceKm": 5,
  "preferTrees": true,
  "nearBeach": true,
  "nearPark": true,
  "sunnyRoute": true,
  "avoidHills": false,
  "tipo": "corrida"
}

Resposta de sucesso (200 OK)

Descrição:
Retorna uma rota gerada com base nas preferências enviadas.

Exemplo de resposta:

{
  "routeName": "Corrida na Foz",
  "totalDistanceKm": 5.1,
  "estimatedTimeMin": 32,
  "startPoint": {
    "lat": 41.1540,
    "lng": -8.6535
  },
  "endPoint": {
    "lat": 41.1501,
    "lng": -8.6800
  },
  "path": [
    { "lat": 41.1535, "lng": -8.6550 },
    { "lat": 41.1520, "lng": -8.6620 },
    { "lat": 41.1510, "lng": -8.6700 }
  ],
  "preferences": {
    "preferTrees": true,
    "nearBeach": true,
    "nearPark": true,
    "sunnyRoute": true,
    "avoidHills": false
  }
}

Respostas de erro: 

| Código                      | Tipo              | Descrição                                             |
| --------------------------- | ----------------- | ----------------------------------------------------- |
| 400 Bad Request           | Erro de validação | Dados incompletos ou inválidos no corpo da requisição |
| 500 Internal Server Error | Erro no servidor  | Falha ao gerar a rota ou erro inesperado              |

Notas adicionais:

• Este endpoint utiliza um serviço interno (RouteService) para calcular o percurso e retornar um conjunto de coordenadas e informações resumidas.

• Futuramente, pretende-se adicionar:

   • POST /api/routes/save → para guardar rotas no histórico do utilizador

   • GET /api/routes → para listar rotas já geradas

   • DELETE /api/routes/{id} → para remover rotas salvas

---
## Dicionário de Dados

**Entidade: Usuario**
| Atributo     | Tipo de Dado | Descrição                   | Restrições                |
| ------------ | ------------ | --------------------------- | ------------------------- |
| id_usuario   | INT          | Identificador único         | PK, Auto Increment        |
| nome         | VARCHAR(100) | Nome completo               | NOT NULL                  |
| email        | VARCHAR(100) | Email do usuário (único)    | NOT NULL, UNIQUE          |
| senha        | VARCHAR(255) | Senha criptografada         | NOT NULL                  |
| data_criacao | DATETIME     | Data de criação do registro | DEFAULT CURRENT_TIMESTAMP |

**Entidade: Meta**
| Atributo    | Tipo de Dado | Descrição                   | Restrições               |
| ----------- | ------------ | --------------------------- | ------------------------ |
| id_meta     | INT          | Identificador único da meta | PK, Auto Increment       |
| descricao   | VARCHAR(255) | Descrição da meta           | NOT NULL                 |
| data_inicio | DATE         | Data de início da meta      | —                        |
| data_fim    | DATE         | Data limite da meta         | —                        |
| id_usuario  | INT          | Usuário responsável         | FK → Usuário(id_usuario) |

**Entidade: Corrida**
| Atributo     | Tipo de Dado | Descrição                      | Restrições               |
| ------------ | ------------ | ------------------------------ | ------------------------ |
| id_corrida   | INT          | Identificador único da corrida | PK, Auto Increment       |
| data_corrida | DATE         | Data da corrida                | NOT NULL                 |
| distancia    | FLOAT        | Distância percorrida (km)      | NOT NULL                 |
| tempo        | TIME         | Duração da corrida             | —                        |
| id_usuario   | INT          | Usuário que realizou a corrida | FK → Usuário(id_usuario) |


**Endidade: Postagem**
| Atributo      | Tipo de Dado | Descrição                 | Restrições                |
| ------------- | ------------ | ------------------------- | ------------------------- |
| id_postagem   | INT          | Identificador da postagem | PK, Auto Increment        |
| titulo        | VARCHAR(100) | Título da postagem        | NOT NULL                  |
| conteudo      | TEXT         | Conteúdo da postagem      | NOT NULL                  |
| data_postagem | DATETIME     | Data/hora da publicação   | DEFAULT CURRENT_TIMESTAMP |
| id_usuario    | INT          | Autor da postagem         | FK → Usuário(id_usuario)  |

**Entidade: UC(Relaçao Usuario-Corrida)**
| Atributo   | Tipo de Dado | Descrição                | Restrições               |
| ---------- | ------------ | ------------------------ | ------------------------ |
| id_usuario | INT          | Identificador do usuário | FK → Usuário(id_usuario) |
| id_corrida | INT          | Identificador da corrida | FK → Corrida(id_corrida) |

---
## Estrutura dos Dados 
![Estrutura de Dados](../../Imagens/Estruturadedados.png)

---
## Descrição da Solução a Implementar

- Aplicação mobile gratuita com **geolocalização e registo em tempo real**  
- Criação de rotas personalizadas (distância, áreas verdes, praias, sol, etc.)  
- Histórico de corridas e estatísticas  
- Integração com Google Maps SDK e Fused Location Provider  
- Base de dados (SQLite/MySQL) para registo de atividades

**Desenvolvimento da Base de Dados**
A modelagem da base de dados foi projetada para suportar as principais funcionalidades descritas no primeiro relatório: registo de utilizadores, armazenamento de atividades (corridas e caminhadas), definição de metas e interações na comunidade.
A base foi estruturada seguindo o modelo relacional, utilizando MySQL (em ambiente de desenvolvimento via SQL Workbench).

**Tabelas Principais**
- Usuário
- Corrida
- Rota
- Meta
- UC (UsuárioCorrida)

O relacionamento entre as tabelas foi definido da seguinte forma:
 Um utilizador pode ter várias atividades, metas e publicações.
 Cada atividade pode estar associada a uma rota.

Implementação:
A base foi criada e testada com comandos SQL para inserção, atualização e consulta dos dados.

**Modelagem do Sistema**
A estrutura do sistema foi baseada nos princípios da Programação Orientada a Objetos (POO).
As principais classes identificadas foram:
•	Classe Utilizador – armazena informações do perfil e preferências.
•	Classe Atividade – contém dados de cada corrida/caminhada.
•	Classe Rota – representa os trajetos personalizados criados ou sugeridos.
•	Classe Meta – controla objetivos definidos pelos utilizadores.
•	Classe Comunidade – gerencia as interações e postagens.
O diagrama de classes (em formato UML) representa essas entidades e seus relacionamentos, garantindo coerência entre o modelo lógico e o físico da base de dados.

**Desenvolvimento no Android studio**
O projeto foi criado no Android Studio utilizando Kotlin como linguagem principal.
A arquitetura adotada segue o padrão MVC (Model-View-Controller) para facilitar a separação entre interface, lógica e dados.
Telas desenvolvidas:
• Tela de login.
• Tela de Registro.
• Tela Inicial.
• Tela de Gerar Rotas.
• Tela de Corrida.
• Tela de Histórico de Corrida.
• Tela de Comunidade.
• Tela de Perfil.

**Testes e Validações**
Foram realizados testes iniciais de: 
• Navegações entre telas.
• Inserção e leitura de dados.
• Simulação de rotas no mapa.

**Próximas Etapas**
• Implementação de feed de comunidade.
• Melhorias no layout.
• Implementação de notificações e sistemas de metas.

**Áreas curriculares envolvidas:**
- **Base de Dados**: SQLite/MySQL  
- **Programação Mobile**: Android Studio + Java  
- **POO**: Classes de Usuário, Atividade, Localização  
- **Matemática Discreta**: Grafos e algoritmos de caminhos  
- **Competências Comunicacionais**: Poster, vídeo promocional, design  

**Tecnologias:**  
- IDE: Android Studio  
- Linguagem: Java  
- Banco: SQL Workbench  
- APIs: Google Maps SDK, Fused Location Provider, Directions API  
- UI: Figma  
- Versionamento: GitHub / ClickUp  

---

## Planeamento e Calendarização

- Mockups e interface (Figma)  
- Desenvolvimento da base de dados (SQL Workbench)  
- Desenvolvimento front-end (Android Studio)  
- Integração com APIs (Google Maps SDK)  
- Testes e validação  
- Criação de poster e vídeo promocional  

---

## Mockup da Aplicação


![Mockup do aplicativo](../../Imagens/Mockup_RunUp.png)


# Mockup Interativo

[Ver mockup completo no Figma](https://www.figma.com/make/trREFBKUoFakKsm9PVs8au/RunUp-Mobile-App-Mockups?node-id=0-1&t=bzRYL2e4k7qEW83n-1)

---

## Conclusão

Nesta segunda fase, o grupo consolidou a estrutura técnica do projeto RunUp, criando a base de dados, modelando as entidades e iniciando o desenvolvimento da aplicação no Android Studio.
Com a junção do backend e das telas principais, o projeto começa a ganhar forma prática e aproxima-se da sua proposta inicial: uma aplicação acessível, funcional e motivadora para corredores e caminhantes.

---

## Poster 

[Poster do aplicativo](../../Imagens/Poster_RunUp.pdf)

---
## Bibliografia

1. **Google Maps Platform – APIs by Platform**  
   Disponível em: https://developers.google.com/maps/apis-by-platform?hl=pt-br  

2. **“Corrida foi o esporte que mais cresceu no mundo em 2024”, diz relatório**  
   Saúde Abril. Disponível em: https://saude.abril.com.br/fitness/corrida-foi-o-esporte-que-mais-cresceu-no-mundo-em-2024-diz-relatorio/  

3. **Os Melhores Apps de Corrida: Qual Escolher para Melhorar Seu Desempenho?**  
   Az on Esportes. Disponível em: https://azonesportes.com.br/app-de-corrida/  

---

