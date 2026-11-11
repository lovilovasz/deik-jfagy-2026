# Data Model

This feature changes build configuration only. There are no new or modified persistent entities, DTOs or endpoints; the existing `Person` and `Todo` model is untouched.

Configuration concepts (not stored data):

| Concept | Value | Where defined |
|---------|-------|---------------|
| Style rule set | Google Java style (`google_checks.xml`) | `pom.xml` Checkstyle plugin |
| Failing severity | warning and above | `pom.xml` |
| Coverage metric / minimum | Line coverage, 0.80, bundle level | `pom.xml` JaCoCo `check` rule |
| Exclusions | Minimal, justified in config comments | `pom.xml` / optional suppressions file |
| Reports | `target/checkstyle-result.xml`, `target/site/jacoco/index.html` | Build output |
