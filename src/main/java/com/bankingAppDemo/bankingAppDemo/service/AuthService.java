package com.bankingAppDemo.bankingAppDemo.service;

import com.bankingAppDemo.bankingAppDemo.payload.request.LoginRequest;
import com.bankingAppDemo.bankingAppDemo.payload.request.UserRequest;
import com.bankingAppDemo.bankingAppDemo.payload.response.ApiResponse;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.payload.response.JwtAuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    BankResponse registerUser(UserRequest userRequest);

    ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest);
}