# API

This folder contains the Java-based API for the World Cup Stats project.

## Development

### Prerequisites
- Java 21
- Docker and Docker Compose

### Local Database
To start the local PostgreSQL database:
```bash
docker compose up -d
```

The database will be available at `localhost:5432`. On application startup, **Flyway** automatically runs all pending migrations located in `src/main/resources/db/migration` to set up the schema and seed initial data.

Database connection details (local profile):
- Database: `worldcupstats`
- User: `worldcupuser`
- Password: `worldcuppassword`

### Quality Checks
To run all verification tasks (unit tests + static analysis):
```bash
./gradlew check
```

Static analysis is performed using [Checkstyle](https://checkstyle.org/). Configuration can be found in `config/checkstyle/checkstyle.xml`.
Reports are generated in `build/reports/checkstyle/`.

### Running Tests
To run the unit tests (fast, no integration tests):
```bash
./gradlew test
```
(Note: These tests use an in-memory H2 database and do **not** require Docker. They exclude tests tagged with `@Tag("integration")`).

### Integration Tests
To run tests that require a real database or perform full dataset ingestion:
1. Ensure the database is running: `docker compose up -d`
2. Run the integration tests:
```bash
./gradlew integrationTest
```
This task runs tests tagged with `@Tag("integration")`, including:
- Database connectivity and migration checks.
- Full Kaggle dataset ingestion integration tests.
- Fixture-based ingestion tests for CI/CD.

### Running the API
```bash
./gradlew bootRun
```
By default, the application runs with the `local` profile, which connects to the database started via Docker Compose.

To run with a specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=yourprofile'
```
