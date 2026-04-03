package github.arnavbansal2764.zorvyn_solution.service;

import github.arnavbansal2764.zorvyn_solution.dto.TransactionRequest;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionResponse;
import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import github.arnavbansal2764.zorvyn_solution.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // ── Create ──────────────────────────────────────────────────────────────────

    public TransactionResponse create(TransactionRequest request) {
        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate() != null ? request.getDate() : LocalDate.now())
                .notes(request.getNotes())
                .build();
        return toResponse(transactionRepository.save(transaction));
    }

    // ── Read ─────────────────────────────────────────────────────────────────────

    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionResponse getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
        return toResponse(transaction);
    }

    // ── Filter ─────────────────────────────────────────────────────────────────

    public List<TransactionResponse> filter(TransactionType type, String category,
                                            LocalDate startDate, LocalDate endDate) {
        List<Transaction> results;

        boolean hasType = type != null;
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasDateRange = startDate != null && endDate != null;

        if (hasType && hasCategory && hasDateRange) {
            results = transactionRepository.findByTypeAndCategoryAndDateBetween(type, category, startDate, endDate);
        } else if (hasType && hasCategory) {
            results = transactionRepository.findByTypeAndCategory(type, category);
        } else if (hasType && hasDateRange) {
            results = transactionRepository.findByTypeAndDateBetween(type, startDate, endDate);
        } else if (hasCategory && hasDateRange) {
            results = transactionRepository.findByCategoryAndDateBetween(category, startDate, endDate);
        } else if (hasType) {
            results = transactionRepository.findByType(type);
        } else if (hasCategory) {
            results = transactionRepository.findByCategory(category);
        } else if (hasDateRange) {
            results = transactionRepository.findByDateBetween(startDate, endDate);
        } else {
            results = transactionRepository.findAll();
        }

        return results.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Update ──────────────────────────────────────────────────────────────────

    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (request.getAmount() != null) transaction.setAmount(request.getAmount());
        if (request.getType() != null) transaction.setType(request.getType());
        if (request.getCategory() != null) transaction.setCategory(request.getCategory());
        if (request.getDate() != null) transaction.setDate(request.getDate());
        if (request.getNotes() != null) transaction.setNotes(request.getNotes());

        return toResponse(transactionRepository.save(transaction));
    }

    // ── Delete ──────────────────────────────────────────────────────────────────

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    // ── Mapper ──────────────────────────────────────────────────────────────────

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
