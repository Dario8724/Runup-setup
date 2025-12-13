-- QUERIES.SQL - Consultas Demonstrativas RunUp
USE RunUp;

/* 1 – Listar todos os utilizadores */
SELECT * FROM usuario;

/* 2 – Listar todas as corridas */
SELECT corrida_id, corrida_data, corrida_distancia, corrida_tempo
FROM corrida;

/* 3 – Listar todas as rotas existentes */
SELECT rota_id, rota_nome FROM rota;

/* 4 – Mostrar todas as metas disponíveis */
SELECT meta_id, meta_descricao FROM meta;

/* 5 – Mostrar todos os posts com o texto */
SELECT post_id, post_comentario
FROM post;

/* 6 – Corridas com nome do utilizador e da rota */
SELECT c.corrida_id, u.user_nome, r.rota_nome, c.corrida_distancia, c.corrida_data
FROM corrida c
JOIN mu ON mu.mu_corrida_id = c.corrida_id
JOIN usuario u ON u.user_id = mu.mu_user_id
JOIN rota r ON r.rota_id = c.corrida_rota_id;

/* 7 – Calcular total de KM por utilizador */
SELECT u.user_nome, SUM(c.corrida_distancia) AS total_km
FROM usuario u
JOIN mu ON mu.mu_user_id = u.user_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
GROUP BY u.user_nome
ORDER BY total_km DESC;

/* 8 – Distância média por corrida */
SELECT AVG(corrida_distancia) AS media_km
FROM corrida;

/* 9 – Mostrar as rotas mais utilizadas */
SELECT r.rota_nome, COUNT(*) AS qtd_vezes
FROM corrida c
JOIN rota r ON r.rota_id = c.corrida_rota_id
GROUP BY r.rota_nome
ORDER BY qtd_vezes DESC;

/* 10 – Mostrar todos os posts com informações da corrida */
SELECT u.user_nome, r.rota_nome, c.corrida_data, p.post_comentario
FROM post p
JOIN corrida c ON c.corrida_id = p.post_corrida_id
JOIN rota r ON r.rota_id = c.corrida_rota_id
JOIN mu ON mu.mu_corrida_id = c.corrida_id
JOIN usuario u ON u.user_id = mu.mu_user_id;

/* 11 – Qual utilizador queimou mais calorias no total */
SELECT u.user_nome, SUM(c.corrida_calorias) AS total_kcal
FROM usuario u
JOIN mu ON mu.mu_user_id = u.user_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
GROUP BY u.user_nome
ORDER BY total_kcal DESC
LIMIT 1;

/* 12 – Melhor ritmo médio de cada utilizador */
SELECT u.user_nome, AVG(c.corrida_ritmo) AS ritmo_medio
FROM usuario u
JOIN mu ON mu.mu_user_id = u.user_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
GROUP BY u.user_nome
ORDER BY ritmo_medio ASC;

/* 13 – Listar corridas que cumpriram metas de distância */
SELECT u.user_nome, m.meta_descricao, c.corrida_distancia, c.corrida_data
FROM mu
JOIN usuario u ON u.user_id = mu.mu_user_id
JOIN meta m ON m.meta_id = mu.mu_meta_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
WHERE c.corrida_distancia >= (
    SELECT CAST(SUBSTRING(meta_descricao, 1, 2) AS UNSIGNED)
)
ORDER BY u.user_nome;

/* 14 – Rotas com número de locais (via LR) */
SELECT r.rota_nome, COUNT(l.local_id) AS num_locais
FROM rota r
JOIN lr ON lr.lr_rota_id = r.rota_id
JOIN local l ON l.local_id = lr.lr_local_id
GROUP BY r.rota_nome
ORDER BY num_locais DESC;

/* 15 – Listar corridas detalhadamente com todos os locais percorridos */
SELECT 
    c.corrida_id,
    u.user_nome,
    r.rota_nome,
    l.local_nome,
    lr.lr_ordem
FROM corrida c
JOIN rota r ON r.rota_id = c.corrida_rota_id
JOIN lr ON lr.lr_rota_id = r.rota_id
JOIN local l ON l.local_id = lr.lr_local_id
JOIN mu ON mu.mu_corrida_id = c.corrida_id
JOIN usuario u ON u.user_id = mu.mu_user_id
ORDER BY c.corrida_id, lr.lr_ordem;

/* 16 – Encontrar o melhor (mais rápido) tempo por km de cada utilizador */
SELECT u.user_nome, MIN(c.corrida_ritmo) AS melhor_pace
FROM usuario u
JOIN mu ON mu.mu_user_id = u.user_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
GROUP BY u.user_nome
ORDER BY melhor_pace ASC;

/* 17 – Listar a corrida mais longa registrada */
SELECT corrida_id, corrida_distancia, corrida_data
FROM corrida
ORDER BY corrida_distancia DESC
LIMIT 1;

/* 18 – Utilizadores que correram mais de 20 km no total */
SELECT u.user_nome, SUM(c.corrida_distancia) AS total_km
FROM usuario u
JOIN mu ON mu.mu_user_id = u.user_id
JOIN corrida c ON c.corrida_id = mu.mu_corrida_id
GROUP BY u.user_nome
HAVING total_km > 20;

/* 19 – Mostrar o ritmo médio por rota */
SELECT r.rota_nome, AVG(c.corrida_ritmo) AS media_pace
FROM corrida c
JOIN rota r ON r.rota_id = c.corrida_rota_id
GROUP BY r.rota_nome
ORDER BY media_pace ASC;

/* 20 – Mostrar as rotas onde há mais posts associados */
SELECT r.rota_nome, COUNT(p.post_id) AS qtd_posts
FROM post p
JOIN corrida c ON c.corrida_id = p.post_corrida_id
JOIN rota r ON r.rota_id = c.corrida_rota_id
GROUP BY r.rota_nome
ORDER BY qtd_posts DESC;
