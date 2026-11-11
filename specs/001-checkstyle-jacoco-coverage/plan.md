# Implementation Plan: Code Style Checks and Test Coverage Gate

**Branch**: `chore/checkstyle-jacoco` | **Date**: 2026-11-11 | **Spec**: [spec.md](spec.md)

**Input**: Feature specification from `/specs/001-checkstyle-jacoco-coverage/spec.md`

## Summary

Add Checkstyle (bundled Google Java style rules) and JaCoCo to the Maven build so that `./mvnw -B verify` fails on any style violation or if overall line coverage is below 80%. Fix all existing violations (behavior-preserving formatting/Javadoc changes), add tests if coverage is short, extend the existing GitHub Actions workflow to publish reports, and document the commands in CLAUDE.md. No application code or API changes.

## Technical Context

**Language/Version**: Java 21

**Primary Dependencies**: Spring Boot 4.1.2 (Maven), Lombok; new build plugins: `maven-checkstyle-plugin` + Checkstyle, `jacoco-maven-plugin`

**Storage**: N/A (tests use in-memory H2)

**Testing**: JUnit 5 + MockMvc (`TodoApiTests` is the template), run by Surefire

**Target Platform**: Developer machines (Windows/Linux) and GitHub Actions `ubuntu-latest`

**Project Type**: web-service (single Maven module)

**Performance Goals**: N/A; checks should add only seconds to the build

**Constraints**: Must work offline-free from a fresh checkout via the Maven wrapper; no new infrastructure; 80% line coverage; zero Checkstyle violations

**Scale/Scope**: 18 main and 3 test Java files

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

`.specify/memory/constitution.md` is still the unfilled template, so it imposes no gates. Project rules from CLAUDE.md are applied instead:

- Minimal changes consistent with layering: PASS (build config, formatting, tests only).
- Run `./mvnw -B verify` before declaring done: PASS (it becomes the enforcement point).
- Test added for every new endpoint: N/A (no new endpoints).
- No secrets, no commits unless asked: PASS.

Post-design re-check: still PASS.

## Project Structure

### Documentation (this feature)

```text
specs/001-checkstyle-jacoco-coverage/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/           # not applicable, no external interface changes
└── tasks.md             # created by /speckit-tasks
```

### Source Code (repository root)

```text
pom.xml                              # add checkstyle + jacoco plugins, version properties
config/checkstyle/                   # only if a local suppressions file is needed
└── checkstyle-suppressions.xml
.github/workflows/ci.yml             # upload reports as artifacts
CLAUDE.md                            # document commands and thresholds
src/main/java/com/jfagy/todo/app/    # formatting/Javadoc fixes only
src/test/java/com/jfagy/todo/app/    # formatting fixes; extra tests if coverage < 80%
```

**Structure Decision**: Single Maven module, configuration lives in `pom.xml`. The Google rule set is referenced from the Checkstyle distribution (`google_checks.xml`) rather than copied, so there is no local rule file to maintain; a suppressions file is added only if a justified exclusion is needed.

## Design Decisions (see research.md)

1. Checkstyle runs at the `validate` phase bound to the `check` goal, with `violationSeverity=warning` because Google's rule set reports at warning level, and `includeTestSourceDirectory=true`.
2. JaCoCo: `prepare-agent` before tests, `report` at `verify`, and `check` with a LINE COVEREDRATIO minimum of 0.80 at bundle level.
3. Coverage exclusions limited to the `Application` main class and `config` package, if needed to meet the gate honestly; prefer adding tests over exclusions.
4. Existing code is 4-space indented; Google style requires 2 spaces, so the fix is a one-off reformat with google-java-format (used outside the build, not added as a dependency), followed by manual fixes for Javadoc, import order and naming findings.

## Complexity Tracking

No constitution violations; nothing to justify.
