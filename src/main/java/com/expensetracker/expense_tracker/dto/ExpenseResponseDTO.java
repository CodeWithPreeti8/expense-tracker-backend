package com.expensetracker.expense_tracker.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ExpenseResponseDTO {
	
	@NotNull(message = "Expense ID cannot be null")
	private Long id;
	
	@NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
	private Double amount;
	
	@Size(max = 200, message = "Description cannot exceed 200 characters")
	private String description;
	
	@NotBlank(message = "Expense date is required")
	private LocalDate expenseDate;
	
	 @NotBlank(message = "Category name is required")
	private String CategoryName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(LocalDate expenseDate) {
		this.expenseDate = expenseDate;
	}
	public String getCategoryName() {
		return CategoryName;
	}
	public void setCategoryName(String categoryName) {
		CategoryName = categoryName;
	}
	
}
