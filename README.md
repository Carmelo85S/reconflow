# ReconFlow

**A Java-based transaction reconciliation tool for banking and FinTech scenarios.**

ReconFlow is a learning project designed to explore how financial systems compare transaction records from different sources, identify discrepancies, and highlight transactions that require further investigation.

The project uses Java, Hibernate/JPA, and PostgreSQL to manage and persist transaction data.

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

## Technologies

- **Java 21**
- **Maven**
- **PostgreSQL**
- **Hibernate ORM / Jakarta Persistence (JPA)**
- **BigDecimal** for monetary values
- **Java Collections:** List, HashMap and HashSet
- **CSV file handling**
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

The current database structure consists of two tables:

- `payments`: stores transactions originating from the payment system.
- `accounting`: stores transactions originating from the accounting system.

Both tables use generated primary keys and store monetary values using `NUMERIC(12, 2)`.

Hibernate's `EntityManager` handles database operations and transaction management.

A shared `TransactionsRepository` provides persistence operations for both entities.

The repository currently supports:

- Saving payment and accounting transactions.
- Retrieving all records from either table.
- Finding records by their generated database ID.
- Finding records by transaction ID.
- Deleting individual records by database ID.
- Deleting all records from either table.

During import, existing records are checked using both `transaction_id` and `amount`. Identical records already stored in the database are skipped, preventing repeated application launches from creating unnecessary copies.

Potential duplicate transaction IDs with different amounts or distinct records remain available for reconciliation analysis.

## Project Structure

```text
reconflow/
├── data/
│   ├── payments.csv
│   └── accounting.csv
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── reconflow/
│                   ├── config/
│                   ├── exceptions/
│                   ├── inputHelpers/
│                   ├── models/
│                   ├── repository/
│                   ├── service/
│                   ├── UI/
│                   └── Main.java
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
3. Configure the PostgreSQL connection and Hibernate persistence settings.
4. Ensure that `payments.csv` and `accounting.csv` are located in the `data/` directory at the project root.
5. Run the `Main` class.

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
- Database transactions and data integrity
- Backend architecture and REST API development

## Project Status

ReconFlow is an educational project under active development. It currently supports CSV-based transaction input, reconciliation logic, and PostgreSQL persistence through Hibernate/JPA.

Reconciliation history and database-based result storage are planned features.

It is not intended for production financial processing.

## Author

**Carmelo Salis**

Aspiring Java Developer | Backend Development | Banking & FinTech