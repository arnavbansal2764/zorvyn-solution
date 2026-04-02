package github.arnavbansal2764.zorvyn_solution.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    @GetMapping("/view")
    public String viewRecords() {
        return "Records View: Accessible by Analyst and Admin.";
    }

    @PostMapping("/create")
    public String createRecord() {
        return "Record Created: Admin only.";
    }

    @PutMapping("/update")
    public String updateRecord() {
        return "Record Updated: Admin only.";
    }

    @DeleteMapping("/delete")
    public String deleteRecord() {
        return "Record Deleted: Admin only.";
    }
}
