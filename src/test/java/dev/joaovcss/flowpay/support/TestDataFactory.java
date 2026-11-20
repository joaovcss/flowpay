package dev.joaovcss.flowpay.support;

import dev.joaovcss.flowpay.domain.account.AccountType;
import dev.joaovcss.flowpay.domain.account.BankAccount;
import dev.joaovcss.flowpay.domain.card.CreditCard;
import dev.joaovcss.flowpay.domain.card.Invoice;
import dev.joaovcss.flowpay.domain.card.InvoiceStatus;
import dev.joaovcss.flowpay.domain.transaction.Transaction;
import dev.joaovcss.flowpay.domain.transaction.TransactionType;
import dev.joaovcss.flowpay.domain.transaction.Transfer;
import dev.joaovcss.flowpay.domain.user.User;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestDataFactory {
    private final EntityManager entityManager;

    public TestDataFactory(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User createUser(String email) {
        User user = new User();
        user.setName("Usuário de teste");
        user.setEmail(email);
        user.setPasswordHash("hash-de-teste");
        entityManager.persist(user);
        return user;
    }

    public BankAccount createAccount(User user, String name) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setName(name);
        account.setInstitution("Banco de teste");
        account.setType(AccountType.CHECKING);
        entityManager.persist(account);
        return account;
    }

    public CreditCard createCreditCard(User user, String name, String institution, BigDecimal creditLimit, Short closingDay, Short dueDay) {
        CreditCard creditCard = new CreditCard();
        creditCard.setUser(user);
        creditCard.setName(name);
        creditCard.setInstitution(institution);
        creditCard.setCreditLimit(creditLimit);
        creditCard.setClosingDay(closingDay);
        creditCard.setDueDay(dueDay);
        entityManager.persist(creditCard);
        return creditCard;
    }

    public Invoice createInvoice(User user, CreditCard card, LocalDate referenceMonth, LocalDate closingDate, LocalDate dueDate, InvoiceStatus status) {
        Invoice invoice = new Invoice();
        invoice.setUser(user);
        invoice.setCard(card);
        invoice.setReferenceMonth(referenceMonth);
        invoice.setClosingDate(closingDate);
        invoice.setDueDate(dueDate);
        invoice.setStatus(status);
        entityManager.persist(invoice);
        return invoice;
    }

    public Transfer createTransfer(User user) {
        Transfer transfer = new Transfer();
        transfer.setUser(user);
        entityManager.persist(transfer);
        return transfer;
    }

    public Transaction createTransaction(User user, BankAccount account, TransactionType type, String amount, Invoice invoice, Transfer transfer) {
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setDescription(type.name());
        transaction.setOccurredOn(LocalDate.of(2026, 11, 10));
        transaction.setInvoice(invoice);
        transaction.setTransfer(transfer);
        entityManager.persist(transaction);
        return transaction;
    }
}
