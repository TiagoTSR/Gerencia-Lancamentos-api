CREATE TABLE pessoa (
	id BIGINT PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	logradouro VARCHAR(30),
	numero VARCHAR(30),
	complemento VARCHAR(30),
	bairro VARCHAR(30),
	cep VARCHAR(30),
	cidade VARCHAR(30),
	estado VARCHAR(30),
	enabled BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, enabled) 
VALUES 
('João Silva', 'Rua do Abacaxi', '10', null, 'Brasil', '38400-12', 'Uberlândia', 'MG', true),
('Maria Rita', 'Rua do Sabiá', '110', 'Apto 101', 'Colina', '11400-12', 'Ribeirão Preto', 'SP', true),
('Pedro Santos', 'Rua da Bateria', '23', null, 'Morumbi', '54212-12', 'Goiânia', 'GO', true),
('Ricardo Pereira', 'Rua do Motorista', '123', 'Apto 302', 'Aparecida', '38400-12', 'Salvador', 'BA', true),
('Josué Mariano', 'Av Rio Branco', '321', null, 'Jardins', '56400-12', 'Natal', 'RN', true),
('Pedro Barbosa', 'Av Brasil', '100', null, 'Tubalina', '77400-12', 'Porto Alegre', 'RS', true),
('Henrique Medeiros', 'Rua do Sapo', '1120', 'Apto 201', 'Centro', '12400-12', 'Rio de Janeiro', 'RJ', true),
('Carlos Santana', 'Rua da Manga', '433', null, 'Centro', '31400-12', 'Belo Horizonte', 'MG', true),
('Leonardo Oliveira', 'Rua do Músico', '566', null, 'Segismundo Pereira', '38400-00', 'Uberlândia', 'MG', true),
('Isabela Martins', 'Rua da Terra', '1233', 'Apto 10', 'Vigilato', '99400-12', 'Manaus', 'AM', true);
