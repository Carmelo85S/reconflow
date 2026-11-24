package org.reconflow.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.reconflow.config.EntityManagerUtil;
import org.reconflow.models.Payments;
import org.reconflow.models.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class PaymentRepositoryImp implements PaymentRepository {
    @Override
    public void save(List<Payments> payments) {

        EntityManager em = EntityManagerUtil
                .getEntityManagerFactory()
                .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for (Payments payment : payments) {
                em.persist(payment);
            }

            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public List<Payments> findAll() {
        return List.of();
    }

    @Override
    public Optional<Payments> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Payments> findByTransactionId(String transactionId) {
        return Optional.empty();
    }

    @Override
    public long deleteAll() {
        return 0;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
