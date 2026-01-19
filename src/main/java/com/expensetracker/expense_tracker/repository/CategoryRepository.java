package com.expensetracker.expense_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.expensetracker.expense_tracker.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
