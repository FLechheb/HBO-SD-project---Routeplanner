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
