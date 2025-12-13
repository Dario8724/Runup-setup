-- POPULATE.SQL - Dados de Teste RunUp
USE RunUp;

INSERT INTO usuario (user_nome, user_email, user_senha, user_data_de_nascimento, user_sexo, user_peso, user_altura, user_experiencia) VALUES
('João Silva', 'joao@email.com', 'senha123', '1990-05-10', 'Masculino', 75.5, 1.80, 'Intermediário'),
('Maria Santos', 'maria@email.com', 'senha456', '1988-03-22', 'Feminino', 60.0, 1.70, 'Avançado'),
('Carlos Oliveira', 'carlos@email.com', 'senha789', '1995-11-01', 'Masculino', 82.3, 1.85, 'Iniciante'),
('Ana Paula', 'ana@email.com', 'senha321', '1992-07-18', 'Feminino', 68.4, 1.68, 'Intermediário'),
('Pedro Lima', 'pedro@email.com', 'senha654', '1987-12-30', 'Masculino', 70.0, 1.75, 'Avançado'),
('Laura Mendes', 'laura@email.com', 'senha987', '1999-09-09', 'Feminino', 58.1, 1.62, 'Iniciante'),
('Ricardo Alves', 'ricardo@email.com', 'senha159', '1985-01-25', 'Masculino', 90.2, 1.90, 'Intermediário');

INSERT INTO meta (meta_nome, meta_distancia) VALUES
('Correr 5 km', 5.00),
('Correr 10 km', 10.00),
('Correr 15 km', 15.00),
('Correr 21 km', 21.00),
('Correr 42 km', 42.00),
('Aumentar velocidade', NULL),
('Melhorar resistência', NULL);

INSERT INTO mu (mu_meta_id, mu_user_id, mu_corrida_id) VALUES
(1, 1, 1), -- João (Intermediário) atingiu 5 km na corrida 1
(2, 1, 2), -- João também fez 10 km na corrida 2

(2, 2, 2), -- Maria (Avançada) atingiu 10 km na corrida 2
(4, 2, 3), -- Maria cumpriu meta de 21 km ao treinar 14 km em ritmo moderado (preparação)

(1, 3, 6), -- Carlos (Iniciante) completou 6 km
(7, 3, 3), -- Carlos trabalhando resistência

(1, 4, 5), -- Ana fez 7 km, encaixa na meta 5 km
(3, 4, 3), -- Intermediária: meta 15 km (treino de 14 km aproxima)

(4, 5, 3), -- Pedro (Avançado): preparação para meia maratona
(5, 5, 7), -- Meta 42 km (Pedro avançado fazendo rota de miradouros intensa)

(6, 6, 5), -- Laura (Iniciante) melhorando velocidade (rota plana)
(7, 6, 1), -- resistência com corrida moderada

(1, 7, 6), -- Ricardo fez 6 km (meta 5 km)
(6, 7, 5); -- Velocidade (rota plana do Parque das Nações)

INSERT INTO tipo (tipo_nome) VALUES
('Treino leve'),
('Treino moderado'),
('Treino intenso'),
('Prova curta'),
('Prova longa'),
('Regenerativo'),
('Sprints');

INSERT INTO corrida (corrida_data, corrida_tempo, corrida_ritmo, corrida_kcal, corrida_distancia, corrida_tipo_id, corrida_rota_id) VALUES
('2025-02-01', '00:40:20', 5.00, 480, 8.00, 2, 1),  -- Rota 1 (plana e rápida)
('2025-02-03', '01:08:30', 5.40, 720, 12.00, 3, 2), -- Rota 2 (treino longo moderado)
('2025-02-05', '01:32:10', 6.00, 910, 14.00, 2, 3), -- Rota 3 (urbana moderada)
('2025-02-07', '01:05:40', 6.30, 650, 9.00, 3, 4),  -- Rota 4 (muita subida → ritmo maior)
('2025-02-10', '00:32:15', 4.60, 430, 7.00, 1, 5),  -- Rota 5 (super plana → rápido)
('2025-02-13', '00:30:55', 5.20, 380, 6.00, 6, 6),  -- Rota 6 (subida na Liberdade → moderado)
('2025-02-15', '00:45:30', 6.40, 600, 7.00, 3, 7);  -- Rota 7 (miradouros → difícil)

INSERT INTO post (post_corrida_id, post_comentario) VALUES
(1, 'Corrida tranquila pela zona ribeirinha entre a Praça do Comércio e o Cais do Sodré.'),
(2, 'Treino forte passando pela Torre de Belém e pelos Jerónimos.'),
(3, 'Treino urbano constante entre o Parque Eduardo VII, Marquês e Saldanha.'),
(4, 'Percurso técnico por Alfama e Castelo de São Jorge, muitas subidas!'),
(5, 'Corrida rápida no Parque das Nações, percurso totalmente plano.'),
(6, 'Do Marquês ao Parque Eduardo VII, treino moderado com algumas subidas.'),
(7, 'Rota muito exigente pelos miradouros e centro histórico.');

INSERT INTO rota (rota_nome, rota_elevacao) VALUES
('Rota Ribeirinha Lisboa', 15.0),    -- 1
('Rota Belém Monumental', 25.0),     -- 2
('Rota Centro Moderno', 40.0),       -- 3
('Rota Histórica de Alfama', 80.0),  -- 4
('Rota Parque das Nações', 10.0),    -- 5
('Rota Avenida da Liberdade', 50.0), -- 6
('Rota Miradouros de Lisboa', 120.0);-- 7

INSERT INTO cr (cr_rota_id, cr_caract_id) VALUES
(1, 2),  -- perto da praia
(1, 4),  -- ensolarada

(2, 6),  -- histórica
(2, 2),  -- perto da praia

(3, 5),  -- mais árvores
(3, 3),  -- evitar subidas

(4, 6),  -- histórica
(4, 3),  -- evitar subidas

(5, 4),  -- ensolarada

(6, 1),  -- perto do parque
(6, 4),  -- ensolarada

(7, 6),  -- histórica
(7, 4);  -- ensolarada

INSERT INTO caracteristica (caract_tipo) VALUES
('Perto do Parque'),       -- 1
('Perto da Praia'),        -- 2
('Evitar Subidas'),        -- 3
('Rota Ensolarada'),       -- 4
('Mais Árvores'),          -- 5
('Histórica'),             -- 6
('Sem Filtro');            -- 7

INSERT INTO local (local_nome) VALUES
('Praça do Comércio'),       -- 1
('Cais do Sodré'),           -- 2
('Belém - Torre de Belém'),  -- 3
('Mosteiro dos Jerónimos'),  -- 4
('Parque Eduardo VII'),      -- 5
('Marquês de Pombal'),       -- 6
('Saldanha'),                -- 7
('Campo Pequeno'),           -- 8
('Parque das Nações'),       -- 9
('Oceanário de Lisboa'),     -- 10
('Alfama'),                  -- 11
('Castelo de São Jorge'),    -- 12
('Avenida da Liberdade');    -- 13

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(1, 1, 1), -- Início na Praça do Comércio
(1, 2, 2), -- Passa pelo Cais do Sodré
(1, 11, 3); -- Finaliza em Alfama

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(2, 3, 1), -- Início na Torre de Belém
(2, 4, 2), -- Passa pelo Mosteiro dos Jerónimos
(2, 1, 3); -- Finaliza na Praça do Comércio

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(3, 5, 1), -- Início no Parque Eduardo VII
(3, 6, 2), -- Passa pelo Marquês de Pombal
(3, 7, 3); -- Finaliza no Saldanha

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(4, 11, 1), -- Início em Alfama
(4, 12, 2), -- Passa pelo Castelo de São Jorge
(4, 1, 3);  -- Finaliza na Praça do Comércio

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(5, 9, 1), -- Início no Parque das Nações
(5, 10, 2), -- Passa pelo Oceanário de Lisboa
(5, 1, 3); -- Finaliza na Praça do Comércio

INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(6, 13, 1), -- Início na Avenida da Liberdade
(6, 6, 2),  -- Passa pelo Marquês de Pombal
(6, 5, 3);  -- Finaliza no Parque Eduardo VII
    
INSERT INTO lr (lr_rota_id, lr_local_id, lr_ordem) VALUES
(7, 12, 1), -- Início no Castelo de São Jorge
(7, 11, 2), -- Passa por Alfama
(7, 13, 3); -- Finaliza na Avenida da Liberdade