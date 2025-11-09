-- POPULATE.SQL - Dados de Teste RunUp
USE RunUp;

-- Usuários
INSERT INTO Usuario (nome, email, senha) VALUES
('João Silva', 'joao@gmail.com', '12345'),
('Maria Costa', 'maria@gmail.com', 'abcde'),
('Carlos Mendes', 'carlos@gmail.com', 'senha123');

-- Metas
INSERT INTO Meta (descricao, data_inicio, data_fim, id_usuario) VALUES
('Correr 50km em um mês', '2025-11-01', '2025-11-30', 1),
('Participar de uma maratona', '2025-10-01', '2025-12-31', 2);

-- Corridas
INSERT INTO Corrida (data_corrida, distancia, tempo, id_usuario) VALUES
('2025-11-02', 5.2, '00:32:10', 1),
('2025-11-03', 10.0, '01:05:23', 2),
('2025-11-04', 7.8, '00:48:10', 1);

-- Postagens
INSERT INTO Postagem (titulo, conteudo, id_usuario) VALUES
('Primeiro treino!', 'Comecei hoje minha jornada no RunUp!', 1),
('Maratona concluída!', 'Foi um desafio incrível completar 10km!', 2);

-- Relacionamentos UC
INSERT INTO UC (id_usuario, id_corrida) VALUES
(1, 1), (2, 2), (1, 3);
