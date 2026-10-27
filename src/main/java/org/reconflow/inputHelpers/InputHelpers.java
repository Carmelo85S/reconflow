package org.reconflow.inputHelpers;

import org.reconflow.exceptions.InvalidReconFlowException;

import java.util.Scanner;

public class InputHelpers {

    public static int readMenuChoice(Scanner scanner, int min, int max, String prompt) {
        while (true) {
            System.out.println();
            System.out.printf(prompt, min, max);
            try {
                String userInput = scanner.nextLine().trim();
                validateInt(userInput, min, max);
                return Integer.parseInt(userInput);
            } catch (InvalidReconFlowException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static int readIntBetween(Scanner scanner, int min, int max, String prompt){
        while (true) {
            System.out.printf(prompt, min, max);
            try {
                String userInput = scanner.nextLine().trim();
                validateInt(userInput, min, max);
                return Integer.parseInt(userInput);
            } catch (InvalidReconFlowException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void validateInt(String userInput, int min, int max) {
        if (userInput.isBlank()) {
            throw new InvalidReconFlowException("  Your input is blank");
        }
        int value;
        try {
            value = Integer.parseInt(userInput);
        } catch (NumberFormatException e) {
            throw new InvalidReconFlowException("  Input must be a number");
        }
        if (value < min || value > max) {
            throw new InvalidReconFlowException(
                    "  Value should be between " + min + " and " + max
            );
        }
    }
}
