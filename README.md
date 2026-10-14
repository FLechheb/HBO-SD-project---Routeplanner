# Reisplanner

Maven multi-module project containing:

- `frontend`: React, TypeScript, and Vite
- `backend`: Spring Boot and Maven

## Requirements

- Java 25
- Node.js and npm
- Docker for the backend's PostgreSQL Compose service

## Build and test

Run the complete reactor build from the repository root:

```shell
./mvnw clean verify
```

Maven runs `npm ci` and `npm run build` for the frontend before building and
testing the backend.

Run only the backend:

```shell
./mvnw -pl backend spring-boot:run
```

Run only the frontend during development:

```shell
cd frontend
npm run dev
```

## Accessibility needs

`GET /api/needs` returns the selectable requirement catalogue, ordered by type:

```json
[
  {"type": "AUDITORY_INFORMATION_REQUIRED", "description": "Hoorbare informatie nodig"},
  {"type": "ELEVATOR_REQUIRED", "description": "Lift nodig"},
  {"type": "EXTRA_TRANSFER_TIME", "description": "Extra overstaptijd"},
  {"type": "GUIDING_LINE_REQUIRED", "description": "Geleidelijn nodig"},
  {"type": "LEVEL_ACCESS_REQUIRED", "description": "Trapvrije toegang nodig"},
  {"type": "SEATING_REQUIRED", "description": "Zitplaats nodig"},
  {"type": "VISUAL_INFORMATION_REQUIRED", "description": "Visuele informatie nodig"},
  {"type": "WHEELCHAIR_ACCESSIBLE", "description": "Rolstoeltoegankelijk"}
]
```

These are independent requirements, not disability presets. This intentionally
replaces the previous disability codes and boolean DTO fields. Flyway migration
V7 upgrades the catalogue; existing migrations remain unchanged.

The backend evaluates facility requirements polymorphically through
`FacilityNeed.evaluate`, with `AccessibilityService.evaluate` combining selected
needs. Every applicable requirement must pass. Outcomes are `SATISFIED`,
`UNSATISFIED`, or `NOT_APPLICABLE`; empty and timing-only selections are not
applicable. Movement requirements do not constrain information facilities, and
information requirements do not constrain movement facilities.

An elevator requirement specifically requires an elevator; step-free access
may instead be provided by a ramp. Seating is a per-movement-facility requirement,
so a normal elevator without seating fails it. These checks do not establish
whole-station or route accessibility.

`EXTRA_TRANSFER_TIME` is catalogue-only in the backend. Profiles and chosen
minutes remain frontend concerns, and needs are not applied to OTP planning.
Frontend integration is tracked in
[the profile follow-up ticket](.scratch/needs-rework/issues/01-api-backed-needs-profile.md).
