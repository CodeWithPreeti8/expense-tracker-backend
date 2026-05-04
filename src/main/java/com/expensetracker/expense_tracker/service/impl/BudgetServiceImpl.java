package com.expensetracker.expense_tracker.service.impl;
import com.expensetracker.expense_tracker.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.expensetracker.expense_tracker.dto.BudgetRequestDTO;
import com.expensetracker.expense_tracker.dto.BudgetResponseDTO;
import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.User;
import com.expensetracker.expense_tracker.repository.BudgetRepository;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import com.expensetracker.expense_tracker.service.BudgetService;
import com.expensetracker.expense_tracker.service.UserService;

@Service
public class BudgetServiceImpl implements BudgetService {
	
	private final BudgetRepository budgetRepository;
    private final UserService userService;
    private final ExpenseRepository expenseRepository;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             UserService userService, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
        this.expenseRepository = expenseRepository;
    }

    @Override
    public void setBudget(BudgetRequestDTO requestDTO) {

        User currentUser = userService.getLoggedInUser();

        // check if budget already exists
        budgetRepository.findByUserAndMonthAndYear(
                currentUser,
                requestDTO.getMonth(),
                requestDTO.getYear()
        ).ifPresent(b -> {
            throw new RuntimeException("Budget already set for this month");
        });

        Budget budget = new Budget();
        budget.setAmount(requestDTO.getAmount());
        budget.setMonth(requestDTO.getMonth());
        budget.setYear(requestDTO.getYear());
        budget.setUser(currentUser);

        budgetRepository.save(budget);
    }
    
    @Override
    public BudgetResponseDTO getBudget() {

        // 1️⃣ Get logged-in user
        User currentUser = userService.getLoggedInUser();

        // 2️⃣ Fetch budget by user
        Budget budget = budgetRepository.findByUser(currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not set"));
     
        // 3️⃣ Extract month & year
        int month = budget.getMonth();
        int year = budget.getYear();
        
     // 4️⃣ Calculate total spent
        Double totalSpent = expenseRepository
                .getTotalExpenseForMonth(currentUser, month, year);
        
     // 5️⃣ Calculate remaining
        Double remaining = budget.getAmount() - totalSpent;

        
     // 6️⃣ Map to DTO
        BudgetResponseDTO dto = new BudgetResponseDTO();
        dto.setTotalBudget(budget.getAmount());
        dto.setSpentAmount(totalSpent);
        dto.setRemainingBudget(remaining);

        return dto;
    }

}
