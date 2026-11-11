# Research: Checkstyle and JaCoCo Setup

## R1: Which Google rule set and how to reference it
- **Decision**: Use `google_checks.xml` bundled inside the Checkstyle jar via `maven-checkstyle-plugin` `configLocation`.
- **Rationale**: It is the canonical Google config; no copy to drift. Matches "use Google config".
- **Alternatives**: Copy the XML into `config/checkstyle/` (editable but drifts); Sun checks (not requested).

## R2: Failing severity
- **Decision**: `violationSeverity=warning`, `failOnViolation=true`, `consoleOutput=true`.
- **Rationale**: Google checks set severity to `warning` by default; the plugin default (`error`) would let every violation pass silently, which the spec forbids (edge case on severity).
- **Alternatives**: Override severity property in config (more invasive).

## R3: Plugin versions
- **Decision**: Pin a recent `maven-checkstyle-plugin` (3.x) and a matching modern Checkstyle (10.x or later) via a `<dependency>` on the plugin; use the `jacoco-maven-plugin` version managed by the Spring Boot parent if present, otherwise pin 0.8.13 or later (Java 21 support required).
- **Rationale**: Java 21 syntax (records) must be parsed; old Checkstyle versions fail on records.
- **To confirm at implementation**: exact latest versions, and whether the Boot parent manages JaCoCo.

## R4: Fixing existing violations
- **Decision**: Run google-java-format once (standalone jar, not a project dependency) to convert indentation to 2 spaces and fix import order/line wrapping, then fix remaining findings (Javadoc, naming, abbreviations) by hand and re-run Checkstyle until clean.
- **Rationale**: Existing code uses 4-space indentation, so nearly every line would be flagged; automated reformat is behavior-preserving and reviewable.
- **Alternatives**: Suppress Indentation (defeats the Google config); fully manual (error-prone).

## R5: Lombok and annotations
- **Decision**: No suppressions initially. Add narrowly scoped suppressions only for demonstrated false positives (for example Javadoc on Lombok-annotated classes).
- **Rationale**: Checkstyle analyzes source, not generated code, so Lombok is mostly safe.

## R6: Coverage threshold scope
- **Decision**: Enforce 0.80 on LINE COVEREDRATIO for the whole bundle; report branch coverage without enforcing.
- **Rationale**: Matches the spec assumption. Tests run on H2, so Surefire plus the JaCoCo agent works with no extra infrastructure.
- **Alternatives**: Per-class thresholds (too strict for entities/config); branch coverage gate (not requested).

## R7: CI
- **Decision**: Keep the existing `./mvnw -B verify` step (it now enforces both checks) and add an artifact upload of `target/site/jacoco` and the Checkstyle report with `if: always()`.
- **Rationale**: Same rules locally and in CI; reports help diagnose failures.
