package github.arnavbansal2764.zorvyn_solution.service;

import github.arnavbansal2764.zorvyn_solution.dto.TransactionRequest;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionUpdateRequest;
import github.arnavbansal2764.zorvyn_solution.model.Transaction;
import github.arnavbansal2764.zorvyn_solution.model.TransactionType;
import github.arnavbansal2764.zorvyn_solution.repository.TransactionRepository;
import github.arnavbansal2764.zorvyn_solution.exception.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return toResponse(transaction);
    }

    // ── Filter ─────────────────────────────────────────────────────────────────

    public List<TransactionResponse> filter(TransactionType type, String category,
                                            LocalDate startDate, LocalDate endDate) {
        Specification<Transaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }
            if (category != null && !category.isBlank()) {
                predicates.add(cb.equal(root.get("category"), category));
            }
            if (startDate != null && endDate != null) {
                predicates.add(cb.between(root.get("date"), startDate, endDate));
            } else if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), startDate));
            } else if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), endDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return transactionRepository.findAll(spec).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Update ──────────────────────────────────────────────────────────────────

    public TransactionResponse update(Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

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
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
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
