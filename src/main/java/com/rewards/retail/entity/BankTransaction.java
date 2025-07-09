package com.rewards.retail.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

import java.time.LocalDate;
@Entity
@Table(name="bank_transaction")
@Getter
@Setter
@Builder
public class BankTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private int amount;
    private LocalDate transactionDate;


}
