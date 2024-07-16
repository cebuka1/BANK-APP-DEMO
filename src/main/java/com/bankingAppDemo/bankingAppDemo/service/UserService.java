package com.bankingAppDemo.bankingAppDemo.service;

import com.bankingAppDemo.bankingAppDemo.payload.request.CreditAndDebitRequest;
import com.bankingAppDemo.bankingAppDemo.payload.request.EnquiryRequest;
import com.bankingAppDemo.bankingAppDemo.payload.request.TransferRequest;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;

public interface UserService {
    BankResponse balanceEnquiry(EnquiryRequest enquiryRequest);
    String nameEnquiry(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(CreditAndDebitRequest request);

    BankResponse debitAccount(CreditAndDebitRequest request);

    BankResponse transfer(TransferRequest request);

//    BankResponse transfer(TransferRequest request);
}
