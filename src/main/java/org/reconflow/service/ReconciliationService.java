package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.ArrayList;
import java.util.List;

import static org.reconflow.UI.Layout.headerTable;

public class ReconciliationService {

    public void runReconciliation(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {

        headerTable();
        List<Transaction> duplicated = new ArrayList<>();
        List<String> seenId = new ArrayList<>();

        for (Transaction d : paymentTransactions) {
            String id = d.getTransactionId();
            if (seenId.contains(id)) {
                duplicated.add(d);
            } else {
                seenId.add(id);
            }
        }

        for (Transaction payment : paymentTransactions) {
            boolean found = false;

            for (Transaction accounting : accountingTransactions) {

                if (payment.getTransactionId().equals(accounting.getTransactionId())) {
                    ReconciliationStatus status;

                    boolean isDuplicated = false;

                    for(Transaction dup : duplicated) {
                        if (dup.getTransactionId().equals(payment.getTransactionId())){
                            isDuplicated = true;
                            break;
                        }
                    }

                    if (isDuplicated) {
                        status = ReconciliationStatus.POTENTIAL_DUPLICATE;
                    } else if (payment.getAmount().compareTo(accounting.getAmount()) == 0) {
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
                boolean isDuplicated = false;

                for (Transaction dup : duplicated) {
                    if (dup.getTransactionId().equals(payment.getTransactionId())) {
                        isDuplicated = true;
                        break;
                    }
                }

                ReconciliationStatus status;

                if (isDuplicated) {
                    status = ReconciliationStatus.POTENTIAL_DUPLICATE;
                } else {
                    status = ReconciliationStatus.MISSING_IN_ACCOUNTING;
                }

                System.out.printf(
                        "| %-14s | %-14s | %-17s | %-25s | %n",
                        payment.getTransactionId(),
                        payment.getAmount(),
                        " - ",
                        status.getLabel()
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

                    if (p.getAmount().compareTo(a.getAmount()) != 0) {
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

    public void viewTransacitions(List<Transaction> paymentTransactions, List<Transaction> accountingTransaction) {
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
        List<Transaction> duplicated = new ArrayList<>();
        List<String> seenId = new ArrayList<>();

        for (Transaction d : paymentTransactions) {
            String id = d.getTransactionId();
            if (seenId.contains(id)) {
                duplicated.add(d);
            } else {
                seenId.add(id);
            }
        }
        for (Transaction d : duplicated) {
            System.out.println(d.getTransactionId() + " - " + d.getAmount());
        }
    }
}