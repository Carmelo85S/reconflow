package org.reconflow.model;

import java.math.BigDecimal;

public class Transaction {
    private String transactionId;
    private BigDecimal amount;

    public Transaction(String transactionId, BigDecimal amount){
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
