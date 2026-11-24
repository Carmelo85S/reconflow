package org.reconflow.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name ="payments")
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    public Payments() {
    }
    
    public Payments(String transactionId, BigDecimal amount){
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Long getId() {
        return id;
    }



}
