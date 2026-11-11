# Todo App

Demo Spring Boot 4 / Java 21 REST app: `Person` (1) —— (N) `Todo`, full CRUD, PostgreSQL.

## Commands
- Build + test: `./mvnw -B verify` (tests use in-memory H2, no Docker needed)
- Style only: `./mvnw -B checkstyle:check` (Google Java style, 2-space indent; any violation fails the build)
- Coverage: `verify` fails if overall line or branch coverage is below 80%; report at `target/site/jacoco/index.html`
- Run everything (db + app): `docker compose up -d --build` → http://localhost:8080
- Run only the DB for local dev: `docker compose up -d db`, then `./mvnw spring-boot:run`
- Swagger UI: http://localhost:8080/swagger-ui.html

## Architecture (package `com.jfagy.todo.app`)
`controller` (REST, `/api/persons`, `/api/todos`) → `service` (`@Transactional`) → `repository` (Spring Data JPA) → `entity`.
`dto` = Java records for requests (validated with `@Valid`) and responses; never expose entities.
`exception` = `NotFoundException` (404), `ConflictException` (409), handled in `GlobalExceptionHandler` as `ProblemDetail`.

## Conventions
- Lombok for entities/services (`@RequiredArgsConstructor` constructor injection). No field `@Autowired`.
- A Todo always has exactly one Person (`optional = false`); a Person owns many Todos (cascade + orphanRemoval).
- DB config comes from env vars `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` (defaults point to the compose Postgres on localhost). Schema is `ddl-auto: update` — demo only; use Flyway/Liquibase for anything real.
- Jackson 3 (Boot 4): primitive request fields that may be omitted must be wrappers (e.g. `Boolean`).
- Add a test in `src/test` for every new endpoint (MockMvc, `TodoApiTests` is the template).

## Spec-driven workflow (GitHub Spec Kit)
Skills live in `.claude/skills/speckit-*`; templates/scripts in `.specify/`. For non-trivial features run, in order:
`/speckit-constitution` (once) → `/speckit-specify` → `/speckit-clarify` → `/speckit-plan` → `/speckit-tasks` → `/speckit-implement`.
Specs are written to `specs/`. Do not edit `.specify/` internals by hand.

## Rules for Claude
- Keep changes minimal and consistent with the layering above; run `./mvnw -B verify` before declaring done.
- Never commit secrets; the compose credentials are demo-only.
- Do not commit unless asked.
