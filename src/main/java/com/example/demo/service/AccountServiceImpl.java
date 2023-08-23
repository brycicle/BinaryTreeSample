package com.example.demo.service;

import com.example.demo.dto.AccountRequest;
import com.example.demo.dto.AccountResponse;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Override
    public AccountResponse getAccount(Long id) {
        Account account = repository.findById(id).orElse(new Account());

        return AccountResponse.builder()
                .id(account.getId())
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .sponsorId(account.getSponsorId())
                .uplineId(account.getUplineId())
                .position(account.getPosition())
                .level(account.getLevel())
                .leftDownlineId(account.getLeftDownlineId())
                .rightDownlineId(account.getRightDownlineId())
                .build();
    }

    @Override
    public AccountResponse saveAccount(AccountRequest request) {
        Account account = Account.builder()
                .accountId(request.getAccountId())
                .username(request.getUsername())
                .sponsorId(repository.findAccountByAccountIdEquals(request.getSponsorId())
                        .orElse(Account.builder().accountId("A0000000").build()).getAccountId())
                .level("0")
                .userType("User")
                .build();

        account = repository.save(account);

        return AccountResponse.builder()
                .id(account.getId())
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .sponsorId(account.getSponsorId())
                .uplineId(account.getUplineId())
                .position(account.getPosition())
                .level(account.getLevel())
                .leftDownlineId(account.getLeftDownlineId())
                .rightDownlineId(account.getRightDownlineId())
                .build();
    }

    @Override
    public AccountResponse activateAccount(Long id) {
        Account account = repository.findById(id).orElse(new Account());
//        7

        Account upline = repository.findAccountByAccountIdEquals(account.getSponsorId())
                .orElse(Account.builder().build());
//        1

        boolean hasUpline = false;
        List<Account> downlines = new ArrayList<>();

        while (!hasUpline) {
            Account current = downlines.stream().findFirst().orElse(upline);

            downlines.remove(current);

            if (Optional.ofNullable(current.getLeftDownlineId()).isEmpty()) {
                hasUpline = !hasUpline;
                upline = current;
                account.setPosition("LEFT");
                upline.setLeftDownlineId(account.getAccountId());
            } else if (Optional.ofNullable(current.getRightDownlineId()).isEmpty()) {
                hasUpline = !hasUpline;
                upline = current;
                account.setPosition("RIGHT");
                upline.setRightDownlineId(account.getAccountId());
            }

            if (!hasUpline) {
                downlines.add(repository.findAccountByAccountIdEquals(current.getLeftDownlineId()).orElse(null));
                downlines.add(repository.findAccountByAccountIdEquals(current.getRightDownlineId()).orElse(null));
            } else {
                account.setUplineId(upline.getAccountId());
                repository.save(account);
                repository.save(upline);
            }
        }

        return AccountResponse.builder()
                .id(account.getId())
                .accountId(account.getAccountId())
                .username(account.getUsername())
                .sponsorId(account.getSponsorId())
                .uplineId(account.getUplineId())
                .position(account.getPosition())
                .level(account.getLevel())
                .leftDownlineId(account.getLeftDownlineId())
                .rightDownlineId(account.getRightDownlineId())
                .build();
    }
}
