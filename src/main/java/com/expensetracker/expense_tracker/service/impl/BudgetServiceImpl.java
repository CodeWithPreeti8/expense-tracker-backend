package com.expensetracker.expense_tracker.service.impl;

import org.springframework.stereotype.Service;

import com.expensetracker.expense_tracker.dto.BudgetRequestDTO;
import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.User;
import com.expensetracker.expense_tracker.repository.BudgetRepository;
import com.expensetracker.expense_tracker.service.BudgetService;
import com.expensetracker.expense_tracker.service.UserService;

@Service
public class BudgetServiceImpl implements BudgetService {
	
	private final BudgetRepository budgetRepository;
    private final UserService userService;

    public BudgetServiceImpl(BudgetRepository budgetRepository,
                             UserService userService) {
        this.budgetRepository = budgetRepository;
        this.userService = userService;
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

}
