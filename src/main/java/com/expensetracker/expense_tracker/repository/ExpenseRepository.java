package com.expensetracker.expense_tracker.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expensetracker.expense_tracker.entity.Expense;
import com.expensetracker.expense_tracker.entity.User;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUser(User user, Pageable pageable);
    
    //To calculate already spent amount for the month.
    @Query("""
    	    SELECT COALESCE(SUM(e.amount), 0)
    	    FROM Expense e
    	    WHERE e.user = :user
    	      AND MONTH(e.expenseDate) = :month
    	      AND YEAR(e.expenseDate) = :year
    	""")
    	Double getTotalExpenseForMonth(
    	        @Param("user") User user,
    	        @Param("month") int month,
    	        @Param("year") int year
    	);
}