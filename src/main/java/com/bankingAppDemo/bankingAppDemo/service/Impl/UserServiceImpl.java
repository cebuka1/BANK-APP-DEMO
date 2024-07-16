package com.bankingAppDemo.bankingAppDemo.service.Impl;

import com.bankingAppDemo.bankingAppDemo.entity.UserEntity;
import com.bankingAppDemo.bankingAppDemo.payload.request.*;
import com.bankingAppDemo.bankingAppDemo.payload.response.AccountInfo;
import com.bankingAppDemo.bankingAppDemo.payload.response.BankResponse;
import com.bankingAppDemo.bankingAppDemo.repository.UserRepository;
import com.bankingAppDemo.bankingAppDemo.service.EmailService;
import com.bankingAppDemo.bankingAppDemo.service.UserService;
import com.bankingAppDemo.bankingAppDemo.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());


        if (!isAccountExists) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NUMBER_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NUMBER_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        boolean isAccountExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());

        if (!isAccountExist) {
            return AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE;
        }

        UserEntity foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditAndDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        UserEntity userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        // then update the user account balance with what is being credit with
        // to add two big decimal you use the add method
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));

        userRepository.save(userToCredit);

        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(userToCredit.getEmail())
                .messageBody("Your account has been credited with " + request.getAmount() + " from " + userToCredit.getFirstName() + " your current account balance is " + userToCredit.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditAndDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if (!isAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NUMBER_NON_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        // if account number exists
        UserEntity userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());

        //check for insufficient balance
        BigInteger availableBalance = userToDebit.getAccountBalance().toBigInteger();
        BigInteger debitAmount = request.getAmount().toBigInteger();

        if (availableBalance.intValue() < debitAmount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();


        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);

            EmailDetails debitAlert = EmailDetails.builder()
                    .subject("DEBIT ALERT")
                    .recipient(userToDebit.getEmail())
                    .messageBody("The sum of " + request.getAmount() + " has been deducted from your account! Your current account balance is " + userToDebit.getAccountBalance())
                    .build();

            emailService.sendEmailAlert(debitAlert);
        }
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }


@Override
public BankResponse transfer(TransferRequest request) {
    /**
     * 1. first check if the destination account number exists
     * 2. then check if amount to send is available.
     * 3. then deduct the amount to send from sender balance
     * 4. then add the send amount to receiver balance
     * 5. then send a debit alert and a credit alert to both sender and receiver
     *
     *
     * if the SA > RA:
     *  return 0;
     */

    boolean isDestinationAccountExists
            = userRepository.existsByAccountNumber(
            request.getDestinationAccountNumber()
    );

    if(!isDestinationAccountExists){
        return BankResponse.builder()
                .responseCode("008")
                .responseMessage("Account number does not exists")
                .build();
    }

    UserEntity sourceAccountUser =
            userRepository.findByAccountNumber(request.getSourceAccountNumber());


    if(request.getAmount().compareTo(sourceAccountUser.getAccountBalance())
            > 0){
        return BankResponse.builder()
                .responseCode("009")
                .responseMessage("INSUFFICIENT BALANCE")
                .accountInfo(null)
                .build();
    }

    sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance()
            .subtract(request.getAmount()));

    userRepository.save(sourceAccountUser);

    String sourceUsername = sourceAccountUser.getFirstName() + " " +
            sourceAccountUser.getOtherName() + " " +
            sourceAccountUser.getLastName();

    EmailDetails debitAlert = EmailDetails.builder()
            .subject("DEBIT ALERT")
            .recipient(sourceAccountUser.getEmail())
            .messageBody("The sum of "
                    + request.getAmount()
                    + "has been deducted from your account. Your current balance is "
                    + sourceAccountUser.getAccountBalance())
            .build();

    emailService.sendEmailAlert(debitAlert);

    UserEntity destinationAccountUser =
            userRepository
                    .findByAccountNumber(request
                            .getDestinationAccountNumber());
    destinationAccountUser
            .setAccountBalance(destinationAccountUser
                    .getAccountBalance().add(request.getAmount()));

    userRepository.save(destinationAccountUser);


    EmailDetails creditAlert = EmailDetails.builder()
            .subject("CREDIT ALERT")
            .recipient(destinationAccountUser.getEmail())
            .messageBody("Your account has been credited with "
                    + " " + request.getAmount() + " from"
                    + sourceUsername
                    + "your current account balance is "
                    + destinationAccountUser.getAccountBalance())
            .build();

    emailService.sendEmailAlert(creditAlert);

    return BankResponse.builder()
            .responseCode("200")
            .responseMessage("Transfer Successful")
            .accountInfo(null)
            .build();
}
}
