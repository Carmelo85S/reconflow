package org.reconflow.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="reconciliation_results")
public class ReconciliationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_id", nullable = false)
    private ReconciliationRun reconciliationRun;

    @Column(name="transaction_id", nullable = false, length = 255)
    private String transactionId;

    @Column(name = "payment_amount", precision = 12, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "accounting_amount", precision = 12, scale = 2)
    private BigDecimal accountingAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ReconciliationStatus status;

    public ReconciliationResult(){}

    public Long getId() {
        return id;
    }

    public ReconciliationRun getReconciliationRun() {
        return reconciliationRun;
    }

    public void setReconciliationRun(ReconciliationRun reconciliationRun) {
        this.reconciliationRun = reconciliationRun;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getAccountingAmount() {
        return accountingAmount;
    }

    public void setAccountingAmount(BigDecimal accountingAmount) {
        this.accountingAmount = accountingAmount;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public void setStatus(ReconciliationStatus status) {
        this.status = status;
    }
}
