package org.reconflow.UI;

public class Layout {

    public static void printWelcomeMessage() {
        System.out.println("""

            ==========================================
                       Welcome to ReconFlow
            ==========================================

            A Java-based transaction reconciliation tool
            for banking and fintech scenarios.

            ReconFlow compares payment and accounting
            records to identify:

              • Matching transactions
              • Amount discrepancies
              • Missing transactions

            ==========================================
            """);
    }
    public static void printHeader(String title, String subtitle) {
        System.out.println("==========================================");
        System.out.println("                " + title.toUpperCase().trim());
        System.out.println("        " + subtitle);
        System.out.println("==========================================");
    }

    public static void showMenu(){
        System.out.println("[1] Run reconciliation");
        System.out.println("[2] View transactions");
        System.out.println("[3] View duplicated");
        System.out.println("[4] View discrepancies");
        System.out.println("[5] Export report");
        System.out.println("[0] Exit");
    }

    public static void headerTable(){
        System.out.println(" ---------------------------------------------------------------------------------");
        System.out.printf("| %-13s | %-13s | %-13s | %-25s |%n",
                "Transaction id", "Payment amount", "Accounting amount", "Status");
        System.out.println(" ---------------------------------------------------------------------------------");
    }
}
