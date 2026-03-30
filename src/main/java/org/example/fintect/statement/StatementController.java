package org.example.fintect.statement;

import lombok.RequiredArgsConstructor;
import org.example.fintect.response.ApiResponse;
import org.example.fintect.statement.utils.StatementEndPointUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @GetMapping(StatementEndPointUtils.GET_ALL_STATEMENTS)
    public ResponseEntity<ApiResponse> getAllStatements(
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String customerFullName,
            @RequestParam(required = false) String customerEmail,
            @RequestParam(required = false) String txId,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return new ResponseEntity<>(statementService.fetchStatements(
                accountNo,
                customerFullName,
                customerEmail,
                txId,
                fromDate,
                toDate,
                page,
                size
        ), HttpStatus.OK);
    }

}
