package com.expensetracker.expense_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expense_tracker.dto.BudgetRequestDTO;
import com.expensetracker.expense_tracker.service.BudgetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/budgets")
public class BudgetController {
	
	private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping
    public ResponseEntity<String> setBudget(
            @Valid @RequestBody BudgetRequestDTO requestDTO) {

        budgetService.setBudget(requestDTO);
        return ResponseEntity.ok("Budget set successfully");
    }
}

