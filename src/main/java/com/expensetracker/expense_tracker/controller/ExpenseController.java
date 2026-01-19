package com.expensetracker.expense_tracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expense_tracker.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker.dto.PaginationResponseDTO;
import com.expensetracker.expense_tracker.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
public class ExpenseController {
	
	private final ExpenseService expenseService;
	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}
	
	@PostMapping("/expenses")
	public ExpenseResponseDTO createExpense(@Valid @RequestBody ExpenseRequestDTO requestDTO) {
		System.out.println("CategoryId = " + requestDTO.getCategoryId());
		return expenseService.cretaeExpense(requestDTO);
	}
	
	@GetMapping("/expenses/{id}")
	public ExpenseResponseDTO getExpenseById(@PathVariable Long id) {
		return expenseService.getExpenseById(id);
	}
	
	@GetMapping("/expenses")
	public PaginationResponseDTO getExpenses(@RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "5") int size) {

	    return expenseService.getExpense(page, size);
	}
	
	@PutMapping("/expenses/{id}")
	public ExpenseResponseDTO updateExpense(@Valid @PathVariable Long id, @RequestBody ExpenseRequestDTO requestDTO) {
		return expenseService.updateExpense(id,requestDTO);
	}
	
	@DeleteMapping("/expenses/{id}")
	public String deleteExpense(@PathVariable Long id) {
		expenseService.deleteExpense(id);
		return "Expense deleted Successfully";
	}
	
}
