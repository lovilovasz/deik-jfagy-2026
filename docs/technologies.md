# Technologies

## Runtime

| Technology | Version | Purpose |
| --- | --- | --- |
| Java | 21 | Language / runtime (records, modern APIs) |
| Spring Boot | 4.1.2 | Application framework (parent POM) |
| Spring Web MVC (`spring-boot-starter-webmvc`) | managed | REST controllers |
| Spring Data JPA (`spring-boot-starter-data-jpa`) | managed | Persistence, Hibernate ORM |
| Bean Validation (`spring-boot-starter-validation`) | managed | `@Valid`, `@NotBlank`, `@Email`, `@NotNull` |
| Jackson 3 | managed | JSON (stricter about primitives – use wrappers for optional fields) |
| PostgreSQL + JDBC driver | 17 (image) | Production database |
| springdoc-openapi (`springdoc-openapi-starter-webmvc-ui`) | 3.2.0 | OpenAPI generation and Swagger UI |
| Lombok | managed | `@Getter/@Setter/@NoArgsConstructor`, `@RequiredArgsConstructor` |

## Testing

| Technology | Purpose |
| --- | --- |
| JUnit 5 + Spring Boot Test | Test framework, `@SpringBootTest` |
| MockMvc (`spring-boot-starter-webmvc-test`) | HTTP-level tests without a server |
| `spring-boot-starter-data-jpa-test` | JPA test support |
| H2 (in-memory, PostgreSQL mode) | Test database – no Docker needed |

## Build and quality

| Technology | Version | Purpose |
| --- | --- | --- |
| Maven (wrapper `mvnw`) | 3.9 | Build |
| Checkstyle (maven-checkstyle-plugin 3.6.0, Checkstyle 14.4.0) | – | Google Java style; runs in `validate`, any warning fails the build; includes tests |
| JaCoCo | 0.8.16 | Coverage report; `verify` fails below 80% line coverage |
| GitHub Actions | – | CI on PRs and pushes to `main` |

## Infrastructure

| Technology | Purpose |
| --- | --- |
| Docker (multi-stage `Dockerfile`) | Build with `maven:3.9-eclipse-temurin-21`, run on `eclipse-temurin:21-jre`, port 8080 |
| Docker Compose (`compose.yaml`) | `db` (postgres:17-alpine, healthcheck, `pgdata` volume) and `app` |

## Process tooling

| Technology | Purpose |
| --- | --- |
| GitHub Spec Kit | Spec-driven workflow (`.specify/`, `.claude/skills/speckit-*`), specs in `specs/` |
| Claude Code | AI assistant guided by `CLAUDE.md` |
