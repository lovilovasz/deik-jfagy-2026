# Todo App

🇭🇺 [Magyar dokumentáció](docs/hu/README.md)

A demo REST API built with **Spring Boot 4 / Java 21**. It manages **persons** and their **todos**
(one person owns many todos) with full CRUD, backed by **PostgreSQL**.

## Features

- CRUD for persons (`/api/persons`) and todos (`/api/todos`)
- Bean Validation on requests, RFC 7807 `ProblemDetail` error responses
- Unique person email (409 on conflict), deleting a person deletes their todos
- OpenAPI / Swagger UI, plus an exported spec in `openapi/openapi.json`
- Docker Compose setup (database + app)
- Quality gates: Checkstyle (Google style) and JaCoCo (80% line coverage)
- GitHub Actions CI

## Quick start

```bash
docker compose up -d --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Try it:

```bash
curl -X POST localhost:8080/api/persons -H "Content-Type: application/json" \
  -d '{"name":"Ada","email":"ada@example.com"}'
curl -X POST localhost:8080/api/todos -H "Content-Type: application/json" \
  -d '{"title":"Write docs","personId":1}'
```

## Documentation

| Document | Contents |
| --- | --- |
| [docs/architecture.md](docs/architecture.md) | Layers, packages, data model, error handling, transactions |
| [docs/api.md](docs/api.md) | Endpoints, request/response shapes, status codes, OpenAPI export |
| [docs/technologies.md](docs/technologies.md) | Every technology and library used, with versions |
| [docs/development.md](docs/development.md) | Build, run, configuration, testing, code style, coverage, CI |
| [docs/spec-driven-workflow.md](docs/spec-driven-workflow.md) | GitHub Spec Kit workflow and the existing specs |

## Project layout

```
.
├── src/main/java/com/jfagy/todo/app/   application code (see docs/architecture.md)
├── src/main/resources/application.yaml runtime configuration
├── src/test/                           tests (H2 in-memory)
├── openapi/openapi.json                generated OpenAPI spec
├── specs/                              Spec Kit feature specs
├── .github/workflows/ci.yml            CI pipeline
├── Dockerfile, compose.yaml            container setup
├── pom.xml                             Maven build
└── CLAUDE.md                           guidance for Claude Code
```
