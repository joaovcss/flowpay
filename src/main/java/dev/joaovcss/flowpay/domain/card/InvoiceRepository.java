package dev.joaovcss.flowpay.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Optional<Invoice> findByCardIdAndReferenceMonth(UUID cardId, LocalDate referenceMonth);

    Optional<Invoice> findByIdAndUserId(UUID id, UUID userId);
}
