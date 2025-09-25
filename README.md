# Checkpoint 1 - SOA

# ByteShop Online – API REST
API REST **simples e funcional** da loja ByteShop, seguindo o **modelo da professora** e as **convenções REST** (camadas Controller → Service → Repository, DTOs, validação, tratamento global de erros e documentação Swagger/OpenAPI).

**Integrantes**
- Gabriel Machado Carrara Pimentel — RM99880  
- Lourenzo Ramos — RM99951  
- Vítor Hugo Rodrigues — RM97758

---

## Sumário
1. [Arquitetura & Padrões](#arquitetura--padrões)  
2. [Requisitos](#requisitos)  
3. [Como Rodar](#como-rodar)  
4. [Configuração do Banco (H2)](#configuração-do-banco-h2)  
5. [Swagger / OpenAPI](#swagger--openapi)  
6. [Tratamento de Erros](#tratamento-de-erros)  
7. [Regras de Negócio Implementadas](#regras-de-negócio-implementadas)  
8. [Endpoints (Mapa)](#endpoints-mapa)  
9. [Exemplos JSON (CRUD e Fluxos)](#exemplos-json-crud-e-fluxos)  
10. [Estrutura de Pastas](#estrutura-de-pastas)  
11. [Dependências Principais](#dependências-principais)  
12. [Dicas & Troubleshooting](#dicas--troubleshooting)

---

## Arquitetura & Padrões
- **Camadas**
  - `controller/` (Controllers + DTOs)
  - `service/` (regras de negócio)
  - `repository/` (JPA Repositories)
  - `domain/` (Entidades JPA)
  - `infrastructure/` (Swagger config + ExceptionHandler)
- **DTOs** separados para **Request** e **Response**.
- **Validação** com Bean Validation (ex.: `@NotNull`, `@Email`, `@Size`).
- **Tratamento global de erros** com `@ControllerAdvice` (JSON padronizado).
- **H2 em memória** + `schema.sql` e `data.sql` aplicados na inicialização.

---

## Requisitos
- **Java 21** (JDK 21)
- **Maven 3.8+**
- Nenhuma instalação de banco: H2 roda em memória (modo MySQL).

---

## Como Rodar
Na raiz do projeto:
```bash
mvn spring-boot:run
```
ou empacotado:
```bash
mvn clean package
java -jar target/ByteShopOnlineApp-1.0.0.jar
```
A aplicação inicia em: `http://localhost:8080`

---

## Configuração do Banco (H2)
- JDBC URL: `jdbc:h2:mem:byteshop_db`
- Usuário: `sa`
- Senha: *(vazia)*
- Console H2: `http://localhost:8080/h2-console`

> **Importante:** O banco é **em memória**. Cada reinicialização zera os dados e reaplica `schema.sql`/`data.sql`.

---

## Swagger / OpenAPI
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`  
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

> Dica: Clique em **Try it out** para habilitar edição dos parâmetros (ex.: `{id}`).  
> Se o campo `{id}` não aparecer, garanta que os controllers usam `@PathVariable("id")` e o `pom.xml` tem `<parameters>true</parameters>` no `maven-compiler-plugin`.

---

## Tratamento de Erros
Formato padrão (exemplo):
```json
{
  "timestamp": "2025-09-24T21:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "email já cadastrado",
  "path": "/clientes"
}
```
Principais status:
- `2xx`: sucesso
- `400`: validação/regra de negócio (qtd < 1, produto inativo, valor do pagamento ≠ total, etc.)
- `404`: recurso não encontrado
- `409`: conflito (e-mail duplicado, carrinho já existente p/ cliente, pagamento duplicado, produto em uso)

---

## Regras de Negócio Implementadas
- **Cliente**
  - E-mail **único**; duplicado → `409`.
- **Produto**
  - Pode ser ativado/desativado (`ativo: true/false`).
  - **Produto inativo** não entra no carrinho → `400`.
  - **Exclusão** bloqueada se o produto estiver **em uso** em pedido/item → `409`.
- **Carrinho**
  - **1 carrinho por cliente** (duplicado → `409`).
  - Item com `quantidade ≥ 1`.
  - `subtotal = precoUnitario × quantidade`; `total` soma dos subtotais.
- **Pedido**
  - Criado **a partir de um carrinho**: copia itens e calcula `total`.
  - Status: `CRIADO`, `PAGO`, `CANCELADO`.
- **Pagamento**
  - **Máximo 1 pagamento por pedido** (duplicado → `409`).
  - `valor` deve ser **igual** ao `total` do pedido (`400` se diferente).
  - `metodo ∈ {PIX, CARTAO, BOLETO}`.

---

## Endpoints (Mapa)
**Clientes** `/clientes`  
- `GET /clientes`
- `GET /clientes/{id}`
- `POST /clientes`
- `PUT /clientes/{id}`
- `DELETE /clientes/{id}`

**Produtos** `/produtos`  
- `GET /produtos`
- `GET /produtos/{id}`
- `POST /produtos`
- `PUT /produtos/{id}`
- `DELETE /produtos/{id}`

**Carrinhos** `/carrinhos`  
- `POST /carrinhos` – `{ "clienteId": ... }`
- `GET /carrinhos/{id}`
- `DELETE /carrinhos/{id}`
- `POST /carrinhos/{id}/itens` – `{ "produtoId": ..., "quantidade": ... }`
- `PUT /carrinhos/{id}/itens/{itemId}`
- `DELETE /carrinhos/{id}/itens/{itemId}`
- `GET /carrinhos/{id}/itens`

**Pedidos** `/pedidos`  
- `POST /pedidos` – `{ "carrinhoId": ... }`
- `GET /pedidos`
- `GET /pedidos/{id}`
- `PUT /pedidos/{id}/status?status=PAGO|CRIADO|CANCELADO`

**Pagamentos** `/pagamentos`  
- `POST /pagamentos` – `{ "pedidoId": ..., "valor": ..., "metodo": "PIX|CARTAO|BOLETO" }`
- `GET /pagamentos/{id}`
- `PUT /pagamentos/{id}/status` – `{ "status": "PENDENTE|APROVADO|RECUSADO" }`

---

## Exemplos JSON (CRUD e Fluxos)

### Clientes (CRUD completo)

**POST /clientes** – *criar cliente*
```json
{
  "nome": "João da Silva",
  "email": "joao.silva@example.com",
  "documento": "12345678900"
}
```
**Resposta 201**:
```json
{
  "id": 1,
  "nome": "João da Silva",
  "email": "joao.silva@example.com",
  "documento": "12345678900"
}
```

**GET /clientes** – *listar clientes*  
**Resposta 200**:
```json
[
  {
    "id": 1,
    "nome": "João da Silva",
    "email": "joao.silva@example.com",
    "documento": "12345678900"
  }
]
```

**GET /clientes/{id}** – *detalhar cliente*  
**Resposta 200**:
```json
{
  "id": 1,
  "nome": "João da Silva",
  "email": "joao.silva@example.com",
  "documento": "12345678900"
}
```

**PUT /clientes/{id}** – *atualizar cliente*
```json
{
  "nome": "João Atualizado",
  "email": "joao.silva@example.com",
  "documento": "12345678900"
}
```
**Resposta 200**:
```json
{
  "id": 1,
  "nome": "João Atualizado",
  "email": "joao.silva@example.com",
  "documento": "12345678900"
}
```

**DELETE /clientes/{id}** – *remover cliente*  
**Resposta 204**: *(corpo vazio)*

---

### Produtos (CRUD completo)

**POST /produtos** – *criar produto*
```json
{
  "nome": "Mouse Gamer",
  "preco": 199.90,
  "categoria": "Periféricos",
  "descricao": "RGB, 8.000 DPI",
  "ativo": true
}
```
**Resposta 201**:
```json
{
  "id": 10,
  "nome": "Mouse Gamer",
  "preco": 199.90,
  "categoria": "Periféricos",
  "descricao": "RGB, 8.000 DPI",
  "ativo": true
}
```

**GET /produtos** – *listar produtos*  
**Resposta 200**:
```json
[
  {
    "id": 10,
    "nome": "Mouse Gamer",
    "preco": 199.90,
    "categoria": "Periféricos",
    "descricao": "RGB, 8.000 DPI",
    "ativo": true
  }
]
```

**GET /produtos/{id}** – *detalhar produto*  
**Resposta 200**:
```json
{
  "id": 10,
  "nome": "Mouse Gamer",
  "preco": 199.90,
  "categoria": "Periféricos",
  "descricao": "RGB, 8.000 DPI",
  "ativo": true
}
```

**PUT /produtos/{id}** – *atualizar produto*
```json
{
  "nome": "Mouse Gamer Pro",
  "preco": 249.90,
  "categoria": "Periféricos",
  "descricao": "RGB, 12.000 DPI",
  "ativo": true
}
```
**Resposta 200**:
```json
{
  "id": 10,
  "nome": "Mouse Gamer Pro",
  "preco": 249.90,
  "categoria": "Periféricos",
  "descricao": "RGB, 12.000 DPI",
  "ativo": true
}
```

**DELETE /produtos/{id}** – *remover produto*  
**Resposta 204**: *(corpo vazio)*  
> Se o produto estiver **em uso** por pedidos/itens, retorna `409 Conflict`.

---

### Carrinhos (fluxo + CRUD de itens)

**POST /carrinhos** – *criar carrinho para um cliente*
```json
{
  "clienteId": 1
}
```
**Resposta 201**:
```json
{
  "id": 1,
  "clienteId": 1,
  "total": 0,
  "itens": []
}
```

**POST /carrinhos/{id}/itens** – *adicionar item ao carrinho*
```json
{
  "produtoId": 10,
  "quantidade": 2
}
```
**Resposta 200**:
```json
{
  "id": 5,
  "produtoId": 10,
  "nomeProduto": "Mouse Gamer",
  "quantidade": 2,
  "precoUnitario": 199.90,
  "subtotal": 399.80
}
```

**PUT /carrinhos/{id}/itens/{itemId}** – *atualizar quantidade*
```json
{
  "produtoId": 10,
  "quantidade": 3
}
```
**Resposta 200**:
```json
{
  "id": 5,
  "produtoId": 10,
  "nomeProduto": "Mouse Gamer",
  "quantidade": 3,
  "precoUnitario": 199.90,
  "subtotal": 599.70
}
```

**GET /carrinhos/{id}/itens** – *listar itens*  
**Resposta 200**:
```json
[
  {
    "id": 5,
    "produtoId": 10,
    "nomeProduto": "Mouse Gamer",
    "quantidade": 3,
    "precoUnitario": 199.90,
    "subtotal": 599.70
  }
]
```

**GET /carrinhos/{id}** – *detalhe do carrinho (total + itens)*  
**Resposta 200**:
```json
{
  "id": 1,
  "clienteId": 1,
  "total": 599.70,
  "itens": [
    {
      "id": 5,
      "produtoId": 10,
      "nomeProduto": "Mouse Gamer",
      "quantidade": 3,
      "precoUnitario": 199.90,
      "subtotal": 599.70
    }
  ]
}
```

**DELETE /carrinhos/{id}/itens/{itemId}** – *remover item*  
**Resposta 204**: *(corpo vazio)*

**DELETE /carrinhos/{id}** – *remover carrinho*  
**Resposta 204**: *(corpo vazio)*

---

### Pedidos

**POST /pedidos** – *criar pedido a partir do carrinho*
```json
{
  "carrinhoId": 1
}
```
**Resposta 200**:
```json
{
  "id": 7,
  "clienteId": 1,
  "total": 599.70,
  "status": "CRIADO",
  "itens": [
    {
      "id": 8,
      "produtoId": 10,
      "nomeProduto": "Mouse Gamer",
      "quantidade": 3,
      "precoUnitario": 199.90,
      "subtotal": 599.70
    }
  ],
  "pagamento": null
}
```

**GET /pedidos/{id}** – *detalhar pedido*  
**Resposta 200**:
```json
{
  "id": 7,
  "clienteId": 1,
  "total": 599.70,
  "status": "CRIADO",
  "itens": [ ... ],
  "pagamento": null
}
```

**PUT /pedidos/{id}/status?status=PAGO** – *alterar status*  
**Resposta 200**:
```json
{
  "id": 7,
  "clienteId": 1,
  "total": 599.70,
  "status": "PAGO",
  "itens": [ ... ],
  "pagamento": {
    "id": 3,
    "pedidoId": 7,
    "valor": 599.70,
    "status": "APROVADO",
    "metodo": "PIX"
  }
}
```

---

### Pagamentos

**POST /pagamentos** – *criar pagamento para um pedido (valor deve ser igual ao total)*
```json
{
  "pedidoId": 7,
  "valor": 599.70,
  "metodo": "PIX"
}
```
**Resposta 200**:
```json
{
  "id": 3,
  "pedidoId": 7,
  "valor": 599.70,
  "status": "PENDENTE",
  "metodo": "PIX"
}
```

**PUT /pagamentos/{id}** – *atualizar status do pagamento*
```json
{
  "status": "APROVADO"
}
```
**Resposta 200**:
```json
{
  "id": 3,
  "pedidoId": 7,
  "valor": 599.70,
  "status": "APROVADO",
  "metodo": "PIX"
}
```

**GET /pagamentos/{id}** – *detalhar pagamento*  
**Resposta 200**:
```json
{
  "id": 3,
  "pedidoId": 7,
  "valor": 599.70,
  "status": "APROVADO",
  "metodo": "PIX"
}
```

---

## Estrutura de Pastas
```
src
 ├─ main
 │   ├─ java/br/com/fiap/byteshoponlineapp
 │   │   ├─ domain/              # Entidades JPA
 │   │   ├─ repository/          # JpaRepository
 │   │   ├─ service/             # Regras de negócio
 │   │   ├─ controller/          # Controllers REST
 │   │   │   └─ dto/             # DTOs request/response
 │   │   └─ infrastructure/      # Swagger + ExceptionHandler
 │   └─ resources/
 │       ├─ application.properties
 │       ├─ schema.sql
 │       └─ data.sql
 └─ test/java/...                # (se houver testes)
```

---

## Dependências Principais
- `spring-boot-starter-web` – REST/Servlet
- `spring-boot-starter-data-jpa` – JPA/Hibernate
- `spring-boot-starter-validation` – Bean Validation
- `com.h2database:h2` – banco em memória
- `org.springdoc:springdoc-openapi-starter-webmvc-ui` – Swagger UI
- `lombok` – getters/setters/constructors (habilitar *annotation processing* na IDE)

---

## Dicas & Troubleshooting
- **Swagger não mostra `{id}`**  
  - Use **Try it out**; garanta `@PathVariable("id")` nos controllers e `<parameters>true</parameters>` no `pom.xml`.
- **Erro no `CREATE SCHEMA` (charset/collate)**  
  - H2 não aceita `DEFAULT CHARACTER SET/COLLATE`. Use:
    ```sql
    CREATE SCHEMA IF NOT EXISTS byteshop_db;
    SET SCHEMA byteshop_db;
    ```
