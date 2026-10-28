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

PostgreSQL starts automatically with the backend. OpenTripPlanner (OTP) is
optional and is not started or checked for readiness by Spring Boot.
Start it separately when needed (requires a prebuilt graph in `backend/otp-data`):

```shell
docker compose -f backend/compose.yaml --profile otp up -d otp
```

An OTP startup failure does not block the normal backend startup. Without OTP,
live station searches and journey planning are unavailable; other endpoints
remain usable. Do not enable the `otp` Compose profile for the backend process
if OTP failures must not block startup. The default OTP URL is
`http://localhost:8086`; override it with `OTP_BASE_URL` when using another instance.

Run only the frontend during development:

```shell
cd frontend
npm run dev
```