package github.arnavbansal2764.zorvyn_solution.dto;

import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionUpdateRequest {

    @PositiveOrZero(message = "Amount must be zero or positive")
    private BigDecimal amount;
    
    private TransactionType type;
    
    private String category;
    
    private LocalDate date;
    
    private String notes;
}
