# ReconFlow

**A Java-based transaction reconciliation tool for banking and FinTech scenarios.**

ReconFlow is a learning project designed to explore how financial systems compare transaction records from different sources, identify discrepancies, and highlight transactions that require further investigation.

The project uses Java, Hibernate/JPA, and PostgreSQL to manage transaction data and persist reconciliation history.

## Example Output

The following example illustrates how ReconFlow identifies differences between payment and accounting records, including potential duplicate transactions.

| **Transaction ID** | **Payment Amount** | **Accounting Amount** | **Status** |
|---|---:|---:|---|
| TX001 | 100.00 | 100.00 | Match |
| TX002 | 50.00 | 55.00 | Amount mismatch |
| TX003 | 75.00 | — | Missing in accounting |
| TX004 | 120.00 | 120.00 | Match |
| TX005 | 9.99 | 10.00 | Amount mismatch |
| TX006 | 200.00 | — | Potential duplicate |
| TX006 | 200.00 | — | Potential duplicate |
| TX009 | 45.50 | 45.50 | Match |
| TX010 | 300.00 | 300.00 | Match |
| TX012 | 640.00 | 635.00 | Amount mismatch |

## Project Goal

In financial systems, the same transaction may be recorded by multiple systems, such as a payment platform and an accounting system. Their records do not always match.

ReconFlow aims to identify these differences through a reconciliation process, helping highlight transactions that may require further investigation.

## Features

- Load transaction data from CSV files.
- Compare transactions using transaction IDs.
- Identify matching transactions.
- Detect amount mismatches.
- Identify transactions missing from either system.
- Detect potential duplicate transaction IDs.
- Display payment and accounting transactions side by side.
- Generate reconciliation and discrepancy reports through a command-line interface.
- Persist payment and accounting transactions in PostgreSQL.
- Use Hibernate/JPA for database operations and transaction management.
- Retrieve all transactions or search by database ID or transaction ID.
- Delete individual transactions or clear all records from either source.
- Prevent repeated imports of identical transactions using transaction ID and amount.
- Create and persist reconciliation runs.
- Store individual reconciliation results linked to each run.
- Maintain reconciliation summaries, including total transactions, matches, discrepancies, and potential duplicates.
- Compare new reconciliation results with the latest saved run.
- Prevent saving a new reconciliation run when no changes are detected.
- Bidirectional reconciliation between payment and accounting records
- Detection of transactions missing from either system
- Detection of potential duplicate transactions
- Comparison of new reconciliation results with the latest saved run
- Prevention of duplicate reconciliation runs when no changes are detected
- Persistence of reconciliation runs and individual results in PostgreSQL

## Technologies

- **Java 21**
- **Maven**
- **PostgreSQL**
- **Hibernate ORM / Jakarta Persistence (JPA)**
- **BigDecimal** for monetary values
- **Java Collections:** List, HashMap and HashSet
- **CSV file handling**
- **Flyway** for database migrations
- **Git and GitHub**

## Technical Implementation

### Transaction Lookup

ReconFlow uses different Java collections for different responsibilities:

- `List<Transaction>` preserves the original transaction records, including duplicate transaction IDs.
- `HashMap<String, Transaction>` provides key-based lookups using transaction IDs.
- `HashSet<String>` helps identify duplicate transaction IDs.

Pre-built HashMaps are used to improve lookup efficiency instead of repeatedly iterating through transaction lists.

This improves average lookup complexity from **O(n) to O(1)**.

The original lists remain important because a HashMap can only store one value per key. Keeping both structures ensures duplicate records are not lost during reconciliation.

### Financial Data Handling

`BigDecimal` is used for monetary values to avoid floating-point precision issues.

Transaction amounts are compared using `compareTo()` rather than direct object equality.

### Database Persistence

ReconFlow uses PostgreSQL as its relational database and Hibernate/JPA for persistence.

The database structure includes four tables:

- `payments`: stores transactions originating from the payment system.
- `accounting`: stores transactions originating from the accounting system.
- `reconciliation_runs`: stores individual reconciliation executions and their summary information.
- `reconciliation_results`: stores the individual transaction results associated with each reconciliation run.

Both source transaction tables use generated primary keys and store monetary values using `NUMERIC(12, 2)`.

Hibernate's `EntityManager` handles database operations and transaction management.

A shared `TransactionsRepository` provides persistence operations for payment and accounting entities.

The transaction repository supports:

- Saving payment and accounting transactions.
- Retrieving all records from either table.
- Finding records by their generated database ID.
- Finding records by transaction ID.
- Deleting individual records by database ID.
- Deleting all records from either table.

During import, existing records are checked using both `transaction_id` and `amount`. Identical records already stored in the database are skipped, preventing repeated application launches from creating unnecessary copies.

Potential duplicate transaction IDs with different amounts or distinct records remain available for reconciliation analysis.

### Reconciliation History

Each reconciliation execution creates a new `ReconciliationRun` entity.

A run stores summary information:

- Creation timestamp.
- Total number of reconciliation results.
- Number of matched transactions.
- Number of discrepancies.
- Number of potential duplicates.

Each individual `ReconciliationResult` stores:

- Transaction ID.
- Payment amount.
- Accounting amount.
- Reconciliation status.
- Reference to its parent reconciliation run.

The relationship between `ReconciliationRun` and `ReconciliationResult` is one-to-many.

A single reconciliation run can contain multiple transaction results, while each result belongs to one run.

This design preserves historical reconciliation data instead of overwriting previous results.

Before persisting a new reconciliation run, ReconFlow compares its results with the latest saved run.

If all transaction IDs, amounts, and reconciliation statuses are unchanged, the new run is skipped. This prevents unnecessary duplicate entries in the reconciliation history.

A new run is persisted when changes are detected, such as modified transaction amounts, new transactions, or different reconciliation statuses.

Each reconciliation run stores its individual transaction results, including transaction IDs, payment amounts, accounting amounts, and reconciliation statuses.

ReconFlow performs bidirectional reconciliation, identifying transactions that exist in the payment system but are missing from accounting, as well as transactions recorded in accounting but missing from the payment system.

Before saving a new run, the application compares its results with the latest saved run. If all transaction IDs, amounts, and statuses remain unchanged, the new run is skipped. This prevents unnecessary duplicate entries in the reconciliation history.
### ACID Transactions

Database operations use JPA transactions to maintain data integrity.

Payment and accounting imports are executed within a database transaction.

Reconciliation history is also persisted transactionally: the reconciliation run and all its individual results are saved within the same database transaction.

The implementation supports the four ACID properties:

- **Atomicity:** Related records are persisted together. If an error occurs, the transaction is rolled back.
- **Consistency:** Database constraints enforce data validity and integrity.
- **Isolation:** Transaction isolation is handled by PostgreSQL according to the configured isolation level.
- **Durability:** Committed transactions are persisted by PostgreSQL.

This transactional approach prevents partial imports and incomplete reconciliation history.

## Project Structure

```text
reconflow/
├── data/
│   ├── payments.csv
│   └── accounting.csv
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── reconflow/
│       │           ├── config/
│       │           ├── exceptions/
│       │           ├── inputHelpers/
│       │           ├── models/
│       │           ├── repository/
│       │           ├── service/
│       │           ├── UI/
│       │           └── Main.java
│       └── resources/
│           └── db/
│               └── migration/
├── pom.xml
└── README.md
```

## Getting Started

### Prerequisites

- Java JDK 21 or later
- Maven
- PostgreSQL
- Git

### Run the Application

1. Clone the repository:

```bash
git clone https://github.com/Carmelo85S/reconflow.git
```

2. Open the project in IntelliJ IDEA.
3. Configure the PostgreSQL connection and environment variables.
4. Ensure that `payments.csv` and `accounting.csv` are located in the `data/` directory at the project root.
5. Run the `Main` class.

Flyway applies the database migrations during application initialization.

## Reconciliation Scenarios

| **Scenario** | **Description** |
|---|---|
| Match | Transaction ID and amount match in both systems |
| Amount mismatch | Transaction exists in both systems, but the amounts differ |
| Missing in accounting | Transaction exists only in the payment system |
| Missing in payment system | Transaction exists only in the accounting system |
| Potential duplicate | A transaction ID appears more than once in the source data |

## Roadmap

- [x] Create the initial Java application structure
- [x] Model transactions using `BigDecimal`
- [x] Load transaction data from CSV files
- [x] Compare transaction IDs and amounts
- [x] Identify missing transactions in both systems
- [x] Implement potential duplicate detection
- [x] Display payment and accounting transactions side by side
- [x] Display reconciliation discrepancies
- [x] Introduce HashMap-based lookup methods
- [x] Integrate PostgreSQL
- [x] Configure Hibernate/JPA persistence
- [x] Create payment and accounting database entities
- [x] Implement transaction repository
- [x] Persist payment and accounting transactions
- [x] Implement transaction lookup operations
- [x] Implement transaction deletion operations
- [x] Prevent repeated imports of identical transactions
- [x] Create reconciliation run and result entities
- [x] Establish one-to-many relationship between runs and results
- [x] Persist reconciliation history in PostgreSQL
- [x] Save reconciliation runs and results within a single database transaction
- [x] Compare reconciliation results with the latest saved run
- [x] Prevent duplicate reconciliation runs when no changes are detected
- [x] Implement bidirectional transaction reconciliation
- [x] Detect potential duplicate transactions
- [x] Persist reconciliation runs and individual results
- [x] Compare results against the latest reconciliation run
- [x] Prevent saving unchanged reconciliation runs
- [ ] Retrieve and display reconciliation history
- [ ] View detailed results for a selected run
- [ ] Export reconciliation reports to CSV
- 
## Learning Objectives

This project is part of my journey toward becoming a Java developer, with a particular interest in backend development, banking, and financial technology.

The main objectives are to practise:

- Object-oriented programming
- Java Collections and data structures
- File I/O and input validation
- Financial data handling
- Exception handling and unit testing
- Algorithmic complexity and efficient lookups
- Relational database design
- Hibernate and ORM concepts
- Entity relationships and database persistence
- Database transactions and data integrity
- Backend architecture and REST API development

## Project Status

ReconFlow is an educational project under active development.

The application currently supports:

- CSV-based transaction imports from payment and accounting systems.
- Transaction reconciliation using transaction IDs and amounts.
- Detection of matching transactions, amount mismatches, missing records, and potential duplicates.
- PostgreSQL persistence using Hibernate/JPA.
- Historical reconciliation runs, storing individual transaction results and summary statistics.
- Reconciliation history displayed through the CLI, including run IDs, timestamps, and result counts.
- Detailed inspection of historical runs, including transaction IDs, payment amounts, accounting amounts, and reconciliation statuses.
- Duplicate-run prevention by comparing new reconciliation results with the latest saved run.

Each reconciliation execution is stored as a separate run. A new run is only persisted when its results differ from the latest saved execution.

**Next steps:** Improve reconciliation history management and explore additional features such as deleting historical runs and generating reconciliation reports.
## Author

**Carmelo Salis**

Aspiring Java Developer | Backend Development | Banking & FinTech