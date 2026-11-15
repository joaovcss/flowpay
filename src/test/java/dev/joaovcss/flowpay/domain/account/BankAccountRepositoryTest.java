package dev.joaovcss.flowpay.domain.account;

import dev.joaovcss.flowpay.domain.user.User;
import jakarta.persistence.EntityManager;
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

    @Autowired
    BankAccountRepository bankAccountRepository;


    @Test
    @DisplayName("Should find a bank account by id and user id")
    void findByIdAndUserIdSuccess() {
        User user = createUser("teste@teste.com");
        BankAccount account = createAccount(user, "Conta de teste");

        Optional<BankAccount> result = bankAccountRepository.findByIdAndUserId(account.getId(), user.getId());

        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Should not find a bank account from another user")
    void findByIdAndUserIdReturnsEmptyForAnotherUser() {
        User user = createUser("teste@teste.com");
        User user2 = createUser("teste2@teste.com");
        BankAccount account = createAccount(user, "Conta de teste");

        Optional<BankAccount> result = bankAccountRepository.findByIdAndUserId(account.getId(), user2.getId());

        assertThat(result).isEmpty();
    }

    private User createUser(String email) {
        User user = new User();
        user.setName("Usuário de teste");
        user.setEmail(email);
        user.setPasswordHash("hash-de-teste");
        entityManager.persist(user);
        return user;
    }

    private BankAccount createAccount(User user, String name) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setName(name);
        account.setInstitution("Banco de teste");
        account.setType(AccountType.CHECKING);
        entityManager.persist(account);
        return account;
    }
}