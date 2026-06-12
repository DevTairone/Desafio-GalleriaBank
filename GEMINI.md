# Contexto do Projeto: Desafio GalleriaBank (Back-End)

Você deve atuar como um desenvolvedor Java especialista em Spring Boot. O objetivo é construir a API REST para o desafio técnico do GalleriaBank utilizando as regras de negócio e restrições técnicas abaixo.

## 🛠️ Tecnologias e Configuração
* **Linguagem:** Java 8+
* **Framework:** Spring Boot (Web, Validation, Data JPA)
* **Banco de Dados:** H2 (Memória para testes) ou PostgreSQL via Docker
* **Gerenciador de Dependências:** Maven ou Gradle

---

## 📐 Modelagem de Dados e Regras de Negócio

### 1. Usuário
* **Campos:**
    * `id` (Long, auto incremento)
    * `nome` (String, obrigatório, mínimo de 3 caracteres)
    * `login` (String, obrigatório, único)
    * `senha` (String)
* **Regra Crítica:** Não permitir o cadastro de um `login` existente, **mesmo se o usuário antigo tiver sido excluído** (remoção lógica).

### 2. Cliente
* **Campos:**
    * `id` (Long, auto incremento)
    * `nome` (String, obrigatório, mínimo de 3 caracteres)
    * `cpf` (String, obrigatório, único, com validação de formato e dígitos)
    * `telefone` (String)
* **Regra Crítica:** Não permitir dois clientes ativos com o mesmo CPF.

### 3. Produto
* **Campos:**
    * `id` (Long, auto incremento)
    * `descricao` (String, obrigatório)
    * `valor` (BigDecimal/Double, obrigatório, deve ser maior que 0)

### 4. Pedido
* **Campos:**
    * `id` (Long, auto incremento)
    * `numero` (String/Long, opcional - pode ser o próprio id ou um campo próprio)
    * `dataEmissao` (LocalDateTime, automática com o horário atual do sistema)
    * `descricao` (String, texto livre)
    * `clienteId` (Long, obrigatório)
    * `produtoId` (Lista de IDs de produtos - obrigatório ter pelo menos 1 produto no pedido)

---

## 🛣️ Endpoints da API REST
*Atenção: Os endpoints marcados com `[Restrito]` devem possuir alguma camada de segurança/autenticação.*

### Usuários
* `POST /usuarios` - Cria um novo usuário
* `GET /usuarios/{id}` - Busca usuário por ID `[Restrito]`
* `PUT /usuarios/{id}` - Atualiza usuário `[Restrito]`
* `DELETE /usuarios/{id}` - Remove usuário via **Remoção Lógica** `[Restrito]`

### Clientes
* `POST /clientes` - Cria um novo cliente `[Restrito]`
* `GET /clientes/{id}` - Busca cliente por ID `[Restrito]`
* `PUT /clientes/{id}` - Atualiza cliente `[Restrito]`
* `DELETE /clientes/{id}` - Remove cliente `[Restrito]` *(Regra de negócio definida: Bloquear a remoção se o cliente possuir pedidos vinculados)*.

### Produtos
* `POST /produtos` - Cria um novo produto `[Restrito]`
* `GET /produtos/{id}` - Busca produto por ID `[Restrito]`
* `PUT /produtos/{id}` - Atualiza produto `[Restrito]`
* `DELETE /produtos/{id}` - Remove produto `[Restrito]` *(Regra de negócio definida: Bloquear a remoção se o produto estiver associado a algum pedido ativo)*.

### Pedidos
* `POST /pedidos` - Cria um pedido para um cliente contendo a lista de itens/produtos `[Restrito]`
* `GET /pedidos/{id}` - Detalhes do pedido, incluindo a list