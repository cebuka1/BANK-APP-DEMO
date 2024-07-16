package com.bankingAppDemo.bankingAppDemo.repository;

import com.bankingAppDemo.bankingAppDemo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
