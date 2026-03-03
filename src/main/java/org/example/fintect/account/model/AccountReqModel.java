package org.example.fintect.account.model;

import lombok.Data;
import org.example.fintect.account.enumType.AccountType;
@Data
public class AccountReqModel {
    private String email;
    private AccountType accountType;
}
