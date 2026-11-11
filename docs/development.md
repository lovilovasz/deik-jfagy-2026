# Development guide

## Prerequisites

- JDK 21
- Docker (only for running PostgreSQL / the full stack; tests don't need it)

## Commands

| Goal | Command |
| --- | --- |
| Build + test + style + coverage | `./mvnw -B verify` |
| Style only | `./mvnw -B checkstyle:check` |
| Run everything (db + app) | `docker compose up -d --build` |
| Run only the DB | `docker compose up -d db` |
| Run the app locally | `./mvnw spring-boot:run` (after starting the DB) |
| Stop and remove stack | `docker compose down` (add `-v` to drop the data volume) |

On Windows use `mvnw.cmd` in PowerShell/cmd, or `./mvnw` in Git Bash.

## Configuration

`src/main/resources/application.yaml`:

| Env var | Default | Meaning |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/tododb` | JDBC URL |
| `DB_USERNAME` | `todo` | DB user |
| `DB_PASSWORD` | `todo` | DB password |

Other settings: `spring.jpa.hibernate.ddl-auto: update` (demo only), `open-in-view: false`,
pretty-printed OpenAPI JSON. In Compose the app gets `DB_URL=jdbc:postgresql://db:5432/tododb`.
The credentials are demo-only – never commit real secrets.

Tests use `src/test/resources/application.yaml`: H2 in-memory (`MODE=PostgreSQL`) with
`ddl-auto: create-drop`.

## Testing

- `ApplicationTests` – context loads.
- `TodoApiTests` – MockMvc CRUD flow for persons and todos; the template for new endpoint tests.
- `OpenApiSpecTests` – fetches `/v3/api-docs` and rewrites `openapi/openapi.json`.

Rule: every new endpoint gets a MockMvc test in `src/test`.

## Code style (Checkstyle)

Google Java style (2-space indent, Javadoc on public types/methods). Checkstyle runs in the Maven
`validate` phase and includes test sources; any violation, even a warning, fails the build. Fix
violations rather than suppressing them.

## Coverage (JaCoCo)

The agent is attached during tests; `verify` generates the report at
`target/site/jacoco/index.html` and fails if overall **line or branch** coverage is below **80%**
(property `coverage.minimum` in `pom.xml`).

## CI

`.github/workflows/ci.yml` runs on pull requests to `main` and pushes to `main`: checkout, Temurin 21
with Maven cache, `./mvnw -B verify`, builds the Docker image, publishes the jar to Artifactory (pushes to `main` only, and only when the
repository variable `ARTIFACTORY_URL` and secrets `ARTIFACTORY_USERNAME` / `ARTIFACTORY_TOKEN` are
set), then uploads the JaCoCo report and `checkstyle-result.xml` as
the `quality-reports` artifact. Concurrent runs on the same ref are cancelled.

## Docker image

`Dockerfile` is multi-stage: dependencies are resolved first (layer cache), then
`mvn package -DskipTests` builds the jar, which runs on a JRE 21 image exposing port 8080.

## Contributing conventions

- Keep changes minimal and consistent with the layering in [architecture.md](architecture.md).
- Run `./mvnw -B verify` before considering work done.
- Use a new branch for each piece of work; don't commit secrets.
