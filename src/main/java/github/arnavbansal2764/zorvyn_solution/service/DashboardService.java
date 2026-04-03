package github.arnavbansal2764.zorvyn_solution.service;

import github.arnavbansal2764.zorvyn_solution.dto.CategorySummaryResponse;
import github.arnavbansal2764.zorvyn_solution.dto.DashboardSummaryResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TrendDataPoint;
import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import github.arnavbansal2764.zorvyn_solution.repository.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public DashboardSummaryResponse getSummary() {
        BigDecimal totalIncome = transactionRepository.sumByType(TransactionType.INCOME);
        BigDecimal totalExpenses = transactionRepository.sumByType(TransactionType.EXPENSE);
        long incomeCount = transactionRepository.countByType(TransactionType.INCOME);
        long expenseCount = transactionRepository.countByType(TransactionType.EXPENSE);

        return DashboardSummaryResponse.builder()
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome.subtract(totalExpenses))
                .totalTransactions(incomeCount + expenseCount)
                .incomeCount(incomeCount)
                .expenseCount(expenseCount)
                .build();
    }

    public List<CategorySummaryResponse> getCategoryTotals() {
        List<Object[]> rawData = transactionRepository.sumGroupedByCategoryAndType();
        Map<String, CategorySummaryResponse> categoryMap = new LinkedHashMap<>();

        for (Object[] row : rawData) {
            String category = (String) row[0];
            TransactionType type = (TransactionType) row[1];
            BigDecimal sum = (BigDecimal) row[2];
            long count = (Long) row[3];

            CategorySummaryResponse summary = categoryMap.computeIfAbsent(category,
                    k -> CategorySummaryResponse.builder()
                            .category(k)
                            .totalIncome(BigDecimal.ZERO)
                            .totalExpenses(BigDecimal.ZERO)
                            .net(BigDecimal.ZERO)
                            .transactionCount(0)
                            .build());

            if (type == TransactionType.INCOME) {
                summary.setTotalIncome(summary.getTotalIncome().add(sum));
            } else {
                summary.setTotalExpenses(summary.getTotalExpenses().add(sum));
            }
            summary.setTransactionCount(summary.getTransactionCount() + count);
            summary.setNet(summary.getTotalIncome().subtract(summary.getTotalExpenses()));
        }

        return new ArrayList<>(categoryMap.values());
    }

    public List<TransactionResponse> getRecentActivity(int limit) {
        List<Transaction> recent = transactionRepository.findRecentTransactions(PageRequest.of(0, limit));
        return recent.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<TrendDataPoint> getMonthlyTrends(LocalDate startDate, LocalDate endDate) {
        // Fallback dates if not provided: last 6 months
        LocalDate start = startDate != null ? startDate : LocalDate.now().minusMonths(6).withDayOfMonth(1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        List<Transaction> transactions = transactionRepository.findByDateBetween(start, end);

        // Group by Year-Month
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, TrendDataPoint> trendsMap = new TreeMap<>();

        for (Transaction t : transactions) {
            String period = t.getDate().format(formatter);

            TrendDataPoint point = trendsMap.computeIfAbsent(period,
                    k -> TrendDataPoint.builder()
                            .period(k)
                            .totalIncome(BigDecimal.ZERO)
                            .totalExpenses(BigDecimal.ZERO)
                            .net(BigDecimal.ZERO)
                            .transactionCount(0)
                            .build());

            if (t.getType() == TransactionType.INCOME) {
                point.setTotalIncome(point.getTotalIncome().add(t.getAmount()));
            } else {
                point.setTotalExpenses(point.getTotalExpenses().add(t.getAmount()));
            }
            point.setTransactionCount(point.getTransactionCount() + 1);
            point.setNet(point.getTotalIncome().subtract(point.getTotalExpenses()));
        }

        return new ArrayList<>(trendsMap.values());
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .amount(t.getAmount())
                .type(t.getType())
                .category(t.getCategory())
                .date(t.getDate())
                .notes(t.getNotes())
                .build();
    }
}
