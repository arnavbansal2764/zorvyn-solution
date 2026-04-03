package github.arnavbansal2764.zorvyn_solution.controller;

import github.arnavbansal2764.zorvyn_solution.dto.CategorySummaryResponse;
import github.arnavbansal2764.zorvyn_solution.dto.DashboardSummaryResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TransactionResponse;
import github.arnavbansal2764.zorvyn_solution.dto.TrendDataPoint;
import github.arnavbansal2764.zorvyn_solution.service.DashboardService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {
        return ResponseEntity.ok(dashboardService.getSummary());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategorySummaryResponse>> getCategoryTotals() {
        return ResponseEntity.ok(dashboardService.getCategoryTotals());
    }

    @GetMapping("/recent")
    public ResponseEntity<List<TransactionResponse>> getRecentActivity(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(dashboardService.getRecentActivity(limit));
    }

    @GetMapping("/trends/monthly")
    public ResponseEntity<List<TrendDataPoint>> getMonthlyTrends(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(dashboardService.getMonthlyTrends(startDate, endDate));
    }

    @GetMapping("/data")
    public String getDashboardData() {
        return "Dashboard Data: Accessible by Viewer, Analyst, and Admin.";
    }
}
