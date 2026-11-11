# Technológiák

[English version](../technologies.md)

## Futásidő

| Technológia | Verzió | Cél |
| --- | --- | --- |
| Java | 21 | Nyelv / futtatókörnyezet (record-ok, modern API-k) |
| Spring Boot | 4.1.2 | Alkalmazás keretrendszer (parent POM) |
| Spring Web MVC (`spring-boot-starter-webmvc`) | kezelt | REST controllerek |
| Spring Data JPA (`spring-boot-starter-data-jpa`) | kezelt | Perzisztencia, Hibernate ORM |
| Bean Validation (`spring-boot-starter-validation`) | kezelt | `@Valid`, `@NotBlank`, `@Email`, `@NotNull` |
| Jackson 3 | kezelt | JSON (szigorúbb a primitívekkel – kihagyható mezőkhöz wrapper kell) |
| PostgreSQL + JDBC driver | 17 (image) | Éles adatbázis |
| springdoc-openapi (`springdoc-openapi-starter-webmvc-ui`) | 3.2.0 | OpenAPI generálás és Swagger UI |
| Lombok | kezelt | `@Getter/@Setter/@NoArgsConstructor`, `@RequiredArgsConstructor` |

## Tesztelés

| Technológia | Cél |
| --- | --- |
| JUnit 5 + Spring Boot Test | Teszt keretrendszer, `@SpringBootTest` |
| MockMvc (`spring-boot-starter-webmvc-test`) | HTTP szintű tesztek szerver nélkül |
| `spring-boot-starter-data-jpa-test` | JPA teszttámogatás |
| H2 (memóriában, PostgreSQL módban) | Teszt adatbázis – nem kell Docker |

## Build és minőség

| Technológia | Verzió | Cél |
| --- | --- | --- |
| Maven (wrapper: `mvnw`) | 3.9 | Build |
| Checkstyle (maven-checkstyle-plugin 3.6.0, Checkstyle 14.4.0) | – | Google Java stílus; a `validate` fázisban fut, bármely figyelmeztetés megbuktatja a buildet; a teszteket is ellenőrzi |
| JaCoCo | 0.8.16 | Lefedettségi riport; a `verify` 80% sorlefedettség alatt elbukik |
| GitHub Actions | – | CI PR-eken és a `main`-re érkező push-okon |

## Infrastruktúra

| Technológia | Cél |
| --- | --- |
| Docker (többlépcsős `Dockerfile`) | Build: `maven:3.9-eclipse-temurin-21`, futtatás: `eclipse-temurin:21-jre`, 8080-as port |
| Docker Compose (`compose.yaml`) | `db` (postgres:17-alpine, healthcheck, `pgdata` volume) és `app` |

## Folyamat-eszközök

| Technológia | Cél |
| --- | --- |
| GitHub Spec Kit | Specifikáció-vezérelt munkafolyamat (`.specify/`, `.claude/skills/speckit-*`), specek: `specs/` |
| Claude Code | A `CLAUDE.md` alapján dolgozó AI asszisztens |
