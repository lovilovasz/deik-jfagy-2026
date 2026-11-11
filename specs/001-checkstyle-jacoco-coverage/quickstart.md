# Quickstart: Validating Style and Coverage Gates

Prerequisites: Java 21; no Docker needed (tests use in-memory H2).

## 1. Full build passes
```bash
./mvnw -B verify
```
Expected: Checkstyle reports 0 violations, tests pass, JaCoCo check passes (coverage >= 80%), `BUILD SUCCESS`.

## 2. Style check alone
```bash
./mvnw -B checkstyle:check
```
Expected: same style result without running tests.

## 3. View the coverage report
Open `target/site/jacoco/index.html` after step 1. Expected: per-package and per-class line and branch coverage; overall line coverage >= 80%.

## 4. Style gate fails on a violation (then revert)
Change indentation of any line in a main or test source file, then run `./mvnw -B verify`.
Expected: `BUILD FAILURE` naming file, line and rule. Revert the change.

## 5. Coverage gate fails below 80% (then revert)
Temporarily disable several tests (for example add `@Disabled`) and run `./mvnw -B verify`.
Expected: `BUILD FAILURE` from the JaCoCo check stating required vs. actual coverage. Revert.

## 6. CI parity
Open a pull request; the CI workflow runs the same `verify` and uploads the Checkstyle and JaCoCo reports as artifacts.
