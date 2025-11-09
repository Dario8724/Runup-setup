-- CREATE.SQL - Base de Dados RunUp
DROP DATABASE IF EXISTS RunUp;
CREATE DATABASE RunUp;
USE RunUp;

-- Tabela: Usuario
CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Tabela: Meta
CREATE TABLE Meta (
    id_meta INT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    data_inicio DATE,
    data_fim DATE,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

-- Tabela: Corrida
CREATE TABLE Corrida (
    id_corrida INT AUTO_INCREMENT PRIMARY KEY,
    data_corrida DATE NOT NULL,
    distancia FLOAT NOT NULL,
    tempo TIME,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

-- Tabela: Postagem
CREATE TABLE Postagem (
    id_postagem INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    conteudo TEXT NOT NULL,
    data_postagem DATETIME DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario)
);

-- Tabela: UC (Relacionamento Usuário–Corrida)
CREATE TABLE UC (
    id_usuario INT,
    id_corrida INT,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_corrida) REFERENCES Corrida(id_corrida)
);
