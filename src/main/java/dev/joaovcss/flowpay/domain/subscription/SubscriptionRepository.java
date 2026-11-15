package dev.joaovcss.flowpay.domain.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByIdAndUserId(UUID id, UUID userId);

    List<Subscription> findAllByUserIdAndCanceledOnIsNull(UUID userId);
}
