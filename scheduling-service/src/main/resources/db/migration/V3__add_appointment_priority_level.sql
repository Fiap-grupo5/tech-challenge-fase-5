-- Adicionar coluna priority_level à tabela appointments
ALTER TABLE appointments ADD COLUMN priority_level VARCHAR(20);

-- Comentário: A coluna é opcional (nullable) para compatibilidade com registros existentes.
-- O tipo VARCHAR(20) comporta os valores do enum PriorityLevel: LOW, MEDIUM, HIGH, URGENT. 