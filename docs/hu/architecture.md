# Architektúra

Alapcsomag: `com.jfagy.todo.app`

[English version](../architecture.md)

## Rétegek

```
HTTP ──▶ controller ──▶ service ──▶ repository ──▶ entity ──▶ PostgreSQL
            │              │
           dto         exception
```

| Csomag | Felelősség |
| --- | --- |
| `controller` | `PersonController` (`/api/persons`), `TodoController` (`/api/todos`). HTTP-t képez le service-hívásokra, `@Valid`-dal validál, válaszokat épít (201 + új erőforrás URI-ja, 204). springdoc annotációkat tartalmaz. |
| `service` | `PersonService`, `TodoService`. Üzleti szabályok; osztályszintű `@Transactional`, olvasásnál `readOnly = true`. |
| `repository` | Spring Data JPA interfészek (`PersonRepository.existsByEmail`, `TodoRepository.findByPersonId`). |
| `entity` | JPA entitások: `Person` és `Todo`. Soha nem kerülnek ki az API-n. |
| `dto` | Java record-ok: `PersonRequest`, `TodoRequest` (validáltak), `PersonResponse`, `TodoResponse` (`from(entity)` leképezővel). |
| `exception` | `NotFoundException` (404), `ConflictException` (409), `GlobalExceptionHandler`. |
| `config` | `OpenApiConfig` – az API címe/verziója/leírása. |

Szabályok: a controller csak service-eket hív; a service a repository-kat használja (a `TodoService` a
`PersonService`-t a tulajdonos feloldására); entitás nem megy ki JSON-ként; a függőségek Lombok
`@RequiredArgsConstructor`-ral, konstruktoron keresztül érkeznek (nincs mező-`@Autowired`).

## Adatmodell

```
Person (1) ────────── (N) Todo
```

**person**

| Oszlop | Típus | Megjegyzés |
| --- | --- | --- |
| id | bigint | PK, identity |
| name | varchar | kötelező |
| email | varchar | kötelező, egyedi |

**todo**

| Oszlop | Típus | Megjegyzés |
| --- | --- | --- |
| id | bigint | PK, identity |
| title | varchar | kötelező |
| description | varchar | opcionális |
| completed | boolean | kötelező, kihagyva `false` |
| person_id | bigint | FK → person.id, kötelező |

- Egy `Todo`-nak mindig pontosan egy `Person`-je van (`@ManyToOne(optional = false, fetch = LAZY)`).
- Egy `Person` több todo-t birtokol: `@OneToMany(mappedBy = "person", cascade = ALL, orphanRemoval = true)`,
  ezért a személy törlésekor a teendői is törlődnek.
- A sémát a Hibernate hozza létre (`ddl-auto: update`). Ez csak demóra jó; valódi használathoz Flyway
  vagy Liquibase kell. Az `open-in-view` ki van kapcsolva.

## Kérés útja (todo létrehozása)

1. `POST /api/todos` JSON törzzsel → a Jackson 3 `TodoRequest`-té deszerializálja.
2. A `@Valid` ellenőrzi: `title` nem üres, `personId` nem null; hiba esetén 400.
3. A `TodoService.create` a `PersonService.get`-tel betölti a tulajdonost (hiányában 404), leképezi a
   mezőket és menti – mindez egy tranzakcióban.
4. A controller `TodoResponse`-ra képezi az entitást és `201`-et ad az új erőforrás URI-jával.

## Hibakezelés

A `GlobalExceptionHandler` (`@RestControllerAdvice`) RFC 7807 `ProblemDetail`-t ad vissza:

| Ok | Státusz | Részlet |
| --- | --- | --- |
| `NotFoundException` | 404 | `"<Entitás> not found: <id>"` |
| `ConflictException` | 409 | pl. `"Email already in use: …"` |
| `MethodArgumentNotValidException` | 400 | `"mező üzenet; mező üzenet"` |

A hibaüzenetek szövege angol marad.

## Fontos konvenciók

- A kihagyható primitív kérésmezők wrapper típusúak legyenek (pl. `Boolean completed`) a Spring Boot 4
  Jackson 3 viselkedése miatt.
- Minden új végponthoz MockMvc teszt kell; a sablon a `TodoApiTests`.
- Az adatbázis-kapcsolat a `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` környezeti változókból jön.
