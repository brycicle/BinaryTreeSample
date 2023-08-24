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
//        Retrieve account to be updated
        Account account = repository.findById(id).orElse(new Account());
//        Retreive account of sponsor and set it as the current upline
        Account upline = repository.findAccountByAccountIdEquals(account.getSponsorId())
                .orElse(Account.builder().build());
//        Create flag hasUpline and initialize as false
        boolean hasUpline = false;
//        Create list of accounts
        List<Account> downlines = new ArrayList<>();
//        Create while loop which checks if an upline has been found
        while (!hasUpline) {
//            Create an account object for the current account to be checked
//            Current account is from the downlines list or the sponsor for the 1st iteration
            Account current = downlines.stream().findFirst().orElse(upline);
//            Remove current account from the downlines list
            downlines.remove(current);
//            If the current account has no left downlineId execute the following
            if (Optional.ofNullable(current.getLeftDownlineId()).isEmpty()) {
//                Set hasUpline to true
                hasUpline = !hasUpline;
//                Replace the upline with the current account from the iteration
                upline = current;
//                Set position of account to be activated as LEFT
                account.setPosition("LEFT");
//                Set the uplines' left downline id as the current account id of the account to be activated
                upline.setLeftDownlineId(account.getAccountId());
//                Else If the current account has no right downlineId execute the following
            } else if (Optional.ofNullable(current.getRightDownlineId()).isEmpty()) {
//                Set hasUpline to true
                hasUpline = !hasUpline;
//                Replace the upline with the current account from the iteration
                upline = current;
//                Set position of account to be activated as RIGHT
                account.setPosition("RIGHT");
//                Set the uplines' right downline id as the current account id of the account to be activated
                upline.setRightDownlineId(account.getAccountId());
            }
//            After looping through the list of downlines, check if the flag above has found a free slot (left or right)
            if (!hasUpline) {
//                Add the left downline to the current list of downlines
                downlines.add(repository.findAccountByAccountIdEquals(current.getLeftDownlineId()).orElse(null));
//                Add the right downline to the current list of downlines
                downlines.add(repository.findAccountByAccountIdEquals(current.getRightDownlineId()).orElse(null));
//                If an upline has been found, execute the following
            } else {
//                Set the current upline id of the account as the account id of the found upline
                account.setUplineId(upline.getAccountId());
//                Save the current accounts' new details to db
                account = repository.save(account);
//                Save the current uplines' new details to db
                repository.save(upline);
            }
        }
//        Return response
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
