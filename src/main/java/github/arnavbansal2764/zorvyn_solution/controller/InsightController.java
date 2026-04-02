package github.arnavbansal2764.zorvyn_solution.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/insights")
public class InsightController {

    @GetMapping("/access")
    public String getInsights() {
        return "Insights Access: Restricted to Analyst and Admin.";
    }
}
