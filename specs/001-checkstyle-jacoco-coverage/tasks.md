# Tasks: Code Style Checks and Test Coverage Gate

**Input**: Design documents from `/specs/001-checkstyle-jacoco-coverage/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, quickstart.md
**Tests**: No dedicated test tasks were requested. The only new tests are the coverage top-up tasks (T015-T016), required by FR-007 if coverage is under 80%.

## Format: `[ID] [P?] [Story] Description`
- **[P]**: Can run in parallel (different files, no dependencies on incomplete tasks)
- **[Story]**: US1 = style enforcement, US2 = fix existing code, US3 = coverage gate, US4 = reports and CI

All paths are relative to the repository root. Build command: `./mvnw -B verify`.

---

## Phase 1: Setup

- [X] T001 Run `./mvnw -B verify` on the current branch and record the baseline (tests pass, no coverage or style data yet) in a scratch note; confirm Java 21 and the Maven wrapper work
- [X] T002 Determine latest stable `maven-checkstyle-plugin`, Checkstyle (must parse Java 21 records), and `jacoco-maven-plugin` versions; check whether the Spring Boot 4.1.2 parent in `pom.xml` already manages JaCoCo (`jacoco.version`)

---

## Phase 2: Foundational

- [X] T003 Add version properties (`checkstyle.plugin.version`, `checkstyle.version`, and `jacoco.version` only if not parent-managed) to the `<properties>` block in `pom.xml`

**Checkpoint**: Versions decided; stories can begin.

---

## Phase 3: User Story 1 - Automated style enforcement (Priority: P1)

**Goal**: `./mvnw verify` runs Checkstyle with Google rules on main and test sources and fails on violations.
**Independent Test**: `./mvnw -B checkstyle:check` runs and reports findings; a deliberate violation produces a failure naming file, line and rule.

- [X] T004 [US1] Add `maven-checkstyle-plugin` to `<build><plugins>` in `pom.xml`: Checkstyle dependency at the chosen version, `configLocation=google_checks.xml`, `includeTestSourceDirectory=true`, `violationSeverity=warning`, `failOnViolation=true`, `consoleOutput=true`, execution of goal `check` bound to the `validate` phase
- [X] T005 [US1] Run `./mvnw -B checkstyle:check` and save the violation summary (counts per rule) to `specs/001-checkstyle-jacoco-coverage/checkstyle-baseline.txt` to drive US2

**Checkpoint**: Style check runs and fails on the unfixed code (expected until US2 is done).

---

## Phase 4: User Story 2 - Existing code fixed to comply (Priority: P1)

**Goal**: Zero violations without behavior change.
**Independent Test**: `./mvnw -B checkstyle:check` reports 0 violations and `./mvnw -B test` still passes.
**Depends on**: US1.

- [X] T006 [US2] Reformat all Java files under `src/main/java` and `src/test/java` with a standalone google-java-format jar (one-off, not a project dependency) to get 2-space indentation, import order and line wrapping
- [X] T007 [P] [US2] Fix remaining findings (Javadoc, naming, abbreviations) in `src/main/java/com/jfagy/todo/app/controller/PersonController.java` and `src/main/java/com/jfagy/todo/app/controller/TodoController.java`
- [X] T008 [P] [US2] Fix remaining findings in `src/main/java/com/jfagy/todo/app/service/PersonService.java` and `src/main/java/com/jfagy/todo/app/service/TodoService.java`
- [X] T009 [P] [US2] Fix remaining findings in `src/main/java/com/jfagy/todo/app/entity/Person.java`, `src/main/java/com/jfagy/todo/app/entity/Todo.java`, and `src/main/java/com/jfagy/todo/app/repository/`
- [X] T010 [P] [US2] Fix remaining findings in `src/main/java/com/jfagy/todo/app/dto/`, `src/main/java/com/jfagy/todo/app/exception/`, `src/main/java/com/jfagy/todo/app/config/OpenApiConfig.java`, and `src/main/java/com/jfagy/todo/app/Application.java`
- [X] T011 [P] [US2] Fix remaining findings in `src/test/java/com/jfagy/todo/app/ApplicationTests.java`, `OpenApiSpecTests.java`, and `TodoApiTests.java`
- [X] T012 [US2] Add `config/checkstyle/checkstyle-suppressions.xml` and wire it into the plugin in `pom.xml` ONLY for justified false positives (for example Lombok), each with a comment; skip this task if none are needed (not needed)
- [X] T013 [US2] Run `./mvnw -B checkstyle:check` until 0 violations, then `./mvnw -B test` to confirm all tests pass unchanged (FR-003, FR-004)

**Checkpoint**: Style gate is green; US1 acceptance scenarios 1 and 2 can now be demonstrated.

---

## Phase 5: User Story 3 - Coverage measurement and 80% gate (Priority: P1)

**Goal**: Build measures coverage and fails below 80% line coverage.
**Independent Test**: `./mvnw -B verify` passes at >= 80%; disabling tests makes it fail with required vs. actual.
**Depends on**: Foundational only (independent of US1/US2, but final verify needs US2 green).

- [X] T014 [US3] Add `jacoco-maven-plugin` to `pom.xml` with executions: `prepare-agent` (default phase), `report` at `verify`, and `check` at `verify` with a BUNDLE rule on LINE COVEREDRATIO minimum `0.80`; add comment-justified excludes only if needed
- [X] T015 [US3] Run `./mvnw -B verify`, read `target/site/jacoco/index.html` (or `target/jacoco.csv`) and list classes below target
- [X] T016 [US3] If coverage is under 80%, add tests following `src/test/java/com/jfagy/todo/app/TodoApiTests.java` (MockMvc) for uncovered paths in services, controllers and `GlobalExceptionHandler` (for example 404 and 409 branches) in new or existing test classes under `src/test/java/com/jfagy/todo/app/`; prefer tests over exclusions; new tests must also be Checkstyle-clean (not needed)
- [X] T017 [US3] Re-run `./mvnw -B verify` and confirm `BUILD SUCCESS` with overall line coverage >= 80%

**Checkpoint**: Both gates pass in one build.

---

## Phase 6: User Story 4 - Readable reports and CI parity (Priority: P2)

**Goal**: Reports are viewable locally and in CI with identical rules.
**Independent Test**: After a build, per-class coverage and style output are visible; a PR run uploads reports.

- [X] T018 [P] [US4] Update `.github/workflows/ci.yml`: keep the `./mvnw -B verify` step and add an `actions/upload-artifact` step with `if: always()` for `target/site/jacoco/` and `target/checkstyle-result.xml`
- [X] T019 [P] [US4] Update `CLAUDE.md` Commands section: how to run `./mvnw -B checkstyle:check`, the 80% line-coverage gate, report location `target/site/jacoco/index.html`, and a note that style follows Google Java style (2-space indent)

---

## Phase 7: Polish and Verification

- [X] T020 Run the quickstart validation in `specs/001-checkstyle-jacoco-coverage/quickstart.md` steps 1-5 (including the temporary deliberate violation and the temporary disabled tests), then revert those temporary changes
- [X] T021 Confirm `git status` shows only intended changes (`pom.xml`, Java formatting, new tests, CI, CLAUDE.md, specs); delete `checkstyle-baseline.txt` if not wanted; do not commit unless asked

---

## Dependencies and Execution Order

- Phase 1 -> Phase 2 -> user stories -> Phase 7.
- US1 -> US2 (fixing needs the plugin results). US3 can be done in parallel with US1/US2 since it only edits the JaCoCo part of `pom.xml` and adds tests, but T016 tests must pass Checkstyle, so finish them after T013 if possible.
- US4 needs US1 and US3 (reports must exist).
- `pom.xml` is touched by T003, T004, T012, T014, so those are sequential.

## Parallel Opportunities

- T007-T011 (different files per package) after T006.
- T018 and T019 together.

## Implementation Strategy

- **MVP**: Phases 1-4 (US1 + US2): a clean, enforced style gate.
- Then US3 (coverage gate), then US4 (CI reports and docs).
- Keep the build green at each commit point: enable Checkstyle failure only together with the US2 fixes (or land T004 and T006-T013 in one commit).
