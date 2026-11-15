package dev.joaovcss.flowpay.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {
    Optional<CreditCard> findByIdAndUserId(UUID id, UUID userId);

    List<CreditCard> findAllByUserIdAndArchivedAtIsNullOrderByNameAsc(UUID userId);
}
