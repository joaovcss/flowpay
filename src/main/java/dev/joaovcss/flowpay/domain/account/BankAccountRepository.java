package dev.joaovcss.flowpay.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BankAccountRepository extends JpaRepository<BankAccount, UUID> {
    Optional<BankAccount> findByIdAndUserId(UUID id, UUID userId);

    List<BankAccount> findAllByUserIdAndArchivedAtIsNullOrderByNameAsc(UUID userId);
}
