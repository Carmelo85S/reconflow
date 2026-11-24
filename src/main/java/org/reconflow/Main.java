package org.reconflow;

import org.reconflow.config.EntityManagerUtil;
import org.reconflow.inputHelpers.InputHelpers;
import org.reconflow.models.Accounting;
import org.reconflow.models.Payments;
import org.reconflow.models.Transaction;
import org.reconflow.repository.PaymentRepository;
import org.reconflow.repository.PaymentRepositoryImp;
import org.reconflow.service.ReconciliationService;


import java.util.List;
import java.util.Scanner;
import java.util.HashMap;

import static org.reconflow.UI.Layout.*;
import static org.reconflow.repository.HandleData.loadData;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {
        PaymentRepositoryImp pri = new PaymentRepositoryImp();

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


        ReconciliationService service = new ReconciliationService();

        printHeader("Recon Flow", "Transaction Reconciliation");
        boolean running = true;
        int choice;
        while (running) {
            showMenu();
            choice = InputHelpers.readMenuChoice(scanner, 0, 5, "Choose your option: > ");
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
                        pri
                );
            }
        }
        scanner.close();
    }
}
