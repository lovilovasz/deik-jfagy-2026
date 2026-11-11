# Todo App (magyar)

Egy **Spring Boot 4 / Java 21** alapú demó REST API. **Személyeket** és a hozzájuk tartozó
**teendőket** (todo) kezel: egy személynek több teendője lehet. Teljes CRUD, **PostgreSQL** adatbázissal.

[English version](../../README.md)

## Funkciók

- Személyek (`/api/persons`) és teendők (`/api/todos`) CRUD műveletei
- Bean Validation a kéréseken, RFC 7807 `ProblemDetail` hibaválaszok
- Egyedi e-mail cím személyenként (ütközésnél 409), személy törlésekor a teendői is törlődnek
- OpenAPI / Swagger UI, valamint exportált spec az `openapi/openapi.json` fájlban
- Docker Compose beállítás (adatbázis + alkalmazás)
- Minőségi kapuk: Checkstyle (Google stílus) és JaCoCo (80% sorlefedettség)
- GitHub Actions CI

## Gyors indítás

```bash
docker compose up -d --build
```

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Kipróbálás:

```bash
curl -X POST localhost:8080/api/persons -H "Content-Type: application/json" \
  -d '{"name":"Ada","email":"ada@example.com"}'
curl -X POST localhost:8080/api/todos -H "Content-Type: application/json" \
  -d '{"title":"Dokumentáció írása","personId":1}'
```

## Dokumentáció

| Dokumentum | Tartalom |
| --- | --- |
| [architecture.md](architecture.md) | Rétegek, csomagok, adatmodell, hibakezelés, tranzakciók |
| [api.md](api.md) | Végpontok, kérés/válasz formátumok, státuszkódok, OpenAPI export |
| [technologies.md](technologies.md) | Az összes használt technológia és könyvtár, verziókkal |
| [development.md](development.md) | Build, futtatás, konfiguráció, tesztelés, kódstílus, lefedettség, CI |
| [spec-driven-workflow.md](spec-driven-workflow.md) | GitHub Spec Kit munkafolyamat és a meglévő specifikációk |

## Projektstruktúra

```
.
├── src/main/java/com/jfagy/todo/app/   alkalmazáskód (lásd architecture.md)
├── src/main/resources/application.yaml futásidejű konfiguráció
├── src/test/                           tesztek (H2 memóriában)
├── openapi/openapi.json                generált OpenAPI spec
├── specs/                              Spec Kit funkció-specifikációk
├── .github/workflows/ci.yml            CI pipeline
├── Dockerfile, compose.yaml            konténer beállítás
├── pom.xml                             Maven build
└── CLAUDE.md                           útmutató a Claude Code számára
```
