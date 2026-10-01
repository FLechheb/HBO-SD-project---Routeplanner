# Issue tracker: Local Markdown

Issues and specs live as Markdown files in `.scratch/`.

## Conventions

- One feature per directory: `.scratch/<feature-slug>/`.
- Specs use `.scratch/<feature-slug>/spec.md`.
- Implementation tickets use `.scratch/<feature-slug>/issues/<NN>-<slug>.md`,
  numbered from `01`, with one file per ticket.
- Record triage state in a `Status:` line near the top of each ticket.
  Use the role strings in `docs/agents/triage-labels.md`.
- Append comments and conversation history under `## Comments`.

## Publishing and fetching

When a skill says "publish to the issue tracker", create the appropriate
spec or ticket file using the paths above. Create directories as needed.

When a skill says "fetch the relevant ticket", read the referenced file.
Resolve a bare issue number within the current feature; ask for the
feature or path if it is ambiguous.

## Wayfinding operations

Used by `/wayfinder`.

- Map: `.scratch/<effort>/map.md`, containing Notes, Decisions-so-far,
  and Fog.
- Child tickets: `.scratch/<effort>/issues/<NN>-<slug>.md`, numbered
  from `01`, with the question in the body.
- Type: record `research`, `prototype`, `grilling`, or `task` in a
  `Type:` line.
- Lifecycle: wayfinder uses `Status: claimed` and `Status: resolved`
  for execution state, distinct from the triage roles.
- Blocking: record `Blocked by: NN, NN` near the top. A ticket is
  unblocked when every listed ticket is resolved.
- Frontier: select the first ticket by number that is open, unblocked,
  and unclaimed.
- Claim: set `Status: claimed` and save before starting work.
- Resolve: append the answer under `## Answer`, set `Status: resolved`,
  then append a brief summary and link to the map's Decisions-so-far.
