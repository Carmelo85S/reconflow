package org.reconflow.model;

public enum ReconciliationStatus {
    MATCH,
    AMOUNT_MISMATCH,
    MISSING_IN_ACCOUNTING,
    MISSING_IN_PAYMENT_SYSTEM,
    POTENTIAL_DUPLICATE
}
