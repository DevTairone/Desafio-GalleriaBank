# 📖 Documentação Técnica - GalleriaBank Compras

Este projeto é um sistema de gestão de compras que permite o gerenciamento de usuários, clientes, produtos e pedidos. A arquitetura foi projetada seguindo padrões de alta coesão e baixo acoplamento, garantindo que a lógica de negócio seja o centro da aplicação e permaneça isolada de detalhes técnicos como bancos de dados ou frameworks web.

---

## 🏗️ Estrutura Arquitetural

O sistema é dividido em três camadas principais, cada uma com responsabilidades bem definidas:

### 1. Core (Núcleo do Negócio)
É a camada mais interna, contendo as regras que definem o comportamento do GalleriaBank.

*   **Domain (Entidades):** Objetos fundamentais como `Usuario`, `Cliente`, `Produto` e `Pedido`. 
    *   Exemplo: A entidade `Pedido` possui lógica própria para calcular o valor total e gerar números de identificação.
*   **Ports (Interfaces):** Contratos que definem como o núcleo deve interagir com o mundo externo (ex: persistência de dados).
    *   Exemplo: `UsuarioRepositoryPort` define o que é necessário para salvar um usuário, sem especificar qual banco de dados será usado.

### 2. Application (Casos de Uso)
Camada responsável por orquestrar o fluxo da aplicação e implementar as regras de negócio específicas.

*   **UseCases:** Classes como `PedidoUseCase` e `ClienteUseCase` que executam as operações do sistema. Elas recebem os dados, validam as regras de negócio e utilizam os **Ports** para persistir as informações.
    *   **Regras Implementadas:**
        *   Validação de CPF único para clientes.
        *   Bloqueio de exclusão de clientes/produtos vinculados a pedidos ativos.
        *   Gerenciamento de exclusão lógica para usuários (Soft Delete).

### 3. Infrastructure (Infraestrutura)
Contém as implementações técnicas e ferramentas de terceiros (Spring Boot, Hibernate, PostgreSQL).

*   **Persistence (Adapters & Repositories):** Implementa os contratos definidos no Core utilizando Spring Data JPA. Os `Adapters` fazem a ponte entre as interfaces do domínio e a tecnologia de banco de dados.
*   **Web (REST API):** 
    *   **Controllers:** Expõem os endpoints da aplicação.
    *   **DTOs:** Objetos de transferência para garantir que dados sensíveis (como senhas) não circulem na rede.
    *   **Exceptions:** Um manipulador global (`GlobalExceptionHandler`) garante que erros retornem mensagens claras e padronizadas em JSON.
*   **Configuration:** Configurações de segurança (Basic Auth) e documentação automática com Swagger (OpenAPI).

---

## 🔐 Segurança e Validação

*   **Autenticação:** O sistema utiliza **Spring Security** com autenticação básica. Senhas de usuários são criptografadas com **BCrypt** antes de serem armazenadas.
*   **Validação de CPF:** Existe um validador customizado (`CpfValidator`) que verifica matematicamente os dígitos verificadores do CPF, impedindo cadastros inválidos via `@Valid`.
*   **Tratamento de Erros:** Exceções de negócio (`BusinessException`) e de busca (`ResourceNotFoundException`) são mapeadas para os status HTTP 400 e 404, respectivamente.

---

## 🔄 Fluxo de Funcionamento

Para a criação de um novo **Pedido**, o fluxo é o seguinte:
1.  O `PedidoController` recebe um JSON com o ID do cliente e a lista de IDs de produtos.
2.  O `PedidoUseCase` é acionado e valida se o cliente existe e se os produtos estão disponíveis.
3.  O `PedidoUseCase` cria a entidade `Pedido`, calcula o total e solicita ao `PedidoRepositoryPort` o salvamento.
4.  O `PedidoRepositoryAdapter` (infraestrutura) recebe o objeto de domínio e o salva no banco PostgreSQL via JPA.
5.  O resultado é convertido para um `PedidoResponseDTO` e retornado ao cliente.

---

## 🛠️ Tecnologias Utilizadas

*   **Java 21**
*   **Spring Boot 3.2.5** (Web, Data JPA, Security, Validation)
*   **PostgreSQL** (Banco de dados principal)
*   **Docker** (Containerização do banco de dados)
*   **SpringDoc OpenAPI** (Documentação Swagger)
*   **JUnit 5 / Mockito** (Testes de unidade da lógica de negócio)

---

## 🚀 Como Executar

### Pré-requisitos
*   Docker e Docker Compose instalados.
*   JDK 21.

### Passos
1.  **Subir o Banco de Dados:**
    ```bash
    docker-compose up -d
    ```
2.  **Compilar e Rodar:**
    ```bash
    ./mvnw spring-boot:run
    ```
3.  **Acessar Documentação:**
    Abra o navegador em: `http://localhost:8080/swagger-ui.html`

---

## 📊 Resumo de Funcionalidades

| Entidade | Funcionalidade | Detalhe |
| :--- | :--- | :--- |
| **Usuário** | Cadastro | Criptografia de senha e verificação de login único. |
| **Usuário** | Exclusão Lógica | O registro não é deletado, apenas marcado como `excluido`. |
| **Cliente** | Validação | CPF validado matematicamente e deve ser único. |
| **Cliente** | Segurança | Bloqueio de exclusão se houver pedidos. |
| **Produto** | Preço | Validação para garantir que o valor seja > 0. |
| **Pedido** | Automação | Geração automática de número de pedido (ex: PED-1). |
| **Pedido** | Agregação | Cálculo dinâmico do valor total somando os produtos. |

---
*Documentação gerada para GalleriaBank Compras.*