# Feature Specification: Code Style Checks and Test Coverage Gate

**Feature Branch**: `chore/checkstyle-jacoco`

**Created**: 2026-11-11

**Status**: Draft

**Input**: User description: "I want you to setup checkstyle for static code analysis, and jacoco for test coverage. I want at least 80% coverage. For checkstyle use the Google config, also fix issues after setup."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Automated code style enforcement (Priority: P1)

As a developer, I want the project's standard build to check all source code against the Google Java style rules (Checkstyle), so that style problems are caught automatically and the codebase stays consistent.

**Why this priority**: Style analysis is the first half of the request and is only useful if the existing code already complies; it unblocks a clean baseline.

**Independent Test**: Run the standard build on a clean checkout and confirm the style check runs and reports zero violations; then introduce a deliberate violation and confirm the build reports it.

**Acceptance Scenarios**:

1. **Given** the existing code base, **When** the standard build is run, **Then** the style check runs against main and test sources and finishes with zero violations.
2. **Given** a source file with a deliberate style violation (e.g., wrong indentation), **When** the standard build is run, **Then** the build fails and names the file, line and violated rule.
3. **Given** a developer wants to check style only, **When** they run the style check on its own, **Then** they get the same results without running the full test suite.

---

### User Story 2 - Existing code fixed to comply (Priority: P1)

As a maintainer, I want all pre-existing style violations fixed as part of this work, so that turning the check on does not leave the build red.

**Why this priority**: The user explicitly asked for issues to be fixed after setup; enforcement without a clean baseline would break every build.

**Independent Test**: Run the style check on the repository and confirm zero violations, and confirm the full test suite still passes with unchanged behavior.

**Acceptance Scenarios**:

1. **Given** the style check is enabled, **When** it is run on the current code base, **Then** no violations remain.
2. **Given** the formatting fixes are applied, **When** the full test suite runs, **Then** all tests pass and no API behavior changed.

---

### User Story 3 - Test coverage measurement and 80% gate (Priority: P1)

As a developer, I want the build to measure test coverage and fail when it falls below 80%, so that coverage cannot silently degrade.

**Why this priority**: Coverage measurement and the 80% minimum are the second half of the request.

**Independent Test**: Run the standard build and confirm a coverage report is produced and the build passes at or above 80%; temporarily remove tests and confirm the build fails below 80%.

**Acceptance Scenarios**:

1. **Given** the current tests, **When** the standard build is run, **Then** a coverage report is generated and overall coverage is at least 80%.
2. **Given** coverage drops below 80%, **When** the standard build is run, **Then** the build fails with a message stating the required and actual coverage.
3. **Given** coverage is below 80% for the current code, **When** this feature is completed, **Then** additional tests are added so the threshold is met.

---

### User Story 4 - Readable reports (Priority: P2)

As a developer or reviewer, I want human-readable style and coverage reports after a build, so that I can see exactly what to fix or test.

**Why this priority**: Helpful for acting on failures but not required for enforcement.

**Independent Test**: After a build, open the generated reports and find per-file coverage and any style findings.

**Acceptance Scenarios**:

1. **Given** a completed build, **When** the developer opens the coverage report, **Then** per-package and per-class coverage is visible.
2. **Given** the CI pipeline runs the build, **When** it finishes, **Then** the same checks and thresholds apply as locally.

---

### Edge Cases

- Generated or non-logic code (application entry point, configuration classes, data transfer objects) could distort the coverage figure; the rules for what counts must be explicit.
- Style rules that conflict with Lombok-generated code or framework annotations must not cause false failures.
- Coverage is measured on the test run that already uses the in-memory test database, so no extra infrastructure is needed.
- A style warning (as opposed to error) must not silently pass; the severity that fails the build must be defined.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The standard build MUST run a static style analysis using the Google Java style rule set against both main and test sources.
- **FR-002**: The build MUST fail when any style violation of the configured failing severity is found, reporting file, line and rule.
- **FR-003**: All style violations present in the current code base MUST be fixed so the check passes with zero violations.
- **FR-004**: Fixing style violations MUST NOT change application behavior; the full existing test suite MUST still pass.
- **FR-005**: The standard build MUST measure test coverage during the test run and produce a human-readable report.
- **FR-006**: The build MUST fail when overall line coverage is below 80%.
- **FR-007**: If current coverage is below 80%, new tests MUST be added, following the project's existing test conventions, until the threshold is met.
- **FR-008**: Style and coverage checks MUST run in the continuous integration pipeline with the same rules as locally.
- **FR-009**: The project documentation MUST describe how to run the style check and view the coverage report.
- **FR-010**: Any exclusions from style or coverage checks MUST be explicit, minimal and justified in the configuration.

### Key Entities

- **Style rule set**: The Google Java style conventions, with their severity levels, applied to all Java sources.
- **Coverage threshold**: The minimum overall coverage (80%) that the build enforces.
- **Reports**: The style findings and coverage summaries produced by each build.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Running the standard build on a fresh checkout yields zero style violations.
- **SC-002**: Overall test coverage is at least 80% and is shown in the generated report.
- **SC-003**: A deliberately introduced style violation or a coverage drop below 80% causes the build to fail 100% of the time.
- **SC-004**: All pre-existing tests pass unchanged after the fixes.
- **SC-005**: A developer can find how to run both checks and open the reports from the project documentation in under 2 minutes.
- **SC-006**: Pull request builds in CI enforce the same checks, so no change can merge with violations or sub-80% coverage.

## Assumptions

- "Google config" means the publicly available Google Java style rule set shipped with the style tool; it is used as-is apart from minimal, justified suppressions (e.g., for generated code).
- The 80% threshold applies to overall line coverage of production code; branch coverage is reported but not enforced.
- Trivial or generated code (application entry point, framework configuration) may be excluded if justified, but exclusions are kept minimal.
- Style violations fail the build from the start, since existing violations are fixed in this same change.
- The existing CI workflow is extended rather than replaced.
- No change to application features, endpoints, or data model is in scope.
