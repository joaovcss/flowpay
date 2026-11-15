package dev.joaovcss.flowpay.domain.transaction;

import dev.joaovcss.flowpay.domain.income.RecurringIncome;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    @Query(value = """
        SELECT COALESCE(SUM(
        CASE
        WHEN type IN ('TRANSFER_OUT', 'EXPENSE', 'INVOICE_PAYMENT') THEN -amount
        WHEN type IN ('TRANSFER_IN', 'INCOME') THEN amount
        END
        ), 0) FROM transactions WHERE account_id = :accountId AND user_id = :userId
        """, nativeQuery = true)
    BigDecimal sumMovementsByAccountIdAndUserId(@Param("accountId") UUID accountId, @Param("userId") UUID userId);

    Page<Transaction> findAllByAccountIdAndUserIdAndOccurredOnBetween(UUID accountId, UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    List<Transaction> findTop10ByUserIdOrderByOccurredOnDescCreatedAtDesc(UUID userId);

    List<Transaction> findAllByTransferIdAndUserId(UUID transferId, UUID userId);

    boolean existsByRecurringIncomeIdAndReferenceMonth(UUID recurringIncomeId, LocalDate referenceMonth);
}

