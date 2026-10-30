# ReconFlow

**A Java-based transaction reconciliation tool for banking and FinTech scenarios.**

ReconFlow is a learning project designed to explore how financial systems compare transaction records from different sources, identify discrepancies, and highlight transactions that require further investigation.

## Example Output

The following example shows how ReconFlow identifies differences between payment and accounting records, including potential duplicate transactions.

| Transaction ID | Payment Amount | Accounting Amount | Status |
|---|---:|---:|---|
| TX001 | 100.00 | 100.00 | Match |
| TX002 | 50.00 | 55.00 | Amount mismatch |
| TX003 | 75.00 | — | Missing in accounting |
| TX004 | 120.00 | 120.00 | Match |
| TX005 | 9.99 | 10.00 | Amount mismatch |
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
- Display reconciliation results in a command-line interface.

## Technologies

- **Java 21**
- **Maven**
- **CSV file handling**
- **BigDecimal** for monetary values
- **Git and GitHub**

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
│                   ├── exception/
│                   ├── inputHelpers/
│                   ├── model/
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
- Git

### Run the application

1. Clone the repository:

```bash
git clone https://github.com/Carmelo85S/reconflow.git
```

2. Open the project in IntelliJ IDEA.

3. Ensure that `payments.csv` and `accounting.csv` are located in the `data/` directory at the project root.

4. Run the `Main` class.

## Reconciliation Scenarios

| Scenario | Description |
|---|---|
| Match | Transaction ID and amount match in both systems |
| Amount mismatch | Transaction exists in both systems, but the amounts differ |
| Missing in accounting | Transaction exists only in the payment system |
| Missing in payment system | Transaction exists only in the accounting system |
| Potential duplicate | A transaction ID appears more than once in the payment data |

## Roadmap

- [x] Create the initial Java application structure
- [x] Model transactions using `BigDecimal`
- [x] Load transaction data from CSV files
- [x] Compare transaction IDs and amounts
- [x] Identify missing transactions in both systems
- [x] Implement potential duplicate detection
- [x] Display payment and accounting transactions side by side
- [x] Display reconciliation discrepancies
- [ ] Improve input validation and error handling
- [ ] Write unit tests with JUnit
- [ ] Integrate PostgreSQL using JDBC
- [ ] Explore a REST API with Spring Boot

## Learning Objectives

This project is part of my journey toward becoming a Java developer, with a particular interest in backend development, banking, and financial technology.

The main objectives are to practise:

- Object-oriented programming
- Collections and data processing
- File I/O and input validation
- Financial data handling
- Exception handling and unit testing
- Database integration and backend architecture

## Project Status

ReconFlow is an educational project under active development. It uses sample data and is not intended for production financial processing.

## 👤 Author

**Carmelo Salis**

Aspiring Java Developer | Backend Development | Banking & FinTech

[GitHub Profile](https://github.com/Carmelo85S)