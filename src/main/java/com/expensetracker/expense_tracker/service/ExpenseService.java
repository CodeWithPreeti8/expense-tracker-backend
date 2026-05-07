package com.expensetracker.expense_tracker.service;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.expensetracker.expense_tracker.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker.dto.PaginationResponseDTO;

import jakarta.validation.Valid;

public interface ExpenseService {
	
	ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO);
	ExpenseResponseDTO getExpenseById(Long id);
	PaginationResponseDTO getExpense(int page, int size);
	ExpenseResponseDTO updateExpense( Long id,ExpenseRequestDTO requestDTO);
	void deleteExpense(Long id);

}
