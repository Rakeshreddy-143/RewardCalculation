package com.rewards.retail.repository;

import com.rewards.retail.entity.BankTransaction;
import org.hibernate.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {
    @Query("""
           select coalesce(sum(t.amount), 0)
           from BankTransaction t
           where t.personId = :personId
             and t.transactionDate between :start and :end
           """)
    int totalSpentInMonth(@Param("personId") Long personId,
                                 @Param("start") LocalDate start,
                                 @Param("end") LocalDate end);
}
