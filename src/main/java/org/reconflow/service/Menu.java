package org.reconflow.service;

import org.reconflow.exceptions.QuitReconflowOperationException;
import org.reconflow.models.Accounting;
import org.reconflow.models.Payments;
import org.reconflow.models.Transaction;
import org.reconflow.repository.ReconciliationRepository;
import org.reconflow.repository.ReconciliationRepositoryImpl;
import org.reconflow.repository.TransactionsRepositoryImp;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Menu {

    public static void runAction(Scanner scanner, int choice, ReconciliationService service, List<Transaction> paymentTransactions, List<Transaction> accountingTransactions, HashMap<String, Transaction> paymentById,
                                 HashMap<String, Transaction> accountingById, List<Payments> payments, List<Accounting> accounting, TransactionsRepositoryImp tri) {
        try {
            switch (choice) {
                case 1 -> service.runReconciliation(paymentTransactions, accountingTransactions, accountingById, paymentById);
                case 2 -> service.viewTransactions(paymentTransactions, accountingTransactions);
                case 3 -> service.viewDuplicated(paymentTransactions);
                case 4 -> service.viewDiscrepancies(
                        paymentTransactions,
                        accountingTransactions,
                        paymentById,
                        accountingById
                );
                case 5 -> tri.save(payments, accounting);
                case 6 -> service.viewReconciliationHistory();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid input");
            }
        } catch (QuitReconflowOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
