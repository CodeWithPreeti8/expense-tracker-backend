package com.expensetracker.expense_tracker.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Service;
import com.expensetracker.expense_tracker.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker.dto.PaginationResponseDTO;
import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.Category;
import com.expensetracker.expense_tracker.entity.Expense;
import com.expensetracker.expense_tracker.entity.User;
import com.expensetracker.expense_tracker.exception.BadRequestException;
import com.expensetracker.expense_tracker.exception.CustomAccessDeniedException;
import com.expensetracker.expense_tracker.exception.ResourceNotFoundException;
import com.expensetracker.expense_tracker.repository.BudgetRepository;
import com.expensetracker.expense_tracker.repository.CategoryRepository;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import com.expensetracker.expense_tracker.repository.UserRepository;
import com.expensetracker.expense_tracker.security.CustomUserDetails;
import com.expensetracker.expense_tracker.service.AsyncService;
import com.expensetracker.expense_tracker.service.ExpenseService;
import com.expensetracker.expense_tracker.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ExpenseServiceImpl implements ExpenseService{
	
	private final ExpenseRepository expenseRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;
	private final UserService userService;
	private final BudgetRepository budgetRepository;
	private final AsyncService asyncService;
	
	public ExpenseServiceImpl(ExpenseRepository expenseRepository,CategoryRepository categoryRepository,UserRepository userRepository,UserService userService, BudgetRepository budgetRepository,AsyncService asyncService) {
		this.expenseRepository = expenseRepository;
		this.categoryRepository = categoryRepository;
		this.userRepository = userRepository;
		this.userService = userService;
		this.budgetRepository = budgetRepository;
		this.asyncService = asyncService;
	}
	@Override
	public ExpenseResponseDTO createExpense(ExpenseRequestDTO requestDTO) {
											//Budget must be checked before saving expense
		// 1️⃣ Get logged-in user
		User currentUser = userService.getLoggedInUser();

		// 2️⃣ Extract month & year from expense date
		int month = requestDTO.getExpenseDate().getMonthValue();
		int year = requestDTO.getExpenseDate().getYear();

		// 3️⃣ Fetch budget for this user & month
		Budget budget = budgetRepository
		        .findByUserAndMonthAndYear(currentUser, month, year)
		        .orElseThrow(() ->
		                new RuntimeException("Budget not set for this month"));
		
		// 4️⃣ Get total expenses already spent
		Double totalSpent = expenseRepository
		        .getTotalExpenseForMonth(currentUser, month, year);
		
		// 5️⃣ Budget validation
		if (totalSpent + requestDTO.getAmount() > budget.getAmount()) {
			throw new BadRequestException(
				    "Budget exceeded. Remaining amount: " 
				    + (budget.getAmount() - totalSpent)
				);
		}
		
		// 1️⃣ Fetch Category from DB using categoryId
		
	    Category category = categoryRepository.findById(requestDTO.getCategoryId())
	            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

	    User users= userService.getLoggedInUser();
	    // 2️⃣ Fetch logged-in User from JWT
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	    CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
	    String email = userDetails.getUsername(); // or getEmail()

	    User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	    // 3️⃣ Map DTO → Entity
	    Expense expense = new Expense();
	    expense.setAmount(requestDTO.getAmount());
	    expense.setDescription(requestDTO.getDescription());
	    expense.setExpenseDate(requestDTO.getExpenseDate());
	    expense.setCategory(category);
	    expense.setUser(user);

	    // 4️⃣ Save Expense
	    Expense savedExpense = expenseRepository.save(expense);
	    
	    //Call Async Method
	    asyncService.handleExpenseCreated(currentUser.getId(), requestDTO.getAmount());

	    // 5️⃣ Map Entity → ResponseDTO
	    ExpenseResponseDTO response = new ExpenseResponseDTO();
	    response.setId(savedExpense.getId());
	    response.setAmount(savedExpense.getAmount());
	    response.setDescription(savedExpense.getDescription());
	    response.setExpenseDate(savedExpense.getExpenseDate());
	    response.setCategoryName(savedExpense.getCategory().getName());

	    return response;
	}
	@Override
	public ExpenseResponseDTO getExpenseById(Long id) {
		
		User currentUser = userService.getLoggedInUser();
			//Find Expense by id 
		Expense expense = expenseRepository.findById(id).orElseThrow(() ->new ResourceNotFoundException("Expense Not found with id : "+id));
		
		// 3️⃣ Ownership check 🔐
		if (!expense.getUser().getId().equals(currentUser.getId())) {
			throw new CustomAccessDeniedException("You are not allowed to view this expense");
	    }
			//MAP to DTO
		ExpenseResponseDTO response = new ExpenseResponseDTO();
		response.setId(expense.getId());
		response.setAmount(expense.getAmount());
		response.setDescription(expense.getDescription());
		response.setExpenseDate(expense.getExpenseDate());
		response.setCategoryName(expense.getCategory().getName());
		
		return response;
	}
	
	@Override
	public PaginationResponseDTO getExpense(int page, int size) {
		
		// 1️⃣ Get logged-in user
	    User currentUser = userService.getLoggedInUser();

	    // 2️⃣ Create Pageable object
	   Pageable pageable = PageRequest.of(page, size, Sort.by("expenseDate").descending());

	    // 3️⃣ Fetch paginated expenses
	    Page<Expense> expensePage =
	            expenseRepository.findByUser(currentUser, pageable);

	    // 4️⃣ Convert Entity → DTO
	    List<ExpenseResponseDTO> responseList = new ArrayList<>();

	    for (Expense expense : expensePage.getContent()) {
	        ExpenseResponseDTO dto = new ExpenseResponseDTO();
	        dto.setId(expense.getId());
	        dto.setAmount(expense.getAmount());
	        dto.setDescription(expense.getDescription());
	        dto.setExpenseDate(expense.getExpenseDate());
	        dto.setCategoryName(expense.getCategory().getName());

	        responseList.add(dto);
	    }

	    PaginationResponseDTO response = new PaginationResponseDTO();
	    response.setExpenses(responseList);
	    response.setCurrentPage(expensePage.getNumber());
	    response.setTotalPages(expensePage.getTotalPages());
	    response.setTotalItems(expensePage.getTotalElements());

	    return response;
	}
	@Override
	public ExpenseResponseDTO updateExpense( Long id,ExpenseRequestDTO requestDTO) {
		User currentUser = userService.getLoggedInUser();	
		
		//2 Find Expense by id
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id : "+id));
		
		// 3️⃣ Ownership check 🔐
	    if (!expense.getUser().getId().equals(currentUser.getId())) {
	        throw new RuntimeException("You are not allowed to update this expense");
	    }
	    
	 // 4️⃣ Fetch category
	    Category category = categoryRepository.findById(requestDTO.getCategoryId())
	            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

			// Update fields from RequestDTO to entity
		expense.setAmount(requestDTO.getAmount());
		expense.setDescription(requestDTO.getDescription());
		expense.setExpenseDate(requestDTO.getExpenseDate());
		expense.setCategory(category);
		
			// Save the updated Expense
		Expense updatedExpense = expenseRepository.save(expense);
		
			// Map to responseDTO
		ExpenseResponseDTO dto = new ExpenseResponseDTO();
		dto.setId(updatedExpense.getId());
		dto.setAmount(updatedExpense.getAmount());
		dto.setDescription(updatedExpense.getDescription());
		dto.setExpenseDate(updatedExpense.getExpenseDate());
		dto.setCategoryName(updatedExpense.getCategory().getName());
		
		return dto;
		
	}
	
		
	@Override
	public void deleteExpense(Long id) {
		
		 // 1️⃣ Get logged-in user
	    User currentUser = userService.getLoggedInUser();
	    
	    // 2️⃣ Fetch expense
		Expense expense = expenseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Expense not found with id : "+id));
		
		// 3️⃣ Ownership check 
	    if (!expense.getUser().getId().equals(currentUser.getId())) {
	        throw new RuntimeException("You are not allowed to delete this expense");
	    }
		expenseRepository.delete(expense);
	}


}
