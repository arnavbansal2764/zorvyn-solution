package github.arnavbansal2764.zorvyn_solution.controller;

import github.arnavbansal2764.zorvyn_solution.dto.TransactionRequest;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionUpdateRequest;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import github.arnavbansal2764.zorvyn_solution.service.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * POST /api/transactions
     * Create a new transaction. Admin only.
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.create(request));
    }

    /**
     * GET /api/transactions
     * Retrieve all transactions. Analyst and Admin.
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    /**
     * GET /api/transactions/{id}
     * Retrieve a single transaction by ID. Analyst and Admin.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    /**
     * GET /api/transactions/filter
     * Filter transactions by optional query parameters: type, category, startDate,
     * endDate.
     * Analyst and Admin.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponse>> filter(
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(transactionService.filter(type, category, startDate, endDate));
    }

    /**
     * PUT /api/transactions/{id}
     * Update an existing transaction. Admin only.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> update(@PathVariable Long id,
            @Valid @RequestBody TransactionUpdateRequest request) {
        return ResponseEntity.ok(transactionService.update(id, request));
    }

    /**
     * DELETE /api/transactions/{id}
     * Delete a transaction. Admin only.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
