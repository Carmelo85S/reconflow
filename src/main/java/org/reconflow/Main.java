package org.reconflow;

import org.reconflow.inputHelpers.InputHelpers;
import org.reconflow.model.Transaction;
import org.reconflow.service.ReconciliationService;

import java.util.List;
import java.util.Scanner;

import static org.reconflow.UI.Layout.printHeader;
import static org.reconflow.UI.Layout.showMenu;
import static org.reconflow.repository.HandleData.loadData;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Transaction> paymentTransactions = loadData("data/payments.csv");
        List<Transaction> accountingTransactions = loadData("data/accounting.csv");

        ReconciliationService service = new ReconciliationService();

        printHeader("Recon Flow", "Transaction Reconciliation");
        boolean running = true;
        int choice;
        while (running) {
            showMenu();
            choice = InputHelpers.readMenuChoice(scanner, 0, 3, "Choose your option: > ");
            if (choice == 0) {
                running = false;
            } else {
                runAction(scanner, choice, service, paymentTransactions, accountingTransactions);
            }
        }
        scanner.close();
    }
}
