package com.bankingAppDemo.bankingAppDemo.service;

import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface GeneralUserService {
    ResponseEntity<BankResponse<String>> uploadProfilePics(Long userId, MultipartFile multipartFile);

}
