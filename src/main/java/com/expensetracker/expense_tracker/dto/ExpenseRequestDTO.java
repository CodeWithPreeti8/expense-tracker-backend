package com.expensetracker.expense_tracker.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class ExpenseRequestDTO {
	@NotNull(message = "Amount is required")
	@Positive(message = " Amount must be greater then 0")
	private Double amount;
	
	@Size(max = 200 , message = "Description cannot exceed 200 characters")
	private String description;
	
	@NotNull(message = " Expense date is required")
	private LocalDate expenseDate;
	
	@NotNull(message = " category id is required")
	private Long categoryId;
	
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
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
