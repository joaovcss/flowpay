package dev.joaovcss.flowpay.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CardPurchaseRepository extends JpaRepository<CardPurchase, UUID> {
    Optional<CardPurchase> findByIdAndUserId(UUID id, UUID userId);
}
