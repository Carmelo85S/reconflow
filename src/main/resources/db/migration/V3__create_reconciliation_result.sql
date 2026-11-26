CREATE TABLE reconciliation_results (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    run_id BIGINT NOT NULL,
    transaction_id VARCHAR(255) NOT NULL,
    payment_amount NUMERIC(12, 2),
    accounting_amount NUMERIC(12, 2),
    status VARCHAR(50) NOT NULL,
    CONSTRAINT fk_reconciliation_run
        FOREIGN KEY (run_id)
        REFERENCES reconciliation_runs(id) ON DELETE CASCADE
);