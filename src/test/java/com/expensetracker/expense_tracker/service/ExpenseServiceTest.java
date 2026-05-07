package com.expensetracker.expense_tracker.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.expensetracker.expense_tracker.dto.ExpenseRequestDTO;
import com.expensetracker.expense_tracker.dto.ExpenseResponseDTO;
import com.expensetracker.expense_tracker.entity.Budget;
import com.expensetracker.expense_tracker.entity.Category;
import com.expensetracker.expense_tracker.entity.Expense;
import com.expensetracker.expense_tracker.entity.User;
import com.expensetracker.expense_tracker.repository.BudgetRepository;
import com.expensetracker.expense_tracker.repository.CategoryRepository;
import com.expensetracker.expense_tracker.repository.ExpenseRepository;
import com.expensetracker.expense_tracker.repository.UserRepository;
import com.expensetracker.expense_tracker.security.CustomUserDetails;
import com.expensetracker.expense_tracker.service.impl.ExpenseServiceImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


public class ExpenseServiceTest {
	@Mock
	private UserService userService;
	@Mock
	private ExpenseRepository expenseRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private BudgetRepository budgetRepository;
	@Mock
	private CategoryRepository categoryRepository;
	@Mock
	private AsyncService asyncService;
	
	@InjectMocks
	private ExpenseServiceImpl expenseService;
	
	private User user;
	private Category category;
	private ExpenseRequestDTO dto;
	@BeforeEach
	void setUp() {
		 MockitoAnnotations.openMocks(this);
		// ✅ Common User
		 user = new User();
		 user.setId(1L);
		 user.setEmail("testuser");
		 
		// ✅ Common Category
		 category = new Category();
		 category.setId(1L);
		 category.setName("Food");
		 
		// ✅ Common DTO
		 dto = new ExpenseRequestDTO();
		 dto.setAmount(1000.0);
		 dto.setDescription("Lunch");
		 dto.setCategoryId(1L);
		 dto.setExpenseDate(LocalDate.now());
		  
		  // ✅ Security Mock
	        Authentication authentication = mock(Authentication.class);
	        SecurityContext securityContext = mock(SecurityContext.class);
	        CustomUserDetails userDetails = mock(CustomUserDetails.class);

	        when(userDetails.getUsername()).thenReturn("testuser");
	        when(authentication.getPrincipal()).thenReturn(userDetails);
	        when(securityContext.getAuthentication()).thenReturn(authentication);

	        SecurityContextHolder.setContext(securityContext);
	    
		}
	
	
	
	 // ✅ TEST 1: Success Case
	@Test
	void testCreateExpense_Success() {
		
		// ✅ Mock User
        when(userRepository.findByEmail("testuser"))
                .thenReturn(Optional.of(user));

        when(userService.getLoggedInUser())
                .thenReturn(user);

     // ✅ Mock Budget
        Budget budget = new Budget();
        budget.setAmount(5000.0);

        when(budgetRepository.findByUserAndMonthAndYear(any(), anyInt(), anyInt()))
                .thenReturn(Optional.of(budget));

     // ✅ Mock Category
        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));
        
     // ✅ Mock Saved Expense
        Expense savedExpense = new Expense();
        savedExpense.setId(1L);
        savedExpense.setAmount(1000.0);
        savedExpense.setDescription("Lunch");
        savedExpense.setExpenseDate(LocalDate.now());
        savedExpense.setCategory(category);

        when(expenseRepository.save(any()))
                .thenReturn(savedExpense);
        
     // ✅ Call Real Method
        ExpenseResponseDTO result = expenseService.createExpense(dto);
        
     // ✅ Assertions
        assertNotNull(result);
        assertEquals(1000.0, result.getAmount());
        assertEquals("Lunch", result.getDescription());
        assertNotNull(result.getId());
        assertNotNull(result.getExpenseDate());
        assertEquals("Food", result.getCategoryName());

     // ✅ Verify Behavior
        verify(expenseRepository, times(1)).save(any());
        verify(asyncService, times(1))
                .handleExpenseCreated(anyLong(), anyDouble());
	}
	
	@Test
	void testCreateExpense_BudgetExceeded() {

	    // ✅ Mock User
	    when(userRepository.findByEmail("testuser"))
	            .thenReturn(Optional.of(user));

	    when(userService.getLoggedInUser())
	            .thenReturn(user);

	    // ✅ Mock Budget
	    Budget budget = new Budget();
	    budget.setAmount(5000.0);

	    when(budgetRepository.findByUserAndMonthAndYear(any(), anyInt(), anyInt()))
	            .thenReturn(Optional.of(budget));

	    // ✅ Current expenses already high
	    when(expenseRepository.getTotalExpenseForMonth(any(), anyInt(), anyInt()))
	            .thenReturn(4500.0);

	    // ✅ Mock Category
	    when(categoryRepository.findById(1L))
	            .thenReturn(Optional.of(category));

	    // ✅ Assert Exception
	    RuntimeException exception = assertThrows(
	            RuntimeException.class,
	            () -> expenseService.createExpense(dto)
	    );

	    // ✅ Verify message
	    assertEquals(
	            "Budget exceeded. Remaining amount: 500.0",
	            exception.getMessage()
	    );

	    // ✅ Verify save NOT called
	    verify(expenseRepository, never()).save(any());
	}
}
