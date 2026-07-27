# Investment Management API

A backend REST API for managing investment funds, investors, and the transactions between them, with basic reporting. Built with Java and Spring Boot.

## Overview

The system models three core concepts:

- **Funds** — pools of money that investors contribute to.
- **Investors** — people who contribute to one or more funds (a fund can have many investors, and an investor can belong to many funds — a many-to-many relationship).
- **Transactions** — money movements applied to a fund by an investor on a given date. Each transaction has a **type**, and the type determines whether it credits (adds to) or debits (subtracts from) the fund:

| Type              | Effect  |
|-------------------|---------|
| `CONTRIBUTION`    | Credit  |
| `INTEREST_INCOME` | Credit  |
| `DISTRIBUTION`    | Debit   |
| `GENERAL_EXPENSE` | Debit   |
| `MANAGEMENT_FEE`  | Debit   |

Reporting derives a fund's balance and an investor's net position by summing their transactions and applying these credit/debit rules.

## Requirements

- Java 21 (or later)

No separate database or build tool installation is needed. The project uses an in-memory H2 database and ships with the Maven wrapper.

## Running the application

From the project root, run a single command:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8080`.

Because the database is in-memory, all data resets on each restart. This is intentional for a demo — it means the app runs with zero setup.

## Inspecting the database (optional)

While the app is running, an H2 web console is available at:

```
http://localhost:8080/h2-console
```

Connect using JDBC URL `jdbc:h2:mem:investmentdb`, user `sa`, and a blank password.

## API endpoints

### Funds
| Method | Path              | Description        |
|--------|-------------------|--------------------|
| POST   | `/api/funds`      | Create a fund      |
| GET    | `/api/funds`      | List all funds     |
| GET    | `/api/funds/{id}` | Get one fund       |
| PUT    | `/api/funds/{id}` | Update a fund name |
| DELETE | `/api/funds/{id}` | Delete a fund      |

### Investors
| Method | Path                  | Description        |
|--------|-----------------------|--------------------|
| POST   | `/api/investors`      | Create an investor |
| GET    | `/api/investors`      | List all investors |
| GET    | `/api/investors/{id}` | Get one investor   |
| PUT    | `/api/investors/{id}` | Update a name      |
| DELETE | `/api/investors/{id}` | Delete an investor |

### Transactions
| Method | Path                     | Description                    |
|--------|--------------------------|--------------------------------|
| POST   | `/api/transactions`      | Record a transaction           |
| GET    | `/api/transactions`      | List all transactions          |
| GET    | `/api/transactions/{id}` | Get one transaction            |
| DELETE | `/api/transactions/{id}` | Delete a transaction           |

Transactions are intentionally not updatable (see Design decisions).

### Reports
| Method | Path                                  | Description                              |
|--------|---------------------------------------|------------------------------------------|
| GET    | `/api/reports/funds/{id}/balance`     | Current balance of a fund                |
| GET    | `/api/reports/investors/{id}/summary` | Net position of an investor across funds |

## Example usage

Create a fund:

```bash
curl -X POST http://localhost:8080/api/funds \
  -H "Content-Type: application/json" \
  -d '{"name": "Growth Fund"}'
```

Create an investor:

```bash
curl -X POST http://localhost:8080/api/investors \
  -H "Content-Type: application/json" \
  -d '{"name": "Jane Doe"}'
```

Record a contribution (a credit):

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "fundId": 1,
    "investorId": 1,
    "transactionDate": "2026-07-26",
    "amount": 100,
    "type": "CONTRIBUTION"
  }'
```

Check the fund balance:

```bash
curl http://localhost:8080/api/reports/funds/1/balance
```

## Design decisions and tradeoffs

This section captures the main engineering choices and the reasoning behind them.

**Layered architecture (controller → service → repository).** Controllers handle only HTTP concerns and delegate immediately to services. Services hold business logic and orchestrate persistence. Repositories handle data access. This separation keeps each layer focused and testable.

**DTOs at the API boundary.** The API exposes request/response DTOs rather than JPA entities. This decouples the public contract from the internal data model (the schema can change without breaking clients), prevents accidental over-exposure of internal fields, guards against mass-assignment on input, and avoids infinite-recursion problems when serializing bidirectional relationships to JSON.

**`BigDecimal` for money.** All monetary amounts use `BigDecimal`, never `double`/`float`. Floating-point types cannot represent decimal fractions exactly, which causes rounding errors that are unacceptable in financial calculations.

**Amount magnitude + type direction.** Transaction amounts are always stored as positive values; the transaction *type* determines whether the amount is added or subtracted. Direction lives in the type, magnitude lives in the amount — this keeps the balance calculation clean and validation simple (`@Positive`).

**Transaction type as an enum that carries its own effect.** Each `TransactionType` is defined with its credit/debit `effect`. Balance calculations ask the type for its effect rather than checking specific type names, so adding a new transaction type in future requires no changes to any calculation logic. The credit/debit rule lives in exactly one place.

**Immutable transactions.** Transactions represent historical financial records, so no update operation is exposed — they form an append-only ledger. In a production system, corrections would be handled via reversing/adjusting entries rather than editing or deleting records, preserving a full audit trail.

**`LocalDate` for transaction dates.** A transaction occurs on a calendar day, so `LocalDate` (immutable, date-only, modern API) is used rather than the legacy `java.util.Date`.

**Lazy loading on relationships.** `@ManyToOne` associations are configured `FetchType.LAZY` to avoid loading related entities that a given operation doesn't need.

**H2 in-memory database.** For a zero-setup demo the app uses in-memory H2, so it runs with a single command and no external dependencies. Because persistence is behind Spring Data JPA, the database is effectively pluggable — the same entity and repository code would run against PostgreSQL by swapping the driver and connection settings.

**Schema generation.** For this demo, Hibernate auto-generates the schema from the entities (`ddl-auto=update`). In production this would be set to `validate`, with schema changes managed through versioned migrations (e.g. Flyway) so they are reviewable and reversible.

## Possible next steps

Given more time, natural extensions would include: unit tests for the balance calculation and integration tests for the endpoints, endpoints to manage fund–investor membership directly, pagination on list endpoints, and a PostgreSQL profile with Flyway migrations for a production-shaped deployment.
