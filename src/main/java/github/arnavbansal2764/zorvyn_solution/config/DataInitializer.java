package github.arnavbansal2764.zorvyn_solution.config;

import github.arnavbansal2764.zorvyn_solution.model.Role;
import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import github.arnavbansal2764.zorvyn_solution.model.User;
import github.arnavbansal2764.zorvyn_solution.repository.TransactionRepository;
import github.arnavbansal2764.zorvyn_solution.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, 
                           TransactionRepository transactionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Seed Users
            if (userRepository.count() == 0) {
                userRepository.save(User.builder().username("admin").password(passwordEncoder.encode("admin123")).role(Role.ADMIN).build());
                userRepository.save(User.builder().username("analyst").password(passwordEncoder.encode("analyst123")).role(Role.ANALYST).build());
                userRepository.save(User.builder().username("viewer").password(passwordEncoder.encode("viewer123")).role(Role.VIEWER).build());
                System.out.println("Default users seeded successfully.");
            }

            // Seed Sample Transactions
            if (transactionRepository.count() == 0) {
                LocalDate now = LocalDate.now();
                
                // --- Income Samples ---
                saveTransaction(6500.00, TransactionType.INCOME, "Salary", now.minusMonths(2), "Previous month salary");
                saveTransaction(6500.00, TransactionType.INCOME, "Salary", now.minusMonths(1), "Last month salary");
                saveTransaction(200.00, TransactionType.INCOME, "Dividends", now.minusDays(15), "Stock dividends");
                saveTransaction(50.00, TransactionType.INCOME, "Gift", now.minusDays(5), "Birthday gift");

                // --- Expense Samples ---
                saveTransaction(1200.00, TransactionType.EXPENSE, "Rent", now.minusMonths(2), "Feb Rent");
                saveTransaction(1200.00, TransactionType.EXPENSE, "Rent", now.minusMonths(1), "March Rent");
                saveTransaction(450.00, TransactionType.EXPENSE, "Groceries", now.minusDays(20), "Monthly groceries");
                saveTransaction(80.50, TransactionType.EXPENSE, "Utilities", now.minusDays(18), "Electric bill");
                saveTransaction(120.00, TransactionType.EXPENSE, "Dining", now.minusDays(12), "Dinner with friends");
                saveTransaction(60.00, TransactionType.EXPENSE, "Transport", now.minusDays(8), "Fuel fill-up");
                saveTransaction(95.00, TransactionType.EXPENSE, "Shopping", now.minusDays(2), "New clothes");

                System.out.println("Sample transactions seeded successfully.");
            }
        };
    }

    private void saveTransaction(double amount, TransactionType type, String category, LocalDate date, String notes) {
        transactionRepository.save(Transaction.builder()
                .amount(BigDecimal.valueOf(amount))
                .type(type)
                .category(category)
                .date(date)
                .notes(notes)
                .build());
    }
}
