package com.bankingAppDemo.bankingAppDemo.service;

import com.bankingAppDemo.bankingAppDemo.payload.request.EmailDetails;
import com.bankingAppDemo.bankingAppDemo.payload.request.EmailRequest;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);


}
