# Fejlesztői útmutató

[English version](../development.md)

## Előfeltételek

- JDK 21
- Docker (csak a PostgreSQL / teljes stack futtatásához; a teszteknek nem kell)

## Parancsok

| Cél | Parancs |
| --- | --- |
| Build + teszt + stílus + lefedettség | `./mvnw -B verify` |
| Csak stílusellenőrzés | `./mvnw -B checkstyle:check` |
| Minden indítása (db + app) | `docker compose up -d --build` |
| Csak az adatbázis | `docker compose up -d db` |
| Alkalmazás helyi futtatása | `./mvnw spring-boot:run` (az adatbázis indítása után) |
| Stack leállítása és törlése | `docker compose down` (`-v`-vel az adat-volume is törlődik) |

Windowson PowerShellben/cmd-ben `mvnw.cmd`, Git Bashben `./mvnw`.

## Konfiguráció

`src/main/resources/application.yaml`:

| Környezeti változó | Alapérték | Jelentés |
| --- | --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/tododb` | JDBC URL |
| `DB_USERNAME` | `todo` | Adatbázis felhasználó |
| `DB_PASSWORD` | `todo` | Adatbázis jelszó |

Egyéb beállítások: `spring.jpa.hibernate.ddl-auto: update` (csak demó), `open-in-view: false`,
formázott OpenAPI JSON. Compose-ban az app `DB_URL=jdbc:postgresql://db:5432/tododb` értéket kap.
A hitelesítő adatok csak demóra valók – valódi titkot soha ne commitolj.

A tesztek a `src/test/resources/application.yaml`-t használják: H2 memóriában (`MODE=PostgreSQL`),
`ddl-auto: create-drop`.

## Tesztelés

- `ApplicationTests` – a kontextus betöltődik.
- `TodoApiTests` – MockMvc CRUD folyamat személyekre és teendőkre; az új végpontok tesztjeinek sablonja.
- `OpenApiSpecTests` – lekéri a `/v3/api-docs`-ot és újraírja az `openapi/openapi.json`-t.

Szabály: minden új végponthoz kell MockMvc teszt a `src/test` alatt.

## Kódstílus (Checkstyle)

Google Java stílus (2 szóköz behúzás, Javadoc a publikus típusokon/metódusokon). A Checkstyle a Maven
`validate` fázisában fut és a tesztforrásokat is ellenőrzi; bármely szabálysértés – akár figyelmeztetés
is – megbuktatja a buildet. A hibákat javítani kell, nem elnyomni.

## Lefedettség (JaCoCo)

Az ügynök a tesztek alatt csatlakozik; a `verify` elkészíti a riportot a
`target/site/jacoco/index.html` helyen, és elbukik, ha az összesített sor- (line) vagy elágazás- (branch) lefedettség **80%** alatt van
(`coverage.minimum` property a `pom.xml`-ben).

## CI

A `.github/workflows/ci.yml` a `main`-re irányuló pull requesteken és a `main` push-okon fut: checkout,
Temurin 21 Maven cache-sel, `./mvnw -B verify`, majd felépíti a Docker image-et, a jar-t publikálja az Artifactoryba (csak `main` push-ra, és csak ha
az `ARTIFACTORY_URL` repository változó és az `ARTIFACTORY_USERNAME` / `ARTIFACTORY_TOKEN` secretek
be vannak állítva), majd a JaCoCo riport és a `checkstyle-result.xml`
feltöltése `quality-reports` artifactként. Ugyanarra a ref-re induló párhuzamos futások megszakadnak.

## Docker image

A `Dockerfile` többlépcsős: előbb a függőségek oldódnak fel (rétegcache), majd a
`mvn package -DskipTests` építi a jar-t, ami JRE 21 image-en fut, a 8080-as portot kitéve.

## Közreműködési konvenciók

- A változtatások legyenek minimálisak és illeszkedjenek az [architektúra](architecture.md) rétegeihez.
- A munka befejezése előtt futtasd a `./mvnw -B verify` parancsot.
- Minden munkához új branch; titkot ne commitolj.
