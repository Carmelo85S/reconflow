package org.reconflow;

import org.reconflow.inputHelpers.InputHelpers;
import org.reconflow.model.Transaction;
import org.reconflow.service.ReconciliationService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import static org.reconflow.UI.Layout.printHeader;
import static org.reconflow.UI.Layout.showMenu;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        List<Transaction> paymentTransactions = List.of(
                new Transaction("TX001", new BigDecimal("100.00")),
                new Transaction("TX002", new BigDecimal("50.00")),
                new Transaction("TX003", new BigDecimal("75.00")),
                new Transaction("TX004", new BigDecimal("120.00")),
                new Transaction("TX005", new BigDecimal("9.99")),
                new Transaction("TX006", new BigDecimal("200.00"))
        );

        List<Transaction> accountingTransactions = List.of(
                new Transaction("TX001", new BigDecimal("100.00")),
                new Transaction("TX002", new BigDecimal("55.00")),
                new Transaction("TX004", new BigDecimal("120.00")),
                new Transaction("TX005", new BigDecimal("10.00")),
                new Transaction("TX007", new BigDecimal("30.00")),
                new Transaction("TX008", new BigDecimal("80.00"))
        );

        ReconciliationService service = new ReconciliationService();
        service.reconcile(paymentTransactions, accountingTransactions);

        printHeader("Recon Flow", "Transaction Reconciliation");
        boolean running = true;
        int choice;
        while (running) {
            showMenu();
            choice = InputHelpers.readMenuChoice(scanner, 0, 3, "Choose your option: > ");
            if (choice == 0) {
                running = false;
            } else {
                runAction(scanner, choice);
            }
        }
    }
}
