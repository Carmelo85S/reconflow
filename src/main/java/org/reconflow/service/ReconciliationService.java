package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.List;

public class ReconciliationService {

    public void reconcile(List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {
        for (Transaction payment : paymentTransactions) {
            boolean found = false;

            for (Transaction accounting : accountingTransactions) {
                if (payment.getTransactionId().equals(accounting.getTransactionId())) {
                    System.out.println("-------------------------------");
                    System.out.println("Transaction Id: " + payment.getTransactionId());
                    System.out.println("Payment amount: " + payment.getAmount());
                    System.out.println("Accounting Amount: " + accounting.getAmount());
                    found = true;
                    ReconciliationStatus status;
                    if(payment.getAmount().compareTo(accounting.getAmount()) == 0){
                        status = ReconciliationStatus.MATCH;
                    }else {
                        status = ReconciliationStatus.AMOUNT_MISMATCH;
                    }
                    System.out.println("Status: " + status);

                }
            }
            if (!found) {
                System.out.println("-------------------------------");
                System.out.println("Transaction Id: " + payment.getTransactionId());
                System.out.println("Status: " + ReconciliationStatus.MISSING_IN_ACCOUNTING);
                System.out.println();
            }
        }
    }
}