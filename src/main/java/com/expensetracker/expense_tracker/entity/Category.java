package com.expensetracker.expense_tracker.entity;

import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name= "categorys")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false, unique= true)
	private String name;
	
	@OneToMany(mappedBy = "category")
	private List<Expense> expenses = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}

	public Category () {
		
	}
	// Constructor to easily create categories
    public Category(String name) {
        this.name = name;
    }
	public Category(Long id, String name, List<Expense> expenses) {
		super();
		this.id = id;
		this.name = name;
		this.expenses = expenses;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + ", expenses=" + expenses + "]";
	}
	
}
