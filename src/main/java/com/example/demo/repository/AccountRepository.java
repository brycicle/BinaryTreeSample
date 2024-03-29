package com.example.demo.repository;

import com.example.demo.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<Account> findAccountByAccountIdEquals(String accountId);

    List<Account> findAllByUplineIdEquals(String uplineId);

    List<Account> findAllBySponsorIdEquals(String uplineId);
}
