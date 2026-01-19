package com.expensetracker.expense_tracker.service;

import com.expensetracker.expense_tracker.dto.BudgetRequestDTO;
import com.expensetracker.expense_tracker.dto.BudgetResponseDTO;

public interface BudgetService {

	void setBudget(BudgetRequestDTO requestDTO);
	BudgetResponseDTO getBudget();
}
