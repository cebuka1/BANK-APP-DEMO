package com.bankingAppDemo.bankingAppDemo.service;

import com.bankingAppDemo.bankingAppDemo.dto.AccountDto;
import com.bankingAppDemo.bankingAppDemo.entity.Account;

import java.util.List;

public interface AccountService {
    AccountDto createAccount(AccountDto accountDto);
    AccountDto getAccountById(Long id);
    AccountDto deposit(Long id, double amount);
    AccountDto withdraw(Long id, double amount);
    List<AccountDto> getAccounts();

}
