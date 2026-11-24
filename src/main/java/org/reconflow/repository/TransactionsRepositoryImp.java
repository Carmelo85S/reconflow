package org.reconflow.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.reconflow.config.EntityManagerUtil;
import org.reconflow.models.Accounting;
import org.reconflow.models.Payments;

import java.util.List;
import java.util.Optional;

public class TransactionsRepositoryImp implements TransactionsRepository {

    @Override
    public void save(List<Payments> payments, List<Accounting> accounting) {

        EntityManager em = EntityManagerUtil
                .getEntityManagerFactory()
                .createEntityManager();

        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            for (Payments pay : payments) {
                boolean exists = !em.createQuery("""
                                SELECT p FROM Payments p
                                WHERE p.transactionId = :transactionId
                                AND p.amount = :amount
                                """, Payments.class)
                        .setParameter("transactionId", pay.getTransactionId())
                        .setParameter("amount", pay.getAmount())
                        .getResultList()
                        .isEmpty();

                if (!exists) {
                    em.persist(pay);
                }
            }

            for (Accounting acc : accounting) {

                boolean exists = !em.createQuery("""
                                SELECT a FROM Accounting a
                                WHERE a.transactionId = :transactionId
                                AND a.amount = :amount
                                """, Accounting.class)
                        .setParameter("transactionId", acc.getTransactionId())
                        .setParameter("amount", acc.getAmount())
                        .getResultList()
                        .isEmpty();

                if (!exists) {
                    em.persist(acc);
                }
            }


            tx.commit();

        } catch (
                RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public List<Payments> findAllPayments() {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(
                    "SELECT p FROM Payments p",
                    Payments.class
            ).getResultList();
        }
    }

    @Override
    public List<Accounting> findAllAccounting() {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(
                    "SELECT a FROM Accounting a",
                    Accounting.class
            ).getResultList();
        }
    }

    @Override
    public Optional<Payments> findPaymentById(Long id) {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return Optional.ofNullable(
                    em.find(Payments.class, id)
            );
        }
    }

    @Override
    public List<Payments> findPaymentByTransactionId(String transactionId) {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(
                            "SELECT p FROM Payments p WHERE transactionId = :transactionId",
                            Payments.class
                    ).setParameter("transactionId", transactionId)
                    .getResultList();
        }
    }

    @Override
    public Optional<Accounting> findAccountingById(Long id) {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return Optional.ofNullable(
                    em.find(Accounting.class, id)
            );
        }
    }

    @Override
    public List<Accounting> findAccountingByTransactionId(String transactionId) {
        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            return em.createQuery(
                            "SELECT a FROM Accounting a WHERE transactionId = :transactionId",
                            Accounting.class
                    ).setParameter("transactionId", transactionId)
                    .getResultList();
        }
    }

    @Override
    public long deleteAllPayments() {
        EntityTransaction tx = null;

        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            int delete = em.createQuery("DELETE FROM Payments")
                    .executeUpdate();
            tx.commit();
            return delete;

        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }


    @Override
    public long deleteAllAccounting() {
        EntityTransaction tx = null;

        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            int delete = em.createQuery("DELETE FROM Accounting")
                    .executeUpdate();
            tx.commit();

            return delete;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }


    @Override
    public boolean deletePaymentById(Long id) {
        EntityTransaction tx = null;

        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            int delete = em.createQuery("DELETE FROM Payments WHERE id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();

            return delete > 0;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public boolean deleteAccountingById(Long id) {
        EntityTransaction tx = null;

        try (EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            tx = em.getTransaction();
            tx.begin();
            int delete = em.createQuery("DELETE FROM Accounting WHERE id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            tx.commit();

            return delete > 0;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }
}
