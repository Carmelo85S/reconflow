package org.reconflow;

import org.reconflow.inputHelpers.InputHelpers;
import org.reconflow.model.Transaction;
import org.reconflow.service.ReconciliationService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.reconflow.UI.Layout.printHeader;
import static org.reconflow.UI.Layout.showMenu;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Transaction> paymentTransactions = loadData("data/payments.csv");

        List<Transaction> accountingTransactions = loadData("data/accounting.csv");

        ReconciliationService service = new ReconciliationService();
        service.runReconciliation(paymentTransactions, accountingTransactions);

        printHeader("Recon Flow", "Transaction Reconciliation");
        boolean running = true;
        int choice;
        while (running) {
            showMenu();
            choice = InputHelpers.readMenuChoice(scanner, 0, 3, "Choose your option: > ");
            if (choice == 0) {
                running = false;
            } else {
                runAction(scanner, choice, service);
            }
        }
        scanner.close();
    }

    public static List<Transaction> loadData(String path) {
        List<Transaction> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                String transactionId = values[0];
                BigDecimal amount = new BigDecimal(values[1]);

                Transaction transaction = new Transaction(transactionId, amount);
                records.add(transaction);
            }
        } catch (IOException e) {
            System.out.println("Error reading ");
            return records;
        }
        return records;
    }
}
