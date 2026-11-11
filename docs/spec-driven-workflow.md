# Spec-driven workflow

The project uses [GitHub Spec Kit](https://github.com/github/spec-kit) with Claude Code. Skills live
in `.claude/skills/speckit-*`, templates and scripts in `.specify/` (do not edit `.specify/`
internals by hand). Specs are written to `specs/`.

## Steps for non-trivial features

Run in order:

1. `/speckit-constitution` – once, defines project principles
2. `/speckit-specify` – write the feature spec from a description
3. `/speckit-clarify` – resolve underspecified areas (up to 5 questions)
4. `/speckit-plan` – technical plan and design artifacts
5. `/speckit-tasks` – dependency-ordered `tasks.md`
6. `/speckit-implement` – execute the tasks

Optional: `/speckit-analyze` (cross-artifact consistency check) and `/speckit-checklist`.

## Existing specs

`specs/001-checkstyle-jacoco-coverage/` documents the addition of Checkstyle and the JaCoCo coverage
gate:

| File | Contents |
| --- | --- |
| `spec.md` | Requirements |
| `plan.md`, `research.md` | Approach and tool decisions |
| `data-model.md`, `quickstart.md` | Supporting design and how to verify |
| `tasks.md` | Implementation task list |
| `checklists/requirements.md` | Spec quality checklist |
