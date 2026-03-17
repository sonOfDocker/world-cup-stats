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

The database will be available at `localhost:5432` with:
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
To run the unit tests:
```bash
./gradlew test
```
(Note: These tests use an in-memory H2 database and do **not** require Docker).

### Integration Tests
To run tests that require a real database:
1. Ensure the database is running: `docker compose up -d`
2. Run the integration tests:
```bash
./gradlew integrationTest
```

### Running the API
```bash
./gradlew bootRun
```
By default, the application runs with the `local` profile, which connects to the database started via Docker Compose.

To run with a specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=yourprofile'
```
