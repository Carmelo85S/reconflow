package org.reconflow.repository;

import org.reconflow.models.Payments;
import org.reconflow.models.Transaction;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    void save(List<Payments> payment);

    List<Payments> findAll();

    Optional<Payments> findById(Long id);

    Optional<Payments> findByTransactionId(String transactionId);

    long deleteAll();

    boolean deleteById(Long id);
}

