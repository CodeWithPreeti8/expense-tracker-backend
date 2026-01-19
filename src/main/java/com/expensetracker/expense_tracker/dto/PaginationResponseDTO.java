package com.expensetracker.expense_tracker.dto;

import java.util.List;

public class PaginationResponseDTO {
	 
	private List<ExpenseResponseDTO> expenses;
    private int currentPage;
    private int totalPages;
    private long totalItems;
	public List<ExpenseResponseDTO> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<ExpenseResponseDTO> expenses) {
		this.expenses = expenses;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalItems() {
		return totalItems;
	}
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}

    
    
}

