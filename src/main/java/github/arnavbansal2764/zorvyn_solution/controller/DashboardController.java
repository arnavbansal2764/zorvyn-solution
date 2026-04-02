package github.arnavbansal2764.zorvyn_solution.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/data")
    public String getDashboardData() {
        return "Dashboard Data: Accessible by Viewer, Analyst, and Admin.";
    }
}
