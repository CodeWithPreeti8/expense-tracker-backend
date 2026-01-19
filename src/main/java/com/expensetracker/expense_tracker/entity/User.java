package com.expensetracker.expense_tracker.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name= "users")
public class User {
	
	@Enumerated(EnumType.STRING)
	@Column(nullable= false)
	private Role role;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false, unique= true)
	private String email;
	@Column(nullable = false)
	private String password;
	
	@OneToMany(mappedBy= "user")
	private List<Expense> expenses = new ArrayList<>();

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Expense> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<Expense> expenses) {
		this.expenses = expenses;
	}
	public User() {
		
	}
	public User(Role role, Long id, String name, String email, String password, List<Expense> expenses) {
		super();
		this.role = role;
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.expenses = expenses;
	}

	@Override
	public String toString() {
		return "User [role=" + role + ", id=" + id + ", name=" + name + ", email=" + email + ", password=" + password
				+ ", expenses=" + expenses + "]";
	}
	
}
