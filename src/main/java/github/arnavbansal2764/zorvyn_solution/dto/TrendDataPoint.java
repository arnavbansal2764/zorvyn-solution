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
public class TrendDataPoint {

    /**
     * Label for the time period.
     * Monthly: "2026-01", "2026-02", etc.
     * Weekly:  "2026-W01", "2026-W02", etc.
     */
    private String period;

    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal net;
    private long transactionCount;
}
