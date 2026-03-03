package org.example.fintect.account;

import lombok.RequiredArgsConstructor;
import org.example.fintect.account.model.AccountReqModel;
import org.example.fintect.account.utils.AccountEndPointUtils;
import org.example.fintect.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping(AccountEndPointUtils.CREATE_ACCOUNT)
    public ResponseEntity<ApiResponse> createAccount(@RequestBody AccountReqModel accountReqModel) {
        return new ResponseEntity<>(accountService.createAccount(accountReqModel), HttpStatus.OK);
    }

    @GetMapping(AccountEndPointUtils.GET_ACCOUNT)
    public ResponseEntity<ApiResponse> getAccount(@RequestParam (required = false) String fullName,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(required = false) String phone,
                                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return new ResponseEntity<>(accountService.fetchAccount(fullName,email,phone,page,size),HttpStatus.OK);
    }

    //fdskdsjjsdio

}
