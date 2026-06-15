# 📝 Checklist de Implementação - GalleriaBank

## 1. Infraestrutura & Modelagem
- [x] Configurar Spring Initializr e dependências
- [x] Criar Entidade `Usuario` com suporte a Remoção Lógica
- [x] Criar Entidade `Cliente` com Unique CPF
- [x] Criar Entidade `Produto` (Valor > 0)
- [x] Criar Entidade `Pedido` com Relacionamentos

## 2. Persistência e Validação
- [x] Custom Validator para CPF (Dígitos verificadores)
- [x] Repositories com Queries de verificação de duplicidade
- [x] DTOs de Request/Response para isolar a camada de visualização

## 3. Regras de Negócio (Services)
- [x] **RN01:** Bloquear login duplicado (mesmo se excluído logicamente)
- [x] **RN02:** Impedir exclusão de Cliente com Pedidos
- [x] **RN03:** Impedir exclusão de Produto vinculado a Pedidos
- [x] **RN04:** Pedido deve ter no mínimo 1 produto

## 4. Controladores e Segurança
- [x] Endpoints de Usuários (Público/Restrito)
- [x] Endpoints de Clientes (Restritos)
- [x] Endpoints de Produtos (Restritos)
- [x] Endpoints de Pedidos (Restritos)
- [x] Configurar Spring Security (Autenticação)

## 5. Qualidade e Entrega
- [x] Handler Global de Exceções (Retornos amigáveis)
- [x] Dockerfile e Docker Compose
- [x] Testes de Unidade (JUnit/Mockito) - Parcial

---
*Status Atual: Planejamento concluído.*