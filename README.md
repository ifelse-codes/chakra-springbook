# Book API

Simple Spring Boot application exposing a book REST API backed by PostgreSQL and OpenAPI/Swagger UI.

## Run

1. Start PostgreSQL with:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=bookdb
export DB_USER=postgres
export DB_PASSWORD=postgres
```

2. Build and run:

```bash
mvn spring-boot:run
```

3. Open Swagger UI:

`http://localhost:8080/swagger-ui.html`

## API Endpoints

- `GET /api/books`
- `GET /api/books/{id}`
- `POST /api/books`
- `PUT /api/books/{id}`
- `DELETE /api/books/{id}`

> Dummy edit to trigger CI/CD pipeline.
