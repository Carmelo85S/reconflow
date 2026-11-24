package org.reconflow.repository;

import org.reconflow.models.Accounting;
import org.reconflow.models.Payments;

import java.util.List;
import java.util.Optional;

public interface TransactionsRepository {

    void save(List<Payments> payment, List<Accounting> accounting);

    List<Payments> findAllPayments();

    List<Accounting> findAllAccounting();

    Optional<Payments> findPaymentById(Long id);

    List<Payments> findPaymentByTransactionId(String transactionId);

    Optional<Accounting> findAccountingById(Long id);

    List<Accounting> findAccountingByTransactionId(String transactionId);

    long deleteAllPayments();

    long deleteAllAccounting();

    boolean deletePaymentById(Long id);

    boolean deleteAccountingById(Long id);

}

