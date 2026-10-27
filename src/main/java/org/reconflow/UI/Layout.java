package org.reconflow.UI;

public class Layout {

    public static void printHeader(String title, String subtitle) {
        System.out.println("====================================");
        System.out.println("             " + title.toUpperCase().trim());
        System.out.println("     " + subtitle);
        System.out.println("====================================");
    }

    public static void showMenu(){
        System.out.println("[1] Run reconciliation");
        System.out.println("[2] View transactions");
        System.out.println("[3] View statistics");
        System.out.println("[0] Exit");
    }
}
