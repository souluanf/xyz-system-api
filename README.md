# XYZ System API

[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue)](https://www.postgresql.org/)
[![Java](https://img.shields.io/badge/Programming%20Language-Java-blue)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Framework-Spring%20Boot-blue)](https://spring.io/projects/spring-boot/)
[![OpenAPI](https://img.shields.io/badge/API%20Specification-OpenAPI-blue)](https://www.openapis.org/)
[![Swagger](https://img.shields.io/badge/API%20Documentation-Swagger-blue)](https://swagger.io/)
[![Docker](https://img.shields.io/badge/Containerization-Docker-blue)](https://www.docker.com/)
[![Keycloak](https://img.shields.io/badge/Identity%20and%20Access%20Management-Keycloak-blue)](https://www.keycloak.org/)
---

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=bugs)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=souluanf_xyz-system-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=souluanf_xyz-system-api)
## Sumário

1. [Funcionalidades](#funcionalidades)
2. [Requisitos](#requisitos)
3. [Execução](#execução)
4. [Credenciais](#credenciais)
5. [Questões](#questões)

---

## Funcionalidades
### 1. **Gerenciamento de Usuários**
- **Criar usuário:** Permite criar novos usuários no sistema.
- **Atualizar usuário:** Atualiza informações de um usuário existente.
- **Deletar usuário:** Remove um usuário pelo `username`.
- **Buscar usuário por `username`:** Recupera os dados de um usuário pelo `username`.
- **Listar todos os usuários:** Retorna uma lista de todos os usuários cadastrados no sistema.

### 2. **Gerenciamento de Vendedores (Salesperson)**
- **Atualizar nomes dos vendedores:** Adiciona ou remove um asterisco (*) ao final do nome com base em suas vendas.
- **Listar vendedores com mais de um pedido:** Retorna uma lista de vendedores que possuem mais de um pedido associado.
- **Excluir vendedores por cidade:** Remove vendedores associados a uma cidade específica.
- **Listar vendedores sem pedidos para um cliente:** Recupera nomes de vendedores sem pedidos para um cliente específico.
- **Listar vendas totais por vendedor:** Retorna o total de vendas associado a cada vendedor.

### 3. **Gerenciamento de Plantas**
- **Criar planta:** Permite adicionar uma nova planta ao sistema.
- **Atualizar planta:** Modifica as informações de uma planta existente.
- **Deletar planta:** Remove uma planta pelo seu identificador (`id`).
- **Listar todas as plantas:** Retorna todas as plantas registradas no sistema.
- **Buscar planta por ID:** Recupera as informações de uma planta específica.

### 4. **Anagramas**
- **Gerar anagramas:** Recebe uma palavra e gera uma lista de todos os anagramas possíveis.

### 5. **Autenticação**
- **Gerar token:** Permite gerar um token JWT para autenticação.


## Requisitos

- Docker e Docker Compose instalados. [Instalação](https://docs.docker.com/get-docker/)

---

## Execução

**Copie o arquivo de variáveis de exemplo**:

```bash
cp .env.example .env
```

**Inicie o ambiente**:

```bash
docker compose up -d
```

**Acesse a aplicação (OpenAPI/Swagger)**:  [http://localhost:8080/api](http://localhost:8080/api)

**Acesse o Keycloak**: [http://localhost:8085](http://localhost:8085)

A documentação também pode ser acessada importando a collection do Postman disponível em [collections](collections)
 Ou executando o comando abaixo: para importar a collection no Postman.

[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/26187327-6cbd2668-ca50-4572-a586-d49bd7e3a442-2sAYQWKDUh)

---

## Credenciais

| Service  | Host        | Porta  | Username | Password | 
|----------|-------------|--------|----------|----------|
| db       | `localhost` | `5432` | `xyz_db` | `xyz_db` | 
| keycloak | `localhost` | `8085` | `admin`  | `admin`  | 
| app      | `localhost` | `8080` | `admin`  | `admin`  | 

## Questões

### 1. Geração de Anagrams
[src/main/java/dev/luanfernandes/service/AnagramService.java](src/main/java/dev/luanfernandes/service/AnagramService.java)

### 2. Uso de Equals and HashCode

[src/main/java/dev/luanfernandes/domain/entity/Plant.java](src/main/java/dev/luanfernandes/domain/entity/Plant.java)

### 3.  Decoupling Using Design Pattern (Adapter)
[src/main/java/dev/luanfernandes/adapter/PlantRepositoryAdapter.java](src/main/java/dev/luanfernandes/adapter/PlantRepositoryAdapter.java)
[src/main/java/dev/luanfernandes/adapter/PlantRepositoryAdapterImpl.java](src/main/java/dev/luanfernandes/adapter/PlantRepositoryAdapterImpl.java)

### 4. Preventing SQL Injection
[src/main/java/dev/luanfernandes/repository/SalespersonRepository.java](src/main/java/dev/luanfernandes/repository/SalespersonRepository.java)
[src/main/java/dev/luanfernandes/repository/OrderRepository.java](src/main/java/dev/luanfernandes/repository/OrderRepository.java)

### 5. Improving Batch Process Performance
	Identificar gargalos:
        - Usar profiler ou logs.
	Otimizar queries:
        - Adicionar índices.
	    - Usar batch updates.
	Melhorar a execução lógica:
	    - Paralelizar operações.
	Melhorar a transferência de arquivos:
	    - Compressão antes do envio.

 ### 6. Queries
- A. Retornar os nomes de todos os vendedores que não têm nenhum pedido com a Samsonic

```sql
SELECT s.name
FROM salespeople s
WHERE NOT EXISTS (SELECT 1
                  FROM orders o
                           JOIN customers c ON o.customer_id = c.id
                  WHERE c.name = 'Samsonic'
                    AND o.salesperson_id = s.id);
```

- B. Atualizar os nomes dos vendedores que têm 2 ou mais pedidos (adicionar ‘*’ no final do nome) ou que têm menos de 2
  pedidos removendo o ‘*’ do final do nome

```sql
UPDATE salespeople
SET name =
        CASE
            WHEN id IN (SELECT salesperson_id
                        FROM orders
                        GROUP BY salesperson_id
                        HAVING COUNT(*) >= 2) THEN
                CASE
                    WHEN name LIKE '%*' THEN name
                    ELSE name || '*'
                    END
            ELSE
                CASE
                    WHEN name LIKE '%*' THEN SUBSTRING(name FROM 1 FOR LENGTH(name) - 1)
                    ELSE name
                    END
            END
WHERE id IN (SELECT id
             FROM salespeople);
```

- C. Excluir todos os vendedores que fizeram pedidos para a cidade de Jackson

```sql
DELETE
FROM salespeople
WHERE id IN (SELECT DISTINCT s.id
             FROM salespeople s
                      JOIN orders o ON s.id = o.salesperson_id
                      JOIN customers c ON o.customer_id = c.id
             WHERE c.city = 'Jackson');
```

- D. Total de vendas para cada vendedor (exibir 0 se o vendedor não tiver vendido nada)

```sql
SELECT s.id, s.name, COALESCE(SUM(o.amount), 0) AS total_sales
FROM salespeople s
         LEFT JOIN orders o ON s.id = o.salesperson_id
GROUP BY s.id, s.name
ORDER BY s.name;
```
### 7. Use Case for Plant Management

Como administrador do sistema XYZ,
Eu quero gerenciar informações de plantas,
Para permitir a criação, atualização, exclusão e pesquisa de plantas no sistema.

**Critérios de Aceitação**:
1. Deve ser possível criar novas plantas fornecendo um código único e uma descrição opcional (até 10 caracteres).
2. Deve ser possível listar todas as plantas registradas.
3. Deve ser possível buscar plantas pelo seu identificador único.
4. Apenas usuários com privilégios de administrador podem excluir plantas.
5. O sistema deve impedir a criação de plantas com códigos duplicados.
6. Mensagens de erro apropriadas devem ser exibidas para entradas inválidas ou quando plantas não forem encontradas.

---

#### Regras de Negócio

1. **Código único obrigatório**:
    - O campo `code` é numérico, obrigatório e deve ser único no sistema.
2. **Descrição opcional**:
    - O campo `description` é alfanumérico e pode conter no máximo 10 caracteres.
3. **Permissões de exclusão**:
    - Apenas usuários com papel de administrador têm permissão para excluir plantas.
4. **Prevenção de duplicidade**:
    - O sistema deve verificar se já existe uma planta com o mesmo `code` antes de criar ou atualizar um registro.

---

#### Validações e Medidas de Segurança

1. **Validações**:
    - O campo `code` deve ser:
        - Numérico.
        - Positivo.
        - Único.
    - O campo `description` deve:
        - Conter no máximo 10 caracteres.
    - Ambas as entradas devem ser validadas antes de serem persistidas no banco de dados.

2. **Segurança**:
    - A autenticação deve ser implementada para garantir que apenas administradores possam realizar exclusões.
    - Erros de validação ou duplicidade devem retornar mensagens claras para o cliente.
    - Logs devem registrar todas as operações realizadas no sistema para auditoria.

---

 ##### Testes Funcionais
1. **Criação de plantas**:
    - Testar com entradas válidas e inválidas (e.g., código duplicado, código negativo, descrição muito longa).
2. **Listagem de plantas**:
    - Garantir que todas as plantas cadastradas são retornadas corretamente.
3. **Busca por ID**:
    - Testar com IDs válidos e inválidos (e.g., planta inexistente).
4. **Atualização de plantas**:
    - Testar alterações nos campos `code` e `description`.
5. **Exclusão de plantas**:
    - Garantir que apenas administradores possam excluir registros.
    - Testar a exclusão de uma planta inexistente.
---
#### Casos de Teste de Borda
- Criar uma planta com o código `0` (não deve ser permitido).
- Criar uma planta com uma descrição contendo exatamente 10 caracteres (deve ser permitido).
- Excluir uma planta como um usuário não autorizado (deve falhar).

---
Implementação do caso de uso de gerenciamento de plantas:

[src/main/java/dev/luanfernandes/controller/PlantController.java](src/main/java/dev/luanfernandes/controller/PlantController.java)