package github.arnavbansal2764.zorvyn_solution.repository;

import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    // ── Basic Filters ────────────────────────────────────────────────────────

    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByCategory(String category);
    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Transaction> findByTypeAndCategory(TransactionType type, String category);
    List<Transaction> findByCategoryAndDateBetween(String category, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByTypeAndDateBetween(TransactionType type, LocalDate startDate, LocalDate endDate);
    List<Transaction> findByTypeAndCategoryAndDateBetween(TransactionType type, String category, LocalDate startDate, LocalDate endDate);

    // ── Dashboard Aggregations ────────────────────────────────────────────────

    /** Total amount for a given transaction type. Returns 0 if no records. */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    BigDecimal sumByType(@Param("type") TransactionType type);

    /** Count of transactions for a given type. */
    long countByType(TransactionType type);

    /**
     * Category-level breakdown.
     * Returns rows of: [category (String), type (TransactionType), sum (BigDecimal), count (Long)]
     */
    @Query("SELECT t.category, t.type, COALESCE(SUM(t.amount), 0), COUNT(t) " +
           "FROM Transaction t " +
           "GROUP BY t.category, t.type " +
           "ORDER BY t.category")
    List<Object[]> sumGroupedByCategoryAndType();

    /** Most recent N transactions ordered by date then id descending. */
    @Query("SELECT t FROM Transaction t ORDER BY t.date DESC, t.id DESC")
    List<Transaction> findRecentTransactions(Pageable pageable);
}
