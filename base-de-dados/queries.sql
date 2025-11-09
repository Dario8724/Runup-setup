-- QUERIES.SQL - Consultas Demonstrativas RunUp
USE RunUp;

-- 1. Listar todos os usuários com suas metas
SELECT U.nome, M.descricao, M.data_fim
FROM Usuario U
LEFT JOIN Meta M ON U.id_usuario = M.id_usuario;

-- 2. Média de distância percorrida por usuário
SELECT U.nome, AVG(C.distancia) AS media_km
FROM Usuario U
JOIN Corrida C ON U.id_usuario = C.id_usuario
GROUP BY U.id_usuario;

-- 3. Postagens mais recentes
SELECT titulo, conteudo, data_postagem
FROM Postagem
ORDER BY data_postagem DESC
LIMIT 5;

-- 4. Número de corridas por usuário
SELECT U.nome, COUNT(C.id_corrida) AS total_corridas
FROM Usuario U
LEFT JOIN Corrida C ON U.id_usuario = C.id_usuario
GROUP BY U.id_usuario;

-- 5. Usuário com maior distância total percorrida
SELECT U.nome, SUM(C.distancia) AS total_km
FROM Usuario U
JOIN Corrida C ON U.id_usuario = C.id_usuario
GROUP BY U.id_usuario
ORDER BY total_km DESC
LIMIT 1;
