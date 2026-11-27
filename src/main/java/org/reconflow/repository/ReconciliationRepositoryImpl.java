package org.reconflow.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.reconflow.config.EntityManagerUtil;
import org.reconflow.models.ReconciliationResult;
import org.reconflow.models.ReconciliationRun;

import java.util.List;
import java.util.Optional;

public class ReconciliationRepositoryImpl implements ReconciliationRepository {

    @Override
    public ReconciliationRun save(ReconciliationRun run) {
        EntityManager em = EntityManagerUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(run);
            for (ReconciliationResult result : run.getResults()) {
                em.persist(result);
            }
            tx.commit();
            return run;
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
    public Optional<ReconciliationRun> findLatest() {
        try (EntityManager em =
                     EntityManagerUtil.getEntityManagerFactory().createEntityManager()) {
            Optional<Long> latestId = em.createQuery(
                            "SELECT r.id " +
                                    "FROM ReconciliationRun r " +
                                    "ORDER BY r.createdAt DESC, r.id DESC",
                            Long.class
                    ).setMaxResults(1)
                    .getResultStream()
                    .findFirst();

            if (latestId.isEmpty()) {
                return Optional.empty();
            }

            return em.createQuery(
                            "SELECT DISTINCT r " +
                                    "FROM ReconciliationRun r " +
                                    "LEFT JOIN FETCH r.results " +
                                    "WHERE r.id = :id",
                            ReconciliationRun.class
                    ).setParameter("id", latestId.get())
                    .getResultStream()
                    .findFirst();
        }
    }

    @Override
    public List<ReconciliationRun> findAll() {
        return List.of();
    }

    @Override
    public Optional<ReconciliationRun> findById() {
        return Optional.empty();
    }



    @Override
    public boolean deleteById(Long id) {
        return false;
    }
}
