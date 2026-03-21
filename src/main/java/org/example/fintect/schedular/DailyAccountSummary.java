package org.example.fintect.schedular;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class DailyAccountSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNo;

    private LocalDate date;

    private BigDecimal totalDebit;

    private BigDecimal totalCredit;

    private BigDecimal netAmount; // debit - credit

    private LocalDateTime createdAt;
}
