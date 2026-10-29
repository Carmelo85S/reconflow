package org.reconflow.service;

import org.reconflow.exceptions.QuitReconflowOperationException;
import java.util.Scanner;

public class Menu{

    public static void runAction(Scanner scanner, int choice, ReconciliationService service) {
        try {
            switch (choice) {
                case 1 -> System.out.println("Run reconciliation");
                case 2 -> System.out.println("View transactions");
                case 3 -> System.out.println("View statistics");
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid input");
            }
        } catch (QuitReconflowOperationException e) {
            System.out.println(e.getMessage());
        }
    }
}
