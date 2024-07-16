package com.bankingAppDemo.bankingAppDemo.controller;

import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.service.GeneralUserService;
import com.bankingAppDemo.bankingAppDemo.utils.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@RestController
public class GeneralUSerController {
    private final GeneralUserService generalUserService;

    @PutMapping("/{id}/profile-pics")
    public ResponseEntity<BankResponse<String>> profileUpload( @PathVariable("id") Long id,
            @RequestParam MultipartFile profilePic){

       if ( profilePic.getSize() > AppConstants.MAX_FILE_SIZE) {
           return ResponseEntity
                   .status(HttpStatus.BAD_REQUEST)
                   .body(new BankResponse<>("File size exceed the normal limit"));
       }
            return generalUserService.uploadProfilePics(id, profilePic);
        }
    }

