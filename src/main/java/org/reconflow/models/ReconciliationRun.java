package org.reconflow.models;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="reconciliation_runs")
public class ReconciliationRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "total_transactions", nullable = false)
    private int totalTransactions;

    @Column(name="matched_count", nullable = false)
    private int matchedCount;

    @Column(name="discrepancy_count", nullable = false)
    private int discrepancyCount;

    @Column(name="duplicate_count", nullable = false)
    private int duplicateCount;

    @OneToMany(mappedBy = "reconciliationRun")
    private List<ReconciliationResult> results = new ArrayList<>();

    public ReconciliationRun(){}

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public int getDiscrepancyCount() {
        return discrepancyCount;
    }

    public void setDiscrepancyCount(int discrepancyCount) {
        this.discrepancyCount = discrepancyCount;
    }

    public int getDuplicateCount() {
        return duplicateCount;
    }

    public void setDuplicateCount(int duplicateCount) {
        this.duplicateCount = duplicateCount;
    }

    public Long getId() {
        return id;
    }

    public List<ReconciliationResult> getResults() {
        return results;
    }

    public void addResult(ReconciliationResult result) {
        results.add(result);
        result.setReconciliationRun(this);
    }


}
