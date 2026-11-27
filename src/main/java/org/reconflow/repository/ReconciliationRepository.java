package org.reconflow.repository;

import org.reconflow.models.ReconciliationRun;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ReconciliationRepository {

    ReconciliationRun save(ReconciliationRun run);
    List<ReconciliationRun> findAll();
    Optional<ReconciliationRun> findById();
    Optional<ReconciliationRun> findLatest();
    boolean deleteById(Long id);
}
