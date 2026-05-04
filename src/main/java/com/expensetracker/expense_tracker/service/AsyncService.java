package com.expensetracker.expense_tracker.service;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.User;
import com.expensetracker.expense_tracker.repository.BudgetRepository;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import com.expensetracker.expense_tracker.repository.UserRepository;

@Service
public class AsyncService {
	
	private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public AsyncService(BudgetRepository budgetRepository,
                        ExpenseRepository expenseRepository,
                        UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }
	@Async
    public void handleExpenseCreated(Long userId, Double amount) {
		System.out.println("Thread: " + Thread.currentThread().getName());

	    // 1️⃣ Get user
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // 2️⃣ Get current month & year
	    LocalDate now = LocalDate.now();
	    int month = now.getMonthValue();
	    int year = now.getYear();

	    // 3️⃣ Get budget
	    Budget budget = budgetRepository
	            .findByUserAndMonthAndYear(user, month, year)
	            .orElse(null);

	    if (budget == null) {
	        System.out.println("⚠️ No budget set for user");
	        return;
	    }

	    // 4️⃣ Calculate total expense
	    Double totalExpense = expenseRepository
	            .getTotalExpenseForMonth(user, month, year);

	    // 5️⃣ Compare with budget
	    if (totalExpense > budget.getAmount()) {
	        System.out.println("🚨 Budget exceeded for user: " + userId);
	    } else if (totalExpense > 0.8 * budget.getAmount()) {
	        System.out.println("⚠️ Warning: 80% budget used");
	    } else {
	        System.out.println("✅ Budget is under control");
	    }
	
	}
}
