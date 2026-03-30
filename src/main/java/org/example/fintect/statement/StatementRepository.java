package org.example.fintect.statement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long>, JpaSpecificationExecutor<Statement> {
    List<Statement> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
