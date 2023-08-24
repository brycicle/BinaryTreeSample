package com.example.demo.service;

import com.example.demo.dto.AccountRequest;
import com.example.demo.dto.AccountResponse;
import com.example.demo.model.Account;

import java.util.List;

public interface AccountService {
    AccountResponse getAccount(Long id);
    AccountResponse saveAccount(AccountRequest request);

    AccountResponse activateAccount(Long id);
    List<AccountResponse> getDownlines(Long id);
}
