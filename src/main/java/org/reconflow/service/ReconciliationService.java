package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.List;

import static org.reconflow.UI.Layout.headerTable;

public class ReconciliationService {

    public void runReconciliation(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {

        headerTable();

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
                            "| %-14s | %-14s | %-17s | %-25s | %n",
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
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        payment.getTransactionId(),
                        payment.getAmount(),
                        " - ",
                        ReconciliationStatus.MISSING_IN_ACCOUNTING.getLabel()
                );
            }
        }
        System.out.println(" ---------------------------------------------------------------------------------");

    }

    public void viewDiscrepancies(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {
        headerTable();

        for (Transaction p : paymentTransactions) {
            boolean found = false;

            for (Transaction a : accountingTransactions) {
                if (p.getTransactionId().equals(a.getTransactionId())) {
                    found = true;

                    if (p.getAmount().compareTo(a.getAmount()) !=0) {
                        System.out.printf(
                                "| %-14s | %-14s | %-17s | %-25s | %n",
                                p.getTransactionId(),
                                p.getAmount(),
                                a.getAmount(),
                                ReconciliationStatus.AMOUNT_MISMATCH.getLabel()
                        );
                    }

                    break;
                }
            }

            if (!found) {
                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        p.getTransactionId(),
                        p.getAmount(),
                        "-",
                        ReconciliationStatus.MISSING_IN_ACCOUNTING.getLabel()
                );
            }
        }
        for (Transaction a : accountingTransactions) {
            boolean found = false;

            for (Transaction pa : paymentTransactions) {
                if (a.getTransactionId().equals(pa.getTransactionId())) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        a.getTransactionId(),
                        "-",
                        a.getAmount(),
                        ReconciliationStatus.MISSING_IN_PAYMENT_SYSTEM.getLabel()
                );
            }
        }

        System.out.println(" ---------------------------------------------------------------------------------");

    }

    public void viewTransacitions(List<Transaction> paymentTransactions, List<Transaction> accountingTransaction){}
}