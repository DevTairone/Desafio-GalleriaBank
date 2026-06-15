# 📝 Checklist de Implementação - GalleriaBank

## 1. Infraestrutura & Modelagem
- [x] Configurar Spring Initializr e dependências
- [x] Criar Entidade `Usuario` com suporte a Remoção Lógica
- [x] Criar Entidade `Cliente` com Unique CPF
- [x] Criar Entidade `Produto` (Valor > 0)
- [x] Criar Entidade `Pedido` com Relacionamentos

## 2. Persistência e Validação
- [ ] Custom Validator para CPF (Dígitos verificadores)
- [ ] Repositories com Queries de verificação de duplicidade
- [ ] DTOs de Request/Response para isolar a camada de visualização

## 3. Regras de Negócio (Services)
- [ ] **RN01:** Bloquear login duplicado (mesmo se excluído logicamente)
- [ ] **RN02:** Impedir exclusão de Cliente com Pedidos
- [ ] **RN03:** Impedir exclusão de Produto vinculado a Pedidos
- [ ] **RN04:** Pedido deve ter no mínimo 1 produto

## 4. Controladores e Segurança
- [ ] Endpoints de Usuários (Público/Restrito)
- [ ] Endpoints de Clientes (Restritos)
- [ ] Endpoints de Produtos (Restritos)
- [ ] Endpoints de Pedidos (Restritos)
- [ ] Configurar Spring Security (Autenticação)

## 5. Qualidade e Entrega
- [ ] Handler Global de Exceções (Retornos amigáveis)
- [ ] Dockerfile e Docker Compose
- [ ] Testes de Unidade (JUnit/Mockito)

---
*Status Atual: Planejamento concluído.*