package github.arnavbansal2764.zorvyn_solution.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategorySummaryResponse {

    private String category;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal net;
    private long transactionCount;
}
