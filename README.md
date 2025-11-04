# Fitness Tracker API

Backend application for tracking fitness activities. Users can log workouts, track progress, and see their results.

## Features

* JWT authentication with access and refresh tokens.
* CRUD operations for workouts.
* Upload and get progress photos (stored in PostgreSQL).
* Data validation (e.g., non-empty name, past date, positive duration and calories).
* Search with filters, sorting, and pagination.
* Global exception handling.
* Swagger documentation for all endpoints.

## Technologies

* Java 17+
* Spring Boot, Spring MVC
* Spring Data JPA, Hibernate
* PostgreSQL, Liquibase
* JUnit 5, Mockito
* Swagger / OpenAPI
* Docker & Docker Compose
* Lombok, MapStruct

## Getting Started

### Prerequisites

* Docker
* Docker Compose

### Running with Docker

1. Clone the repository:

```bash
git clone https://github.com/coldsteeze/fitness-tracker-api
cd fitness-tracker-api
```

2. Build and start containers:

```bash
docker-compose up --build
```

3. Application will be available at:

```
http://localhost:8080
```

PostgreSQL credentials (auto-created):

* User: `fitness`
* Password: `fitness`
* Database: `fitness_tracker_db`

> Note: The `prod` profile is set up for Docker. Running without Docker requires updating the configuration and connecting to a PostgreSQL database manually.

## API Documentation

All endpoints, request/response details, filters, sorting, and pagination are available in Swagger (OpenAPI).

Access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

or

```
http://localhost:8080/swagger-ui/index.html
```

## Testing

* Unit tests with JUnit 5 and Mockito.
* Minimum 70% coverage for business logic.

Run tests:

```bash
./mvnw test
```

## Git & Commits

* Follow GitFlow branching model.
* Use Conventional Commits for meaningful messages.
* Example types:

  * `feat`: new features
  * `fix`: bug fixes
  * `chore`: build, CI, docs
  * `test`: adding/updating tests
  * `refactor`: code restructuring without changing behavior

## Notes

* DTOs are used to separate API and database models.
* Progress photos are stored as binary data in PostgreSQL.
* Exception handling ensures consistent error messages.
* `application-prod.yml` is configured for Docker deployment.
