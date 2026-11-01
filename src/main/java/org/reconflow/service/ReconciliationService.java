package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import static org.reconflow.UI.Layout.headerTable;

public class ReconciliationService {

    public void runReconciliation(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {

        headerTable();

        List<Transaction> duplicated = findDuplicates(paymentTransactions);

        for (Transaction payment : paymentTransactions) {

            Transaction accounting = findAccountingTransaction(payment, accountingTransactions);

            ReconciliationStatus status = determineStatus(payment, accounting, duplicated);

            System.out.printf(
                    "| %-14s | %-14s | %-17s | %-25s | %n",
                    payment.getTransactionId(),
                    payment.getAmount(),
                    accounting != null ? accounting.getAmount() : "-",
                    status.getLabel()
            );
        }
        System.out.println(" ---------------------------------------------------------------------------------");

    }

    public void viewDiscrepancies(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {
        headerTable();
        List<Transaction> duplicated = findDuplicates(paymentTransactions);

        for (Transaction p : paymentTransactions) {

            Transaction accounting = findAccountingTransaction(p, accountingTransactions);

            ReconciliationStatus status = determineStatus(p, accounting, duplicated);

            if (status != ReconciliationStatus.MATCH) {
                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        p.getTransactionId(),
                        p.getAmount(),
                        accounting != null ? accounting.getAmount() : "-",
                        status.getLabel()
                );
            }
        }
        for (Transaction accounting : accountingTransactions) {

            Transaction payment = findPaymentTransaction(accounting, paymentTransactions);

            if (payment == null) {
                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        accounting.getTransactionId(),
                        "-",
                        accounting.getAmount(),
                        ReconciliationStatus.MISSING_IN_PAYMENT_SYSTEM.getLabel()
                );
            }
        }

        System.out.println(" ---------------------------------------------------------------------------------");

    }

    public void viewTransactions(List<Transaction> paymentTransactions, List<Transaction> accountingTransaction) {
        System.out.println("\n=== TRANSACTIONS REPORT ===");
        System.out.println(" ------------------------------------------------------------------------------");
        System.out.printf("| %-15s | %-13s |         | %-15s | %-13s |%n",
                "Pay Trans ID", "Pay Amount", "Acc Trans ID", "Acc Amount");
        System.out.println(" ------------------------------------------------------------------------------");

        // find max number of rows
        int maxRows = Math.max(paymentTransactions.size(), accountingTransaction.size());


        for (int i = 0; i < maxRows; i++) {
            //Payment
            String payId = (i < paymentTransactions.size()) ? paymentTransactions.get(i).getTransactionId() : "";
            String payAmount = (i < paymentTransactions.size()) ? String.valueOf(paymentTransactions.get(i).getAmount()) : "";

            //Account
            String accId = (i < accountingTransaction.size()) ? accountingTransaction.get(i).getTransactionId() : "";
            String accAmount = (i < accountingTransaction.size()) ? String.valueOf(accountingTransaction.get(i).getAmount()) : "";

            System.out.printf("| %-15s | %-13s |         | %-15s | %-13s |%n",
                    payId, payAmount, accId, accAmount);
        }
        System.out.println(" ------------------------------------------------------------------------------");

    }

    public void viewDuplicated(List<Transaction> paymentTransactions) {
        List<Transaction> duplicated = findDuplicates(paymentTransactions);
        for (Transaction d : duplicated) {
            System.out.println(d.getTransactionId() + " - " + d.getAmount());
        }
    }

    public static boolean isMatch(Transaction paymentTransactions, Transaction accountingTransaction) {

        return paymentTransactions.getTransactionId().equals(accountingTransaction.getTransactionId())
                && paymentTransactions.getAmount().compareTo(accountingTransaction.getAmount()) == 0;
    }

    private Transaction findAccountingTransaction(Transaction payment, List<Transaction> accountingTransaction) {
        for (Transaction accounting : accountingTransaction) {
            if (payment.getTransactionId().equals(accounting.getTransactionId())) {
                return accounting;
            }
        }
        return null;
    }

    private Transaction findPaymentTransaction(Transaction accounting, List<Transaction> paymentTransaction) {
        for (Transaction payment : paymentTransaction) {
            if (accounting.getTransactionId().equals(payment.getTransactionId())) {
                return payment;
            }
        }
        return null;
    }

    private List<Transaction> findDuplicates(List<Transaction> paymentTransactions) {
        List<Transaction> duplicated = new ArrayList<>();
        List<String> seenId = new ArrayList<>();
        for (Transaction dup : paymentTransactions) {
            String id = dup.getTransactionId();
            if (seenId.contains(id)) {
                duplicated.add(dup);
            } else {
                seenId.add(id);
            }
        }
        return duplicated;
    }

    private static boolean isDuplicated(String transactionId, List<Transaction> duplicated) {
        for (Transaction duplicate : duplicated) {
            if (duplicate.getTransactionId().equals(transactionId)) {
                return true;
            }
        }
        return false;
    }

    private static ReconciliationStatus determineStatus(Transaction payment, Transaction accounting, List<Transaction> duplicated) {
        boolean isDuplicated = isDuplicated(payment.getTransactionId(), duplicated);

        if (isDuplicated) {
            return ReconciliationStatus.POTENTIAL_DUPLICATE;
        }
        if (accounting == null) {
            return ReconciliationStatus.MISSING_IN_ACCOUNTING;

        }
        if (isMatch(payment, accounting)) {
            return ReconciliationStatus.MATCH;
        }

        return ReconciliationStatus.AMOUNT_MISMATCH;

    }
}