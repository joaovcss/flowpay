package dev.joaovcss.flowpay.domain.income;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecurringIncomeRepository extends JpaRepository<RecurringIncome, UUID> {
    Optional<RecurringIncome> findByIdAndUserId(UUID id, UUID userId);

    List<RecurringIncome> findAllByUserIdAndEndDateIsNull(UUID userId);
}
