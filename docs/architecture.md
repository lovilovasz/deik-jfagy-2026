# Architecture

Base package: `com.jfagy.todo.app`

## Layers

```
HTTP ──▶ controller ──▶ service ──▶ repository ──▶ entity ──▶ PostgreSQL
            │              │
           dto         exception
```

| Package | Responsibility |
| --- | --- |
| `controller` | `PersonController` (`/api/persons`), `TodoController` (`/api/todos`). Maps HTTP to service calls, validates input with `@Valid`, builds responses (201 + `Location`-style URI, 204). Carries springdoc annotations. |
| `service` | `PersonService`, `TodoService`. Business rules; class-level `@Transactional`, `readOnly = true` on reads. |
| `repository` | Spring Data JPA interfaces (`PersonRepository.existsByEmail`, `TodoRepository.findByPersonId`). |
| `entity` | JPA entities `Person` and `Todo`. Never exposed over the API. |
| `dto` | Java records: `PersonRequest`, `TodoRequest` (validated), `PersonResponse`, `TodoResponse` (with `from(entity)` mappers). |
| `exception` | `NotFoundException` (404), `ConflictException` (409), `GlobalExceptionHandler`. |
| `config` | `OpenApiConfig` – API title/version/description. |

Rules: controllers talk only to services; services talk to repositories (and `TodoService` uses
`PersonService` to resolve the owner); entities never leave the service layer as JSON; dependencies
are injected via Lombok `@RequiredArgsConstructor` (no field `@Autowired`).

## Data model

```
Person (1) ────────── (N) Todo
```

**person**

| Column | Type | Notes |
| --- | --- | --- |
| id | bigint | PK, identity |
| name | varchar | not null |
| email | varchar | not null, unique |

**todo**

| Column | Type | Notes |
| --- | --- | --- |
| id | bigint | PK, identity |
| title | varchar | not null |
| description | varchar | nullable |
| completed | boolean | not null, defaults to `false` when omitted in a request |
| person_id | bigint | FK → person.id, not null |

- A `Todo` always has exactly one `Person` (`@ManyToOne(optional = false, fetch = LAZY)`).
- A `Person` owns many todos: `@OneToMany(mappedBy = "person", cascade = ALL, orphanRemoval = true)`,
  so deleting a person deletes their todos.
- Schema is created by Hibernate (`ddl-auto: update`). This is demo-only; use Flyway or Liquibase for
  anything real. `open-in-view` is disabled.

## Request flow (create todo)

1. `POST /api/todos` with JSON body → Jackson 3 deserialises into `TodoRequest`.
2. `@Valid` checks `title` not blank and `personId` not null; failure → 400.
3. `TodoService.create` loads the owner via `PersonService.get` (404 if missing), maps fields,
   saves, all in one transaction.
4. Controller maps the entity to `TodoResponse` and returns `201` with the new resource URI.

## Error handling

`GlobalExceptionHandler` (`@RestControllerAdvice`) returns RFC 7807 `ProblemDetail`:

| Cause | Status | Detail |
| --- | --- | --- |
| `NotFoundException` | 404 | `"<Entity> not found: <id>"` |
| `ConflictException` | 409 | e.g. `"Email already in use: …"` |
| `MethodArgumentNotValidException` | 400 | `"field message; field message"` |

## Notable conventions

- Primitive request fields that may be omitted must be wrappers (e.g. `Boolean completed`),
  because of Jackson 3 behaviour in Spring Boot 4.
- Every new endpoint needs a MockMvc test; `TodoApiTests` is the template.
- DB connection comes from env vars `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
