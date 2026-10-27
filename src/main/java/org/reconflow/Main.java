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
                new Transaction("TX0012", new BigDecimal("100.00")),
                new Transaction("TX002", new BigDecimal("100.00")),
                new Transaction("TX020", new BigDecimal("130.00")),
                new Transaction("TX0030", new BigDecimal("30.00")),
                new Transaction("TX0010", new BigDecimal("13.00")),
                new Transaction("TX0012", new BigDecimal("40.00")),
                new Transaction("TX0052", new BigDecimal("56.07")),
                new Transaction("TX0212", new BigDecimal("100.60")),
                new Transaction("TX0432", new BigDecimal("12.50")),
                new Transaction("TX5019", new BigDecimal("130.00"))
                );

        List<Transaction> accountingTransactions = List.of(
                new Transaction("TX0012", new BigDecimal("100.00")),
                new Transaction("TX002", new BigDecimal("130.00")),
                new Transaction("TX020", new BigDecimal("29.00")),
                new Transaction("TX0030", new BigDecimal("34.50")),
                new Transaction("TX0010", new BigDecimal("23.00")),
                new Transaction("TX2042", new BigDecimal("230.00")),
                new Transaction("TX0052", new BigDecimal("564.07")),
                new Transaction("TX0212", new BigDecimal("40.60")),
                new Transaction("TX0432", new BigDecimal("102.50")),
                new Transaction("TX5019", new BigDecimal("130.00"))
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
