package org.example.fintect.schedular;

import lombok.RequiredArgsConstructor;
import org.example.fintect.statement.Statement;
import org.example.fintect.statement.StatementRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailySummaryService {
    private final StatementRepository statementRepository;
    private final DailySummaryRepository dailySummaryRepository;

    // ✅ Scheduler
    @Scheduled(cron = "0 4 0 * * *")
    public void dailyAccountSummary() {

        LocalDate targetDate = LocalDate.now().minusDays(1);

        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.plusDays(1).atStartOfDay();

        List<Statement> statementList = statementRepository.findAllByCreatedAtBetween(start, end);

        List<DailyAccountSummary> dailyAccountSummaryList = new ArrayList<>();

        for (Statement statement : statementList) {

            if (statement.getAccount() == null)
                continue;

            String accountNo = statement.getAccount().getAccountNo();

            // 🔍 find existing
            DailyAccountSummary existing = null;

            for (DailyAccountSummary summary : dailyAccountSummaryList) {
                if (summary.getAccountNo().equals(accountNo)) {
                    existing = summary;
                    break;
                }
            }

            BigDecimal debit = statement.getDebit() != null ? statement.getDebit() : BigDecimal.ZERO;

            BigDecimal credit = statement.getCredit() != null ? statement.getCredit() : BigDecimal.ZERO;

            if (existing != null) {
                //update
                existing.setTotalDebit(existing.getTotalDebit().add(debit));
                existing.setTotalCredit(existing.getTotalCredit().add(credit));
            } else {
                //create new
                DailyAccountSummary newSummary = new DailyAccountSummary();

                newSummary.setAccountNo(accountNo);
                newSummary.setDate(targetDate);
                newSummary.setTotalDebit(debit);
                newSummary.setTotalCredit(credit);

                dailyAccountSummaryList.add(newSummary);
            }
        }

        //calculate net
        for (DailyAccountSummary summary : dailyAccountSummaryList) {

            BigDecimal debit = summary.getTotalDebit() != null ? summary.getTotalDebit() : BigDecimal.ZERO;

            BigDecimal credit = summary.getTotalCredit() != null ? summary.getTotalCredit() : BigDecimal.ZERO;

            summary.setNetAmount(credit.subtract(debit));
            summary.setCreatedAt(LocalDateTime.now());
        }

        dailySummaryRepository.saveAll(dailyAccountSummaryList);
    }


}
