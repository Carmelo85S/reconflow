package org.reconflow;

import org.reconflow.inputHelpers.InputHelpers;

import java.util.Scanner;

import static org.reconflow.UI.Layout.printHeader;
import static org.reconflow.UI.Layout.showMenu;
import static org.reconflow.service.Menu.runAction;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

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
