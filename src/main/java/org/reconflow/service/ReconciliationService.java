package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.List;

public class ReconciliationService {

    public void runReconciliation(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {

        System.out.println(" ---------------------------------------------------------------------------");
        System.out.printf("| %-13s | %-13s | %-13s | %-18s |%n",
                "Transaction id", "Payment amount", "Accounting amount", "Status");
        System.out.println(" ---------------------------------------------------------------------------");

        for (Transaction payment : paymentTransactions) {
            boolean found = false;

            for (Transaction accounting : accountingTransactions) {
                if (payment.getTransactionId().equals(accounting.getTransactionId())) {
                    ReconciliationStatus status;

                    if (payment.getAmount().compareTo(accounting.getAmount()) == 0) {
                        status = ReconciliationStatus.MATCH;
                    } else {
                        status = ReconciliationStatus.AMOUNT_MISMATCH;
                    }
                    System.out.printf(
                            "| %-14s | %-14s | %-17s | %-18s | %n",
                            payment.getTransactionId(),
                            payment.getAmount(),
                            accounting.getAmount(),
                            status.getLabel()
                    );
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-18s | %n",
                        payment.getTransactionId(),
                        payment.getAmount(),
                        " - ",
                        ReconciliationStatus.MISSING_IN_ACCOUNTING.getLabel()
                );
            }
        }
        System.out.println(" ---------------------------------------------------------------------------");

    }
}