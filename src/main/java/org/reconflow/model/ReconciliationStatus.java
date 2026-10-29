package org.reconflow.model;

public enum ReconciliationStatus {
    MATCH ("Match"),
    AMOUNT_MISMATCH ("Amount mismatch"),
    MISSING_IN_ACCOUNTING ("Missing in account"),
    MISSING_IN_PAYMENT_SYSTEM ("Missing in payment system"),
    POTENTIAL_DUPLICATE ("Potential duplicate");

    private final String label;

    ReconciliationStatus (String label){
        this.label = label;
    }

    public String getLabel(){
        return label;
    }
}
