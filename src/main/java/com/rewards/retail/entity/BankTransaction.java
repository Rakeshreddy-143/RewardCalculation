package com.rewards.retail.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name="bank_transaction")
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long personId;
    private int amount;
    private LocalDate transactionDate;


}
