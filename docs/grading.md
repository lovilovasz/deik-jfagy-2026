# Osztályzási rubrika – évvégi beadandó

Oktatói segédlet az évvégi beadandó értékeléséhez, a kurzus diái (Osztályozás, Számonkérés formája)
alapján. Minden jegy **kumulatív**: a magasabb jegyhez az összes alacsonyabb szint követelménye is
teljesül.

Jelölés: `____` = az oktató által még kitöltendő döntés / dátum (lásd a "Nyitott döntések" részt).

## Határidők és védés

| Védés napja | Utolsó commit / repository link határideje |
| --- | --- |
| **2026. december 2.** (szerda), opcionális | **2026. november 30.**, hétfő 23:59:59 |
| **2026. december 9.** (szerda), utolsó védési nap | **2026. december 7.**, hétfő 23:59:59 |

- A védés legkésőbb **december 9-én** zajlik, ez az utolsó nap.
- Aki szeretné, védhet már **december 2-án** is; rá is ugyanez a szabály vonatkozik: az utolsó
  commitnak a védést megelőző hétfő éjfélig be kell érkeznie.
- A szükséges adatokat (név, repository link, választott védési nap) a közös
  [Google Sheets táblázatban](https://docs.google.com/spreadsheets/d/1Oz4OuvO_KP5pZNG96tVf6qWYZiceicKYGrlurArbJMk/edit?usp=sharing)
  kell kitölteni.

## Számonkérés formája

| Elem | Leírás |
| --- | --- |
| Évvégi beadandó | Önálló megoldás; webes alkalmazás és a hozzá felállított fejlesztői környezet |
| Téma | A hallgató találja ki, az oktatóval **emailben** egyeztetni és elfogadtatni kell |
| Védés | A plagizálás kiszűrése a célja: a hallgató ismeri a saját kódját, meg tudja indokolni a megoldásokat |
| Plágium | Automatikusan elégtelen |
| Gyenge védés | Ha plágium nem állapítható meg, de a kód az elvártnál gyengébb, vagy a hallgató sorozatosan nem tudja megmutatni az órákon tanult gyakorlatokat, a védés megismételhető és a feladat javítható |

## Követelmények jegyenként

> **Frontend nem követelmény.** A beadandó lehet csak backend (REST API); frontend opcionális
> pluszként elkészíthető, de egyik jegyhez sem szükséges, és hiánya nem von le pontot.

### Kettes

- [ ] Önálló munka; a hallgató érti és el tudja magyarázni a működést (védésen igazolja)
- [ ] Védés időpontja: **2026. december 2.** (opcionális, korai védés) vagy **2026. december 9.** (utolsó védési nap)
- [ ] Verziókövetett szoftver, **publikus GitHub repository**
- [ ] Repository link elküldve a határidőig (23:59:59): **december 9-i védés esetén 2026. december 7., hétfő éjfél; december 2-i védés esetén 2026. november 30., hétfő éjfél**
- [ ] A határidő utáni módosításokat nem vesszük figyelembe (ellenőrzés: utolsó commit ideje; az utolsó commit a védést megelőző hétfő éjfélig kell beérkezzen)
- [ ] A kötelező adatok (név, repository link, választott védési nap stb.) kitöltve a közös táblázatban: [Adatlap (Google Sheets)](https://docs.google.com/spreadsheets/d/1Oz4OuvO_KP5pZNG96tVf6qWYZiceicKYGrlurArbJMk/edit?usp=sharing)
- [ ] Spring Boot webes alkalmazás
- [ ] CI pipeline build konfigurálva
- [ ] Az új verzió artifactja elérhető az **Artifactoryban** a CI build után

Sikertelen védés: pótlás a vizsgaidőszak elején; a határidő elmulasztása miatt az év végi jegy
eggyel rosszabb.

### Hármas (+ a kettes összes követelménye)

- [ ] Unit tesztek, **40%-os line & branch** lefedettség
- [ ] JaCoCo bekonfigurálva

### Négyes (+ a hármas összes követelménye)

- [ ] Unit tesztek lefedettsége eléri a **60%-os line & branch** értéket
- [ ] Checkstyle használata

### Ötös (+ a négyes összes követelménye)

- [ ] Unit tesztek lefedettsége eléri a **80%-os line & branch** értéket
- [ ] Docker használata (dockerizáció) **és/vagy** SonarQube bekötése a CI/CD pipeline buildbe

## Gyors ellenőrzés beadásonként

| Mit nézünk | Hol / hogyan |
| --- | --- |
| Publikus repo, verziókövetés | GitHub repo megnyitása kijelentkezve; commit-történet |
| Határidő | Utolsó commit / push ideje a határidő előtt |
| Spring Boot | `pom.xml` (spring-boot-starter-parent / `spring-boot-starter-web*`) |
| CI build | `.github/workflows/*.yml`, zöld futás az Actions fülön |
| Artifactory | A CI workflow publikál (pl. `mvn deploy`), az artifact látszik az Artifactoryban |
| JaCoCo | `jacoco-maven-plugin` a `pom.xml`-ben, riport: `target/site/jacoco/index.html` |
| Lefedettség | A riportban a **Missed Instructions / Branches** oszlopok: line és branch külön-külön el kell érje a küszöböt |
| Checkstyle | `maven-checkstyle-plugin` a `pom.xml`-ben, build sikeres |
| Docker / SonarQube | `Dockerfile` / `compose.yaml` és/vagy Sonar lépés a CI-ban |

Helyi ellenőrzés Maven projekteknél: `./mvnw -B verify`, majd a JaCoCo riport megnyitása.

## Nyitott döntések (az oktatóra vár)

1. ~~**Dátumok**~~ – eldöntve, lásd a "Határidők és védés" részt.
2. **"Line & branch" értelmezése:** a küszöböt a line és a branch lefedettségnek *külön-külön* kell
   elérnie (javaslat), vagy összevont érték számít?
3. **Artifactory:** a referencia-megvalósítás GitHub Packagesbe publikál, mert nincs Artifactory
   példány. Elfogadható-e ez a hallgatóknak is, vagy kötelező az Artifactory?
4. **Docker és/vagy SonarQube:** elég az egyik az ötöshöz? (A dia "és / vagy"-t ír.) Kell-e, hogy a
   CI *buildelje* a Docker image-et, vagy elég a Dockerfile megléte?
5. **Mit tekintünk lefedettségi mérésnek:** a hallgató által beadott JaCoCo riport, vagy az oktató
   futtatja újra?
6. **Gyenge védés:** hány ismétlési lehetőség van, és ez hogyan hat a jegyre?

## Példa: a repository saját állapota

A Todo App referencia-megvalósításként használható. Mérés (`./mvnw -B verify`): line 97,3%, branch
100%; a JaCoCo kapu a line **és** a branch lefedettséget is 80%-on tartja.

| Szint | Állapot | Indok |
| --- | --- | --- |
| Kettes | ✅ | Publikus repo, Spring Boot, CI; a CI a jar-t GitHub Packagesbe publikálja (a `main` push-on, a beépített `GITHUB_TOKEN`-nel) |
| Hármas | ✅ | Line és branch ≥ 40%, JaCoCo |
| Négyes | ✅ | Line és branch ≥ 60%, Checkstyle |
| Ötös | ✅ | Line és branch ≥ 80%, Docker image build a CI-ban |

A GitHub Packages publikálás az első `main` push után ellenőrizhető a repo *Packages* oldalán.
