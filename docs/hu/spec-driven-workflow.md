# Specifikáció-vezérelt munkafolyamat

[English version](../spec-driven-workflow.md)

A projekt a [GitHub Spec Kit](https://github.com/github/spec-kit)-et használja Claude Code-dal. A
skillek a `.claude/skills/speckit-*` alatt, a sablonok és szkriptek a `.specify/` alatt vannak (a
`.specify/` belsejét ne szerkeszd kézzel). A specifikációk a `specs/` mappába kerülnek.

## Lépések nem triviális funkciókhoz

Sorrendben futtasd:

1. `/speckit-constitution` – egyszer, a projekt alapelvei
2. `/speckit-specify` – funkció-specifikáció írása leírásból
3. `/speckit-clarify` – hiányosan meghatározott részek tisztázása (legfeljebb 5 kérdés)
4. `/speckit-plan` – technikai terv és tervezési artifactok
5. `/speckit-tasks` – függőségek szerint rendezett `tasks.md`
6. `/speckit-implement` – a feladatok végrehajtása

Opcionális: `/speckit-analyze` (artifactok közötti konzisztenciaellenőrzés) és `/speckit-checklist`.

## Meglévő specifikációk

A `specs/001-checkstyle-jacoco-coverage/` a Checkstyle és a JaCoCo lefedettségi kapu bevezetését
dokumentálja:

| Fájl | Tartalom |
| --- | --- |
| `spec.md` | Követelmények |
| `plan.md`, `research.md` | Megközelítés és eszközválasztások |
| `data-model.md`, `quickstart.md` | Kiegészítő terv és az ellenőrzés módja |
| `tasks.md` | Megvalósítási feladatlista |
| `checklists/requirements.md` | Specifikáció-minőségi ellenőrzőlista |
