# Compras - Backend + Frontend

Aplicacao de gestao de compras com arquitetura em camadas, API REST em Spring Boot e frontend em Angular.

## Visao geral

O projeto pode estar organizado de duas formas:

- **Cenario A (pastas separadas)**
  - `C:\Users\tairo\Desktop\novo-projeto\compras` (backend)
  - `C:\Users\tairo\Desktop\novo-projeto\compras-frontend` (frontend)

- **Cenario B (mesma pasta)**
  - `C:\Users\tairo\Desktop\novo-projeto\compras`
  - `C:\Users\tairo\Desktop\novo-projeto\compras\compras-frontend`

## Stack

### Backend
- Java 8+ (projeto atual configurado com Java 21)
- Spring Boot (`Web`, `Validation`, `Data JPA`, `Security`)
- PostgreSQL (via Docker Compose)
- Maven Wrapper (`mvnw.cmd`)

### Frontend
- Angular 13+ (implementado com Angular 17)
- Angular Material

## Pre-requisitos

- Git
- Java JDK
- Node.js + npm
- Docker + Docker Compose

## Executar localmente

### 1) Subir backend

As credenciais (banco e usuario admin) nao ficam no repositorio. Elas sao lidas de variaveis de ambiente, fornecidas pelo [Infisical](https://infisical.com) conforme a branch atual (`dev`, `homolog` ou `prod`). A lista de variaveis esta em `.env.exemplo`.

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras"
infisical run -- docker compose up -d
infisical run -- .\mvnw.cmd spring-boot:run
```

Para rodar pela IDE (sem `infisical run`), gere o arquivo local com os segredos da branch atual:

```powershell
infisical.cmd export --format=yaml --output-file=.env.yaml
```

O Spring le o `.env.yaml` automaticamente (`spring.config.import`) e ele esta no `.gitignore`. Sem acesso ao Infisical, copie `.env.exemplo` para `.env.yaml` e preencha os valores. Use o formato YAML: o formato `dotenv` do Infisical coloca aspas nos valores, e o Spring as leria como parte do valor.

Backend:
- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`

Credenciais basicas (Spring Security):
- usuario: valor de `ADMIN_USUARIO`
- senha: valor de `ADMIN_SENHA` (consultar no Infisical, ambiente da branch atual)

### 2) Subir frontend

#### Cenario A (frontend fora de `compras`)

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras-frontend"
npm install
npm start
```

#### Cenario B (frontend dentro de `compras`)

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras\compras-frontend"
npm install
npm start
```

Frontend:
- `http://localhost:4200`

## Fluxo rapido de teste (end-to-end)

1. Abra o Swagger em `http://localhost:8080/swagger-ui.html` para confirmar backend online.
2. Abra o frontend em `http://localhost:4200`.
3. Faça login no frontend com `ADMIN_USUARIO` / `ADMIN_SENHA` do ambiente atual.
4. Navegue por `Usuarios`, `Clientes`, `Produtos` e `Pedidos`.
5. Teste operacoes basicas (busca por ID e chamadas protegidas).

## Build e validacao

### Backend

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras"
infisical run -- .\mvnw.cmd test
infisical run -- .\mvnw.cmd clean package
```

### Frontend

```powershell
# ajuste o caminho conforme o seu cenario
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras-frontend"
npm run build
```

## Problemas comuns

### Frontend nao conecta na API
- Verifique se o backend esta no ar (`http://localhost:8080`).
- Verifique `apiBaseUrl` nos arquivos de ambiente do frontend.
- Verifique CORS no backend, se necessario.

### Erro 401 no frontend
- Confirme usuario/senha (`ADMIN_USUARIO` / `ADMIN_SENHA` no Infisical, ambiente da branch atual).
- Confirme envio do header `Authorization: Basic ...`.

### Porta em uso
- Backend: ajuste `server.port` em `src/main/resources/application.yaml`.
- Frontend:

```powershell
npm start -- --port 4201
```

## Encerrar ambiente

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras"
docker-compose down
```

