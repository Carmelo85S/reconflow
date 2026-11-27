package org.reconflow;

import org.reconflow.config.EntityManagerUtil;
import org.reconflow.inputHelpers.InputHelpers;
import org.reconflow.models.Accounting;
import org.reconflow.models.Payments;
import org.reconflow.models.Transaction;
import org.reconflow.repository.ReconciliationRepository;
import org.reconflow.repository.ReconciliationRepositoryImpl;
import org.reconflow.repository.TransactionsRepositoryImp;
import org.reconflow.service.ReconciliationService;


import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

import static org.reconflow.UI.Layout.*;
import static org.reconflow.repository.HandleData.loadData;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {
        TransactionsRepositoryImp tri = new TransactionsRepositoryImp();

        EntityManagerUtil.getEntityManagerFactory();

        printWelcomeMessage();
        Scanner scanner = new Scanner(System.in);

        List<Transaction> paymentTransactions = loadData("data/payments.csv");
        List<Transaction> accountingTransactions = loadData("data/accounting.csv");

        HashMap<String, Transaction> paymentById = new HashMap<>();
        for(Transaction transaction : paymentTransactions){
            paymentById.put(transaction.getTransactionId(), transaction);
        }

        HashMap<String, Transaction> accountingById = new HashMap<>();
        for(Transaction transaction : accountingTransactions){
            accountingById.put(transaction.getTransactionId(), transaction);
        }

        List<Payments> payments = paymentTransactions.stream()
                .map(transaction -> new Payments(
                        transaction.getTransactionId(),
                        transaction.getAmount()
                ))
                .toList();

        List<Accounting> accounting = accountingTransactions.stream()
                .map(transaction -> new Accounting(
                        transaction.getTransactionId(),
                        transaction.getAmount()
                ))
                .toList();


        ReconciliationRepository reconciliationRepository =
                new ReconciliationRepositoryImpl();

        ReconciliationService service =
                new ReconciliationService(reconciliationRepository);

        printHeader("Recon Flow", "Transaction Reconciliation");
        boolean running = true;
        int choice;
        while (running) {
            showMenu();
            choice = InputHelpers.readMenuChoice(scanner, 0, 7, "Choose your option: > ");
            if (choice == 0) {
                running = false;
            } else {
                runAction(
                        scanner,
                        choice,
                        service,
                        paymentTransactions,
                        accountingTransactions,
                        paymentById,
                        accountingById,
                        payments,
                        accounting,
                        tri
                );
            }
        }
        scanner.close();
    }
}
