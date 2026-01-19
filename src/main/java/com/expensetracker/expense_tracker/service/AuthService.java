package com.expensetracker.expense_tracker.service;

import com.expensetracker.expense_tracker.dto.LoginRequestDTO;
import com.expensetracker.expense_tracker.service.dto.RegisterRequestDTO;

public interface AuthService {
	String login(LoginRequestDTO loginRequest);
    String register(RegisterRequestDTO registerRequest);
}
