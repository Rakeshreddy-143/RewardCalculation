package com.rewards.retail.repository;

import com.rewards.retail.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDate from, LocalDate to);
}
