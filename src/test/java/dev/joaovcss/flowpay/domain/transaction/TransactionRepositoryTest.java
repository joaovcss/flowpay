package dev.joaovcss.flowpay.domain.transaction;

import dev.joaovcss.flowpay.domain.account.BankAccount;
import dev.joaovcss.flowpay.domain.card.CreditCard;
import dev.joaovcss.flowpay.domain.card.Invoice;
import dev.joaovcss.flowpay.domain.card.InvoiceStatus;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TransactionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    TestDataFactory data;

    @BeforeEach
    void setUp() {
        data = new TestDataFactory(entityManager);
    }

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    @DisplayName("Should add incomes and subtract expenses of only one account")
    void sumMovementsByAccountIdAndUserId() {
        User user = data.createUser("joao@example.com");
        BankAccount account = data.createAccount(user, "Conta de teste");
        BankAccount account2 = data.createAccount(user, "Conta de teste 2");
        CreditCard creditCard = data.createCreditCard(user, "Cartão de teste", "Banco de teste", new BigDecimal("1000.00"), (short) 15, (short) 25);
        Invoice invoice = data.createInvoice(user, creditCard, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 25), InvoiceStatus.OPEN);
        Transfer transfer = data.createTransfer(user);
        Transfer transfer2 = data.createTransfer(user);

        data.createTransaction(user, account, TransactionType.INCOME, "50.00", null, null);
        data.createTransaction(user, account, TransactionType.EXPENSE, "30.00", null, null);
        data.createTransaction(user, account, TransactionType.INVOICE_PAYMENT, "20.00", invoice, null);
        data.createTransaction(user, account, TransactionType.TRANSFER_IN, "40.00", null, transfer);
        data.createTransaction(user, account, TransactionType.TRANSFER_OUT, "10.00", null, transfer2);

        data.createTransaction(user, account2, TransactionType.TRANSFER_OUT, "40.00", null, transfer);
        data.createTransaction(user, account2, TransactionType.TRANSFER_IN, "10.00", null, transfer2);
        entityManager.flush();

        BigDecimal result = transactionRepository.sumMovementsByAccountIdAndUserId(account.getId(), user.getId());

        assertThat(result).isEqualByComparingTo("30.00");
    }

    @Test
    @DisplayName("Should return zero for an account without movements")
    void sumMovementsByAccountIdAndUserIdReturnsZeroWithoutMovements() {
        User user = data.createUser("joao@example.com");
        BankAccount account = data.createAccount(user, "Conta de teste");
        entityManager.flush();

        BigDecimal result = transactionRepository.sumMovementsByAccountIdAndUserId(account.getId(), user.getId());

        assertThat(result).isEqualByComparingTo("0");
    }
}
