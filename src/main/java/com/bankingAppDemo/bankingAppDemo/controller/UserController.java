package com.bankingAppDemo.bankingAppDemo.controller;

import com.bankingAppDemo.bankingAppDemo.payload.request.CreditAndDebitRequest;
import com.bankingAppDemo.bankingAppDemo.payload.request.EnquiryRequest;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/balance-enquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest request){
        return  userService.balanceEnquiry(request);
    }

    @GetMapping("/name-enquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest request){
        return userService.nameEnquiry(request);
    }

    @PostMapping("/credit-account")
    public BankResponse creditAccount(@RequestBody CreditAndDebitRequest request){
        return userService.creditAccount(request);
    }

    @PostMapping("/debit-account")
    public BankResponse debitAccount(@RequestBody CreditAndDebitRequest request){
        return userService.debitAccount(request);
    }
}