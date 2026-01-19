package com.expensetracker.expense_tracker.entity;

import jakarta.persistence.Entity;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "expenses")
public class Expense {
	@JsonIgnoreProperties({"expenses"})
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@JsonIgnoreProperties({"expenses"})
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name= "category_Id", nullable = false)
	private Category category;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private Double amount;
	private String description;
	@Column(nullable = false)
	private LocalDate expenseDate;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
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
	public Expense() {
		
	}
	public Expense(User user, Category category, Long id, Double amount, String description, LocalDate expenseDate) {
		super();
		this.user = user;
		this.category = category;
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.expenseDate = expenseDate;
	}
	@Override
	public String toString() {
		return "Expense [user=" + user + ", category=" + category + ", id=" + id + ", amount=" + amount
				+ ", description=" + description + ", expenseDate=" + expenseDate + "]";
	}
	
}
