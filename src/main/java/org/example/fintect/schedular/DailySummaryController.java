package org.example.fintect.schedular;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DailySummaryController {

    private final DailySummaryService dailySummaryService;

    @PostMapping("/run")
    public String runSummary() {
        dailySummaryService.dailyAccountSummary();
        return "Daily summary executed successfully!";
    }
}
