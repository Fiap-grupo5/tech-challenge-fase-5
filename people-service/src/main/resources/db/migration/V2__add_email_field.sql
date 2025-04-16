-- Adicionar campo de email às tabelas
ALTER TABLE patients ADD COLUMN email VARCHAR(100);
ALTER TABLE doctors ADD COLUMN email VARCHAR(100);
ALTER TABLE administrators ADD COLUMN email VARCHAR(100);
