package org.reconflow.service;

import org.junit.jupiter.api.Test;
import org.reconflow.models.Transaction;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ReconciliationServiceTest {

    @Test
    void shouldReturnTrueWhenTransactionsMatch() {

        Transaction payment = new Transaction(
                "TX001", new BigDecimal("100.00"));
        Transaction accounting = new Transaction(
                "TX001", new BigDecimal("100.00"));
        boolean result = ReconciliationService.isMatch(payment, accounting);
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenAmountsAreDifferent() {
        Transaction payment = new Transaction(
                "TX001", new BigDecimal("100.00"));
        Transaction accounting = new Transaction(
                "TX001", new BigDecimal("120.00"));

        boolean result = ReconciliationService.isMatch(payment, accounting);
        assertFalse(result);

    }

    @Test
    void shouldReturnFalseWhenTransactionIdsAreDifferent() {
        Transaction payment = new Transaction("TX001", new BigDecimal("55.00"));
        Transaction accounting = new Transaction("TX002", new BigDecimal("55.00"));

        boolean result = ReconciliationService.isMatch(payment, accounting);
        assertFalse((result));
    }

}