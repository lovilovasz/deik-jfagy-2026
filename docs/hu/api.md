# API referencia

Alap URL: `http://localhost:8080`. Minden törzs JSON. Interaktív dokumentáció: `/swagger-ui.html`.

[English version](../api.md)

## Személyek – `/api/persons`

| Metódus | Útvonal | Leírás | Siker | Hibák |
| --- | --- | --- | --- | --- |
| POST | `/api/persons` | Személy létrehozása | 201 | 400 validáció, 409 foglalt e-mail |
| GET | `/api/persons` | Összes személy listázása | 200 | – |
| GET | `/api/persons/{id}` | Egy személy lekérése | 200 | 404 |
| PUT | `/api/persons/{id}` | Személy módosítása | 200 | 400, 404, 409 |
| DELETE | `/api/persons/{id}` | Személy törlése **a teendőivel együtt** | 204 | 404 |

Kérés (`PersonRequest`):

```json
{ "name": "Ada", "email": "ada@example.com" }
```

A `name` nem lehet üres; az `email` nem üres, érvényes és egyedi cím legyen.

Válasz (`PersonResponse`):

```json
{ "id": 1, "name": "Ada", "email": "ada@example.com" }
```

## Teendők – `/api/todos`

| Metódus | Útvonal | Leírás | Siker | Hibák |
| --- | --- | --- | --- | --- |
| POST | `/api/todos` | Teendő létrehozása egy személynek | 201 | 400, 404 nincs ilyen személy |
| GET | `/api/todos` | Teendők listázása; opcionális `?personId=` szűrő | 200 | – |
| GET | `/api/todos/{id}` | Egy teendő lekérése | 200 | 404 |
| PUT | `/api/todos/{id}` | Teendő módosítása (személy átállítható) | 200 | 400, 404 |
| DELETE | `/api/todos/{id}` | Teendő törlése | 204 | 404 |

Kérés (`TodoRequest`):

```json
{ "title": "Dokumentáció írása", "description": "opcionális", "completed": false, "personId": 1 }
```

A `title` nem lehet üres, a `personId` kötelező, a `completed` opcionális (alapértelmezés: `false`).

Válasz (`TodoResponse`):

```json
{ "id": 1, "title": "Dokumentáció írása", "description": "opcionális", "completed": false, "personId": 1 }
```

## Hibaformátum

A hibák RFC 7807 `ProblemDetail` formátumúak:

```json
{
  "type": "about:blank",
  "title": "Not Found",
  "status": 404,
  "detail": "Person not found: 42",
  "instance": "/api/persons/42"
}
```

## OpenAPI

- Élő spec: `GET /v3/api-docs` (formázott), felület: `/swagger-ui.html`.
- Exportált spec: [`openapi/openapi.json`](../../openapi/openapi.json). Az `OpenApiSpecTests`
  minden `./mvnw test` (vagy `verify`) futáskor újragenerálja. API-változás után commitold a frissített
  fájlt; a frontend kliens generálásához készült.
- A metaadatok (cím: "Todo API", verzió: "v1") a `config/OpenApiConfig`-ban vannak; a műveletek
  leírása a controllerek annotációiban.
