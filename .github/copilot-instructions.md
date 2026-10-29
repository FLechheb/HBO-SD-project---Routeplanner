# Repository guidance

## Existing agent instructions

Paths below are relative to the repository root.

- Follow root `AGENTS.md`. Before exploring domain behavior, read
  `docs/agents/domain.md`, then root `GLOSSARY.md` and relevant `docs/adr/`
  records if present.
- Before creating, reading, or updating issues/specs, read
  `docs/agents/issue-tracker.md`: this repository uses local Markdown under
  `.scratch/<feature>/`, including when a skill says to publish or fetch a ticket.
- Before assigning triage status, read `docs/agents/triage-labels.md`.

## Build, run, test, and lint

Commands below run from the repository root unless explicitly using `cd`.
Prerequisites are Java 25, Node.js/npm, and Docker Compose for backend services.

| Task | Command |
| --- | --- |
| Complete reactor build and frontend/backend tests | `./mvnw clean verify` |
| Backend tests only | `./mvnw -pl backend test` |
| One backend test class | `./mvnw -pl backend -Dtest=JourneyServiceTest test` |
| One backend test method | `./mvnw -pl backend '-Dtest=JourneyServiceTest#rejectsSameOriginAndDestination' test` |
| Backend development server | `./mvnw -pl backend spring-boot:run` |
| Frontend dependency installation | `cd frontend && npm ci` |
| Frontend development server | `cd frontend && npm run dev` |
| Frontend tests, single run | `cd frontend && npm test -- --run` |
| One frontend test file | `cd frontend && npm test -- --run src/services/journeyResponse.test.ts` |
| One frontend test case | `cd frontend && npm test -- --run src/services/journeyResponse.test.ts -t "sends extra transfer minutes when planning a journey"` |
| Frontend type-check and production build | `cd frontend && npm run build` |
| Lint actual frontend sources | `cd frontend && npx --no-install eslint src` |

The root Maven reactor builds `frontend` before `backend`. The frontend module
runs `npm ci` at `generate-resources`, then `npm test -- --run` and
`npm run build` at `prepare-package`; its output remains in `frontend/dist`.
Backend-only commands avoid those npm steps. The lint script currently targets the missing
`ov-reisplanner-frontend-main` directory; use the source command above to lint
the current layout.

Frontend tests use Vitest and React Testing Library. Tests requiring DOM or
localStorage use the per-file `// @vitest-environment jsdom` directive.
Use `--run` for bounded test execution; the effective `npm test` script starts
Vitest in watch mode during interactive development.

Backend tests use JUnit Jupiter and Mockito; integration tests use Spring Boot
with MockMvc or RestAssured. `OtpClientTest` uses a local JDK HTTP server rather
than a running OTP instance. Spring-context tests differ in whether they disable
Docker Compose; check their annotations before assuming infrastructure is needed.
`NeedRepositoryIntegrationTest` demonstrates isolated H2/Flyway testing with
`spring.datasource.generate-unique-name=true` and
`spring.docker.compose.enabled=false`.

## Runtime configuration

- The backend listens on **8081** (`backend/src/main/resources/application.properties`).
  Set `VITE_API_URL=http://localhost:8081` without a trailing slash in
  `frontend/.env`; frontend services append `/api/...`. The frontend README's
  example uses 8080, so follow the backend configuration for the actual port.
- Spring Boot's Compose integration uses `backend/compose.yaml` and starts
  PostgreSQL on 5432. OpenTripPlanner (OTP) is optional, behind the `otp` Compose
  profile, and marked `org.springframework.boot.ignore=true`; Spring Boot
  neither starts it nor waits for its readiness.
  Start it separately with
  `docker compose -f backend/compose.yaml --profile otp up -d otp`.
  OTP exposes port 8086, starts with `--load --serve`, and mounts
  `backend/otp-data`, which must contain a prebuilt graph. `OTP_BASE_URL`
  overrides the backend's default `http://localhost:8086`. Station search and
  live planning require OTP; other endpoints remain usable without it.

## Architecture

Reisplanner is a Dutch public-transport accessibility planner with a React /
TypeScript / Vite frontend and a Spring Boot MVC backend.

- `frontend/src/main.tsx` mounts `App.tsx`. `App` owns planner, profile, and results
  page state using React state rather than a routing library. It coordinates
  station selection, request status, profile persistence, and journey results.
  Successful journeys are cached locally; offline startup and offline planning
  can display the last saved journey.
- The live planning path is `StationPicker` / `App` -> frontend services ->
  `RouteController` -> `StationService` / `JourneyService` -> `OtpClient`.
  `GET /api/routes/stations?name=...` searches stations;
  `POST /api/routes/plan` plans between station IDs. `OtpClient` sends GraphQL
  requests to `/otp/gtfs/v1`; services translate OTP-specific response records
  under `dto/otp` into the application's public DTOs.
- Stop data has separate paths: `GET /api/stops` uses `StopService` -> `StopDAO`
  -> classpath `stop-navigation.json`, returning domain `Stop` objects.
  `RouteService` instead reads database `StopEntity` records through
  `StopRepository` and produces `RouteStopResponse`. These shapes and data sources
  are distinct from the live OTP journey path.
- Persistence uses Spring Data JDBC, not JPA. `entity` records represent database
  rows; numbered SQL migrations under `backend/src/main/resources/db/migration`
  define and seed stops and special needs. Add new migrations for schema or seed
  changes while preserving existing migrations; V7 replaces disability presets
  with independently selectable requirements.
- Special needs flow through `NeedRepository` -> `NeedsDao` ->
  `SpecialNeedService`. `SpecialNeedFactory` creates one subclass per requirement.
  `SpecialNeed` separates facility and timing needs; `FacilityNeed.evaluate`
  handles role applicability and polymorphic checks. `AccessibilityService.evaluate`
  combines applicable outcomes with AND, using `SATISFIED`, `UNSATISFIED`, and
  `NOT_APPLICABLE`. `GET /api/needs` returns the ordered `{type, description, category}`
  catalogue; `NeedType` owns labels and persisted `Need` rows identify membership.
  `SpecialNeedMapper` is generated by MapStruct during compilation.

## Cross-layer conventions

- Model needs as independent traveller requirements using `GLOSSARY.md`
  terminology. Facility evaluation is role-scoped: movement needs do not
  constrain information facilities and vice versa. Empty or timing-only
  selections evaluate to `NOT_APPLICABLE`. These per-facility checks do not
  establish whole-station or route accessibility. An elevator requirement
  specifically requires an elevator; step-free access can use a ramp and
  does not itself guarantee wheelchair accessibility.
- Keep public Java DTOs and their frontend TypeScript response interfaces aligned.
  Preserve the boundary between OTP GraphQL records, database entities, and public
  API responses. Jackson databind imports use `tools.jackson...` (Jackson 3).
- Station identity is the OTP `gtfsId`, not the display name or the older
  fixture's `stopCode`. `StationPicker` clears the selected station when its text
  changes; route submission requires two distinct selected stations.
- The planning contract carries `originStationId`, `destinationStationId`,
  required `extraTransferMinutes` (0, 5, 10, or 15), and `selectedFacilities`.
  `App` sends the last saved profile's transfer time and facility-need identifiers.
  An omitted facility list defaults to empty for older clients; explicit null,
  unknown identifiers, and timing needs are rejected with HTTP 400. Validation
  uses the available facility-need catalogue. `OtpClient` maps positive
  minutes to an ISO-8601 duration for OTP's transfer `slack`; zero omits the
  variable to retain OTP's default. Only "now" planning is supported.
  Facility needs are submitted and validated but do not filter live journeys
  or establish their accessibility. Offline planning reuses the saved journey
  without reevaluating it against the current profile.
- `ProfilePage` fetches `/api/needs` on each visit, filters `category: FACILITY`,
  and stores `NeedType` identifiers. `EXTRA_TRANSFER_TIME` identifies the timing
  need in the catalogue; its selected duration travels separately as
  `extraTransferMinutes`.
- Profile, recent-station, and last-journey persistence live in
  `frontend/src/utils/profileStorage.ts` and
  `frontend/src/utils/recentStations.ts`, plus
  `frontend/src/utils/journeyStorage.ts`. Reuse these helpers and their existing
  storage formats; the saved journey includes origin/destination display names
  so offline results can render without selected station objects.
- Journey and station services translate invalid input into HTTP 400, upstream
  failures into 502, and missing journeys into 404 using `ResponseStatusException`.
  Frontend fetch services reject non-OK responses, leaving components to show
  Dutch user-facing errors.
- Journey timestamps include offsets; `RouteResult` renders them with `nl-NL`
  formatting in `Europe/Amsterdam`. Leg times, platform codes, and route/trip
  metadata can be null and have display fallbacks.
