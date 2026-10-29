package org.reconflow.service;

import org.reconflow.exceptions.QuitReconflowOperationException;
import org.reconflow.model.Transaction;

import java.util.List;
import java.util.Scanner;

public class Menu{

    public static void runAction(Scanner scanner, int choice, ReconciliationService service, List<Transaction> paymentTransactions, List<Transaction> accountingTransactions) {
        try {
            switch (choice) {
                case 1 -> service.runReconciliation(paymentTransactions, accountingTransactions);
                case 2 -> service.viewTransacitions(paymentTransactions, accountingTransactions);
                case 3 -> System.out.println("View statistics");
                case 4 -> service.viewDiscrepancies(paymentTransactions, accountingTransactions);
                case 5 -> System.out.println("Export report");
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid input");
            }
        } catch (QuitReconflowOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
