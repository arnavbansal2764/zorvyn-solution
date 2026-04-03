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

    /**
     * Monthly trend breakdown.
     * Returns rows of: [period (String, YYYY-MM), type (TransactionType), sum (BigDecimal), count (Long)]
     */
    @Query("SELECT FUNCTION('TO_CHAR', t.date, 'YYYY-MM'), t.type, COALESCE(SUM(t.amount), 0), COUNT(t) " +
           "FROM Transaction t " +
           "WHERE t.date BETWEEN :startDate AND :endDate " +
           "GROUP BY FUNCTION('TO_CHAR', t.date, 'YYYY-MM'), t.type " +
           "ORDER BY FUNCTION('TO_CHAR', t.date, 'YYYY-MM')")
    List<Object[]> sumGroupedByMonthAndType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /** Most recent N transactions ordered by date then id descending. */
    @Query("SELECT t FROM Transaction t ORDER BY t.date DESC, t.id DESC")
    List<Transaction> findRecentTransactions(Pageable pageable);
}
