package org.reconflow.service;

import org.reconflow.models.ReconciliationResult;
import org.reconflow.models.ReconciliationRun;
import org.reconflow.models.ReconciliationStatus;
import org.reconflow.models.Transaction;
import org.reconflow.repository.ReconciliationRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.reconflow.UI.Layout.headerTable;

public class ReconciliationService {
    private final ReconciliationRepository reconciliationRepository;

    public ReconciliationService(ReconciliationRepository reconciliationRepository) {
        this.reconciliationRepository = reconciliationRepository;
    }

    public void runReconciliation(List<Transaction> paymentTransactions,
                                  List<Transaction> accountingTransactions,
                                  HashMap<String, Transaction> accountingById,
                                  HashMap<String, Transaction> paymentById){


        headerTable();

        List<Transaction> duplicated = findDuplicates(paymentTransactions);

        ReconciliationRun run = new ReconciliationRun();

        int matchedCount = 0;
        int discrepancyCount = 0;
        int duplicateCount = 0;

        for (Transaction payment : paymentTransactions) {

            Transaction accounting = findAccountingTransaction(payment, accountingById);

            ReconciliationStatus status = determineStatus(payment, accounting, duplicated);

            ReconciliationResult result = new ReconciliationResult();

            result.setTransactionId(payment.getTransactionId());
            result.setPaymentAmount(payment.getAmount());
            result.setAccountingAmount(accounting != null ? accounting.getAmount():null);
            result.setStatus(status);
            run.addResult(result);
            if(status == ReconciliationStatus.MATCH){
                matchedCount++;
            }else if(status == ReconciliationStatus.POTENTIAL_DUPLICATE){
                duplicateCount++;
            }else{
                discrepancyCount++;
            }

            System.out.printf(
                    "| %-14s | %-14s | %-17s | %-25s | %n",
                    payment.getTransactionId(),
                    payment.getAmount(),
                    accounting != null ? accounting.getAmount() : "-",
                    status.getLabel()
            );
        }

        for(Transaction accounting : accountingTransactions){
            Transaction payment = findPaymentTransaction(accounting, paymentById);

            if(payment == null){
                ReconciliationResult result = new ReconciliationResult();
                result.setTransactionId(accounting.getTransactionId());
                result.setPaymentAmount(null);
                result.setAccountingAmount(accounting.getAmount());
                result.setStatus(ReconciliationStatus.MISSING_IN_PAYMENT_SYSTEM);

                run.addResult(result);
                discrepancyCount++;

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
        run.setTotalTransactions(run.getResults().size());
        run.setMatchedCount(matchedCount);
        run.setDiscrepancyCount(discrepancyCount);
        run.setDuplicateCount(duplicateCount);

        System.out.println("Results: " + run.getResults().size());

        Optional<ReconciliationRun> latestRun = reconciliationRepository.findLatest();

        if(latestRun.isPresent() && hasSameResult(run, latestRun.get())){
            System.out.println("No change detected. Reconciliation not saved");
            return;
        }

        reconciliationRepository.save(run);
    }

    private boolean hasSameResult(
            ReconciliationRun current,
            ReconciliationRun latest
    ) {
        List<ReconciliationResult> unmatched =
                new ArrayList<>(latest.getResults());

        if (current.getResults().size() != unmatched.size()) {
            return false;
        }

        for (ReconciliationResult currentResult : current.getResults()) {

            int index = -1;

            for (int i = 0; i < unmatched.size(); i++) {
                if (sameResult(currentResult, unmatched.get(i))) {
                    index = i;
                    break;
                }
            }

            if (index == -1) {
                return false;
            }

            unmatched.remove(index);
        }

        return unmatched.isEmpty();
    }

    private boolean sameResult(
            ReconciliationResult a,
            ReconciliationResult b
    ) {
        return Objects.equals(a.getTransactionId(), b.getTransactionId())
                && sameAmount(a.getPaymentAmount(), b.getPaymentAmount())
                && sameAmount(a.getAccountingAmount(), b.getAccountingAmount())
                && a.getStatus() == b.getStatus();
    }

    private boolean sameAmount(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return a == b;
        }

        return a.compareTo(b) == 0;
    }

    public void viewDiscrepancies(
            List<Transaction> paymentTransactions,
            List<Transaction> accountingTransactions,
            HashMap<String, Transaction> paymentById,
            HashMap<String, Transaction> accountingById) {
        headerTable();
        List<Transaction> duplicated = findDuplicates(paymentTransactions);

        for (Transaction p : paymentTransactions) {

            Transaction accounting =
                    findAccountingTransaction(p, accountingById);

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

            Transaction payment = findPaymentTransaction(accounting, paymentById);

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

        if (duplicated.isEmpty()) {
            System.out.println("No duplicated transactions found.");
            return;
        }

        System.out.println("=== DUPLICATED TRANSACTIONS ===");

        System.out.println("+----------------------+------------+");
        System.out.printf("| %-20s | %-10s |%n", "TRANSACTION ID", "AMOUNT");
        System.out.println("+----------------------+------------+");

        for (Transaction d : duplicated) {
            System.out.printf("| %-20s | %10.2f |%n",
                    d.getTransactionId(),
                    d.getAmount());
        }

        System.out.println("+----------------------+------------+");
    }

    public static boolean isMatch(Transaction paymentTransactions, Transaction accountingTransaction) {

        return paymentTransactions.getTransactionId().equals(accountingTransaction.getTransactionId())
                && paymentTransactions.getAmount().compareTo(accountingTransaction.getAmount()) == 0;
    }

    private Transaction findAccountingTransaction(Transaction payment, HashMap<String, Transaction> accountingTransaction) {

        return accountingTransaction.get(payment.getTransactionId());

    }

    private Transaction findPaymentTransaction(Transaction accounting, HashMap<String, Transaction> paymentTransaction) {

        return paymentTransaction.get(accounting.getTransactionId());
    }

    private List<Transaction> findDuplicates(List<Transaction> paymentTransactions) {
        List<Transaction> duplicated = new ArrayList<>();
        HashSet<String> seenId = new HashSet<>();
        HashSet<String> duplicateIds = new HashSet<>();
        for (Transaction dup : paymentTransactions) {
            String id = dup.getTransactionId();
            if (seenId.contains(id)) {
                duplicateIds.add(id);
            } else {
                seenId.add(id);
            }
        }
        for (Transaction d : paymentTransactions) {
            if (duplicateIds.contains(d.getTransactionId())) {
                duplicated.add(d);
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