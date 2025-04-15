-- Adicionar campo de email à tabela users
ALTER TABLE users ADD COLUMN email VARCHAR(100);

-- Inicialmente definido como NULL para ser compatível com dados existentes
-- Em uma migração posterior, podemos torná-lo NOT NULL após atualizar os dados 