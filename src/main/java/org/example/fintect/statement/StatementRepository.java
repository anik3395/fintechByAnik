package org.example.fintect.statement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
