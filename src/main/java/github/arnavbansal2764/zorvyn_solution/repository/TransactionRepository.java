package github.arnavbansal2764.zorvyn_solution.repository;

import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByCategory(String category);

    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndCategory(TransactionType type, String category);

    List<Transaction> findByCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByTypeAndCategoryAndDateBetween(TransactionType type, String category, LocalDate startDate, LocalDate endDate);
}
