package dev.joaovcss.flowpay.domain.subscription;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.UUID;

public interface SubscriptionChargeRepository extends JpaRepository<SubscriptionCharge, UUID> {
    boolean existsBySubscriptionIdAndChargeDate(UUID subscriptionId, LocalDate chargeDate);
}
