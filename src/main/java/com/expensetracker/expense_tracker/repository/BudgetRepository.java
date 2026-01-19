package com.expensetracker.expense_tracker.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.User;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
	
	Optional<Budget> findByUserAndMonthAndYear(User user, int month, int year);
	Optional<Budget> findByUser(User user);

}
