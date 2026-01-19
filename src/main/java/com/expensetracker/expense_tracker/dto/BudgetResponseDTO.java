package com.expensetracker.expense_tracker.dto;

public class BudgetResponseDTO {
	 
	private Double totalBudget;
    private Double spentAmount;
    private Double remainingBudget;
	public Double getTotalBudget() {
		return totalBudget;
	}
	public void setTotalBudget(Double totalBudget) {
		this.totalBudget = totalBudget;
	}
	public Double getSpentAmount() {
		return spentAmount;
	}
	public void setSpentAmount(Double spentAmount) {
		this.spentAmount = spentAmount;
	}
	public Double getRemainingBudget() {
		return remainingBudget;
	}
	public void setRemainingBudget(Double remainingBudget) {
		this.remainingBudget = remainingBudget;
	}
	public BudgetResponseDTO() {
		
	}
	public BudgetResponseDTO(Double totalBudget, Double spentAmount, Double remainingBudget) {
		super();
		this.totalBudget = totalBudget;
		this.spentAmount = spentAmount;
		this.remainingBudget = remainingBudget;
	}
    
}
