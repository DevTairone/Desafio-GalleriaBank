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

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras"
docker-compose up -d
.\mvnw.cmd spring-boot:run
```

Backend:
- API: `http://localhost:8080`
- Swagger: `http://localhost:8080/swagger-ui.html`

Credenciais basicas (Spring Security):
- usuario: `admin`
- senha: `adminpassword`

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
3. Faça login no frontend com `admin` / `adminpassword`.
4. Navegue por `Usuarios`, `Clientes`, `Produtos` e `Pedidos`.
5. Teste operacoes basicas (busca por ID e chamadas protegidas).

## Build e validacao

### Backend

```powershell
Set-Location "C:\Users\tairo\Desktop\novo-projeto\compras"
.\mvnw.cmd test
.\mvnw.cmd clean package
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
- Confirme usuario/senha (`admin` / `adminpassword`).
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

