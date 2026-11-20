package dev.joaovcss.flowpay.domain.account;

import dev.joaovcss.flowpay.domain.user.User;
import dev.joaovcss.flowpay.support.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BankAccountRepositoryTest {

    @Autowired
    EntityManager entityManager;

    TestDataFactory data;

    @Autowired
    BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        data = new TestDataFactory(entityManager);
    }

    @Test
    @DisplayName("Should find a bank account by id and user id")
    void findByIdAndUserIdSuccess() {
        User user = data.createUser("teste@teste.com");
        BankAccount account = data.createAccount(user, "Conta de teste");

        Optional<BankAccount> result = bankAccountRepository.findByIdAndUserId(account.getId(), user.getId());

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Should not find a bank account from another user")
    void findByIdAndUserIdReturnsEmptyForAnotherUser() {
        User user = data.createUser("teste@teste.com");
        User user2 = data.createUser("teste2@teste.com");
        BankAccount account = data.createAccount(user, "Conta de teste");

        Optional<BankAccount> result = bankAccountRepository.findByIdAndUserId(account.getId(), user2.getId());

        assertThat(result).isEmpty();
    }
}