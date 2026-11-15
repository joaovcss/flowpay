package dev.joaovcss.flowpay.domain.card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface InstallmentRepository extends JpaRepository<Installment, UUID> {
    @Query(value = """
        SELECT COALESCE(SUM(amount), 0) 
        FROM installments
        WHERE invoice_id = :invoiceId
    """, nativeQuery = true)
    BigDecimal sumAmountByInvoiceId(@Param("invoiceId") UUID invoiceId);

    @Query(value = """
        SELECT COALESCE(SUM(ins.amount), 0)
        FROM installments ins
        JOIN invoices inv ON ins.invoice_id = inv.id
        WHERE inv.status <> 'PAID' AND inv.card_id = :cardId
    """, nativeQuery = true)
    BigDecimal sumUnpaidAmountByCardId(@Param("cardId") UUID cardId);
}
