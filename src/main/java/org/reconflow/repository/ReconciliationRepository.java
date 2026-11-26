package org.reconflow.repository;

import org.reconflow.models.ReconciliationRun;

import java.util.List;
import java.util.Optional;

public interface ReconciliationRepository {

    ReconciliationRun save(ReconciliationRun run);
    List<ReconciliationRun> findAll();
    Optional<ReconciliationRun> findById();
    boolean deleteById(Long id);
}
