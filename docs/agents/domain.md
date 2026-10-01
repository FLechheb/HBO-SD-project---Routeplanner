# Domain Docs

This repo uses a single-context layout:

- `GLOSSARY.md` at the repo root defines domain vocabulary.
- `docs/adr/` holds architecture decision records.

## Before exploring

Read the root glossary and ADRs relevant to the area being explored.

If these files or directories do not exist, proceed silently.
The `/domain-modeling` skill creates them lazily when terms or decisions
are resolved; setup does not create placeholder domain documents.

## Use the glossary's vocabulary

Use defined domain terms in issue titles, proposals, hypotheses, and
test names. Respect synonyms the glossary explicitly avoids.

If a needed concept is absent, reconsider whether it belongs to the
domain or note the gap for `/domain-modeling`.

## Flag ADR conflicts

Explicitly identify any proposal that contradicts an existing ADR,
citing the ADR and explaining why the decision is worth reopening.
