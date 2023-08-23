package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(unique = true)
    private String accountId;

    @Column(unique = true)
    private String username;

    private String sponsorId;

    private String uplineId;

    private String position;

    private String level;

    private String leftDownlineId;

    private String rightDownlineId;

    private String userType;

}
