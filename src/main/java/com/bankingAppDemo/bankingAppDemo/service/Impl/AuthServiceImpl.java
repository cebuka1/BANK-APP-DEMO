package com.bankingAppDemo.bankingAppDemo.service.Impl;

import com.bankingAppDemo.bankingAppDemo.entity.UserEntity;
import com.bankingAppDemo.bankingAppDemo.enums.Role;
import com.bankingAppDemo.bankingAppDemo.payload.request.EmailDetails;
import com.bankingAppDemo.bankingAppDemo.payload.request.LoginRequest;
import com.bankingAppDemo.bankingAppDemo.payload.request.UserRequest;
import com.bankingAppDemo.bankingAppDemo.payload.response.AccountInfo;
import com.bankingAppDemo.bankingAppDemo.payload.response.ApiResponse;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.payload.response.JwtAuthResponse;
import com.bankingAppDemo.bankingAppDemo.repository.UserRepository;
import com.bankingAppDemo.bankingAppDemo.service.AuthService;
import com.bankingAppDemo.bankingAppDemo.service.EmailService;
import com.bankingAppDemo.bankingAppDemo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final EmailService emailService;
    @Override
    public BankResponse registerUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            BankResponse response = BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .build();

            return response;
        }

        UserEntity newUser = UserEntity.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .gender(userRequest.getGender())
                .phoneNumber(userRequest.getPhoneNumber())
                .address(userRequest.getAddress())
                .BVN(userRequest.getBVN())
                .pin(userRequest.getPin())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .bankName("World Bank Limited")
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .profilePicture("https://res.cloudinary.com/dpfqbb9pl/image/upload/v1701260428/maleprofile_ffeep9.png")
                .role(Role.USER)
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        // Add email alert here
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("CONGRATULATIONS!!! Your Account Has Been Successfully Created.\n Your Account Details: \n" +
                        "Account Name : " + savedUser.getFirstName() + " " + savedUser.getLastName() + " "
                        + savedUser.getOtherName() +
                        "\nAccount Number : " + savedUser.getAccountNumber())

                .build();

        emailService.sendEmailAlert(emailDetails);



        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .bankName(savedUser.getBankName())
                        .accountName(savedUser.getFirstName() + " " +
                                savedUser.getLastName() + " " +
                                savedUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(LoginRequest loginRequest) {
        Optional<UserEntity> userEntityOptional =
                userRepository.findByEmail(loginRequest.getEmail());


        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You are logged in")
                .recipient(loginRequest.getEmail())
                .messageBody("You logged into your account. If you did" +
                        "not initiate this request, contact support desk.")
                .build();

        emailService.sendEmailAlert(loginAlert);

        UserEntity user = userEntityOptional.get();

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                "Login Successfully",
                                JwtAuthResponse.builder()
                                        .accessToken("generate token here")
                                        .tokenType("Bearer")
                                        .id(user.getId())
                                        .email(user.getEmail())
                                        .gender(user.getGender())
                                        .firstName(user.getFirstName())
                                        .lastName(user.getLastName())
                                        .profilePicture(user.getProfilePicture())
                                        .build()
                        )
                );
    }
}
