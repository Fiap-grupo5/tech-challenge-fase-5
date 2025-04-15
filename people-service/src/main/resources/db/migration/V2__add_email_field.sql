-- Adicionar campo de email às tabelas
ALTER TABLE patients ADD COLUMN email VARCHAR(100);
ALTER TABLE doctors ADD COLUMN email VARCHAR(100);
ALTER TABLE administrators ADD COLUMN email VARCHAR(100);

-- Inicialmente definido como NULL para ser compatível com dados existentes
-- Em uma migração posterior, podemos torná-lo NOT NULL após atualizar os dados 