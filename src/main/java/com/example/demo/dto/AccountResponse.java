package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AccountResponse {
    private Long id;

    private String accountId;

    private String username;

    private String sponsorId;

    private String uplineId;

    private String position;

    private String level;

    private String leftDownlineId;

    private String rightDownlineId;

    private String userType;

}
