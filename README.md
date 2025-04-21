# Sistema de Agendamento Médico

Um sistema completo de gestão de saúde baseado em microserviços para agendamento de consultas e exames médicos. Desenvolvido como trabalho de conclusão da pós-graduação em Desenvolvimento e Arquitetura em Java e Spring Boot.

## Arquitetura de Microserviços

O sistema é composto por quatro microserviços principais que trabalham em conjunto:

### Identity Service (Porta: 8081)
- Autenticação e autorização de usuários
- Gerenciamento de tokens JWT
- Controle de acesso baseado em perfis

### People Service (Porta: 8082)
- Gestão de pacientes
- Gestão de médicos e suas especialidades
- Administração de usuários do sistema

### Scheduling Service (Porta: 8083)
- Gerenciamento de agendamentos de consultas e exames
- Controle de encaminhamentos
- Priorização de atendimentos
- Validação de disponibilidade

### Facility Service (Porta: 8084)
- Gestão de unidades de saúde
- Controle de agendas médicas
- Monitoramento de capacidade das unidades
- Busca por unidades de saúde próximas

## Tecnologias Utilizadas

- **Backend**: Java 17, Spring Boot 3.x
- **Comunicação**: RESTful APIs, Apache Kafka (Mensageria)
- **Persistência**: PostgreSQL, Spring Data JPA
- **Documentação**: Swagger/OpenAPI
- **Infraestrutura**: Docker, Docker Compose
- **Ferramentas**: Maven, Lombok, Spring Cloud

## Dependências do Sistema

- **Kafka**: Barramento de eventos para comunicação assíncrona
- **PostgreSQL**: Banco de dados relacional com quatro schemas:
  - healthcare_identity
  - healthcare_people
  - healthcare_scheduling
  - healthcare_facility

## Pré-requisitos

- Docker e Docker Compose
- Java 17
- Maven

## Executando o Sistema

### Windows

```bash
# Execute o script de construção e execução para Windows
build-and-run.bat
```

### Linux/MacOS

```bash
# Torne o script executável
chmod +x build-and-run.sh init-multiple-databases.sh

# Execute o script de construção e execução
./build-and-run.sh
```

### Manualmente

1. Construa todos os serviços:
   ```bash
   mvn clean package -DskipTests
   ```

2. Inicie todos os serviços usando Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Verifique o status dos serviços:
   ```bash
   docker-compose ps
   ```

## Documentação da API

Cada serviço expõe sua documentação de API via Swagger:

- Identity Service: http://localhost:8081/swagger-ui/index.html
- People Service: http://localhost:8082/swagger-ui/index.html
- Scheduling Service: http://localhost:8083/swagger-ui/index.html
- Facility Service: http://localhost:8084/swagger-ui/index.html

## Fluxo Principal do Sistema

1. Pacientes são cadastrados no People Service
2. Médicos são cadastrados e têm suas agendas definidas no Facility Service
3. Unidades de saúde são configuradas com capacidades no Facility Service
4. Agendamentos são criados no Scheduling Service, que valida disponibilidade do médico e capacidade da unidade

## Parando o Sistema

```bash
# Para parar todos os serviços mantendo os dados
docker-compose down

# Para parar e remover todos os dados (incluindo volumes)
docker-compose down -v
```

## Autores

- Equipe 5 FIAP Tech Challenge - Turma 2024
