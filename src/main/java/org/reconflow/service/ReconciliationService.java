package org.reconflow.service;

import org.reconflow.model.ReconciliationStatus;
import org.reconflow.model.Transaction;

import java.util.List;

public class ReconciliationService {

    public void reconcile(List<Transaction> paymentTransaction, List<Transaction> accountingTransactions) {
        for (Transaction payment : paymentTransaction) {
            for (Transaction accounting : accountingTransactions) {
                if (payment.getTransactionId().equals(accounting.getTransactionId())) {
                    System.out.println("Transaction found: " + accounting.getTransactionId());
                    System.out.println("Amount: " + accounting.getAmount());
                    ReconciliationStatus status;

                    if(payment.getAmount().compareTo(accounting.getAmount()) == 0){
                        status = ReconciliationStatus.MATCH;
                    }else {
                        status = ReconciliationStatus.AMOUNT_MISMATCH;
                    }
                    System.out.println(status);
                }
            }
        }
    }
}