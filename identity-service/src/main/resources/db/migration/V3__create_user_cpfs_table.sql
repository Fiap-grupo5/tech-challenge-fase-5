-- Criar tabela para armazenar CPFs dos usuários
CREATE TABLE user_cpfs (
    id BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    username VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Criar índice para busca rápida por CPF
CREATE INDEX idx_user_cpfs_cpf ON user_cpfs(cpf); 