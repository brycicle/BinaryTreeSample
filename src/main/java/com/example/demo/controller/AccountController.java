package com.example.demo.controller;

import com.example.demo.dto.AccountRequest;
import com.example.demo.dto.AccountResponse;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody final AccountRequest request) {
        return ResponseEntity.ok(accountService.saveAccount(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AccountResponse> activateAccount(@PathVariable final Long id) {
        return ResponseEntity.ok(accountService.activateAccount(id));
    }

    @GetMapping("/downlines/{id}")
    public ResponseEntity<List<AccountResponse>> getDownlines(@PathVariable final Long id) {
        return ResponseEntity.ok(accountService.getDownlines(id));
    }
}
