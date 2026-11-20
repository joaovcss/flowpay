package dev.joaovcss.flowpay.domain.card;

import dev.joaovcss.flowpay.domain.user.User;
import dev.joaovcss.flowpay.support.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class InstallmentRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    InstallmentRepository installmentRepository;

    TestDataFactory data;

    @BeforeEach
    void setUp() {
        data = new TestDataFactory(entityManager);
    }

    @Test
    @DisplayName("Should sum only the installments of the given invoice")
    void sumAmountByInvoiceId() {
        User user = data.createUser("joao@example.com");
        CreditCard card = data.createCreditCard(user, "Cartão de teste", "Banco de teste", new BigDecimal("5000.00"), (short) 10, (short) 20);
        List<Invoice> invoices = createMonthlyInvoices(user, card, 12);

        CardPurchase purchase12x = data.createCardPurchase(user, card, "3000.00", 12);
        for (int i = 0; i < 12; i++) {
            data.createInstallment(user, purchase12x, invoices.get(i), i + 1, "250.00");
        }

        CardPurchase purchase6x = data.createCardPurchase(user, card, "600.00", 6);
        for (int i = 0; i < 6; i++) {
            data.createInstallment(user, purchase6x, invoices.get(i), i + 1, "100.00");
        }
        entityManager.flush();

        BigDecimal firstInvoice = installmentRepository.sumAmountByInvoiceId(invoices.get(0).getId());
        BigDecimal seventhInvoice = installmentRepository.sumAmountByInvoiceId(invoices.get(6).getId());

        assertThat(firstInvoice).isEqualByComparingTo("350.00");
        assertThat(seventhInvoice).isEqualByComparingTo("250.00");
    }

    @Test
    @DisplayName("Should return zero for an invoice without installments")
    void sumAmountByInvoiceIdReturnsZeroWithoutInstallments() {
        User user = data.createUser("joao@example.com");
        CreditCard card = data.createCreditCard(user, "Cartão de teste", "Banco de teste", new BigDecimal("5000.00"), (short) 10, (short) 20);
        Invoice invoice = createMonthlyInvoices(user, card, 1).get(0);
        entityManager.flush();

        BigDecimal result = installmentRepository.sumAmountByInvoiceId(invoice.getId());

        assertThat(result).isEqualByComparingTo("0");
    }

    @Test
    @DisplayName("Should sum installments of open and closed invoices of only one card")
    void sumUnpaidAmountByCardId() {
        User user = data.createUser("joao@example.com");
        CreditCard card = data.createCreditCard(user, "Cartão de teste", "Banco de teste", new BigDecimal("5000.00"), (short) 10, (short) 20);
        CreditCard otherCard = data.createCreditCard(user, "Outro cartão", "Banco de teste", new BigDecimal("5000.00"), (short) 10, (short) 20);

        List<Invoice> invoices = createMonthlyInvoices(user, card, 3);
        invoices.get(0).setStatus(InvoiceStatus.PAID);
        invoices.get(0).setPaidAt(OffsetDateTime.now());
        invoices.get(1).setStatus(InvoiceStatus.CLOSED);
        Invoice otherCardInvoice = createMonthlyInvoices(user, otherCard, 1).get(0);

        CardPurchase purchase = data.createCardPurchase(user, card, "300.00", 3);
        for (int i = 0; i < 3; i++) {
            data.createInstallment(user, purchase, invoices.get(i), i + 1, "100.00");
        }
        CardPurchase otherPurchase = data.createCardPurchase(user, otherCard, "50.00", 1);
        data.createInstallment(user, otherPurchase, otherCardInvoice, 1, "50.00");
        entityManager.flush();

        BigDecimal result = installmentRepository.sumUnpaidAmountByCardId(card.getId());

        assertThat(result).isEqualByComparingTo("200.00");
    }

    private List<Invoice> createMonthlyInvoices(User user, CreditCard card, int months) {
        List<Invoice> invoices = new ArrayList<>();
        for (int i = 0; i < months; i++) {
            LocalDate referenceMonth = LocalDate.of(2026, 1, 1).plusMonths(i);
            invoices.add(data.createInvoice(user, card, referenceMonth,
                    referenceMonth.withDayOfMonth(10), referenceMonth.withDayOfMonth(20), InvoiceStatus.OPEN));
        }
        return invoices;
    }
}
