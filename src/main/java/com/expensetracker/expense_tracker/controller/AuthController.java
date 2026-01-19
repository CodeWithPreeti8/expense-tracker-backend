package com.expensetracker.expense_tracker.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expense_tracker.dto.LoginRequestDTO;
import com.expensetracker.expense_tracker.service.AuthService;
import com.expensetracker.expense_tracker.service.dto.RegisterRequestDTO;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final AuthService authService;

	public AuthController(AuthService authService) {
	      this.authService = authService;
	 }

	    // ===== REGISTER =====
	 @PostMapping("/register")
	 public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequest) {
	     String token = authService.register(registerRequest);
	   return ResponseEntity.ok(token); // returning JWT token
	 }

	    // ===== LOGIN =====
	  @PostMapping("/login")
	  public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequest) {
	      String token = authService.login(loginRequest);
	      return ResponseEntity.ok(token); // returning JWT token
	  }
	}

