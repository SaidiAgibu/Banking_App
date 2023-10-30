package com.saidi.banking_app.controllers;

import com.saidi.banking_app.dto.ChangePin;
import com.saidi.banking_app.dto.TransferDto;
import com.saidi.banking_app.exceptions.AmountLessThanZeroException;
import com.saidi.banking_app.exceptions.IncorrectPinException;
import com.saidi.banking_app.exceptions.NotFoundException;
import com.saidi.banking_app.response.AccountResponse;
import com.saidi.banking_app.response.TransactionHistory;
import com.saidi.banking_app.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/account")
    private ResponseEntity<AccountResponse> viewAccount(@RequestParam("userId") Long userId) throws NotFoundException {
        return userService.viewAccount(userId);
    }

    @PutMapping("/account/change-pin")
    public ResponseEntity<String> changeAccountPin(@RequestParam("accountNumber") String accountNumber, @RequestBody ChangePin changePin) {
        return userService.changePin(accountNumber,changePin);
    }

    @PostMapping("/account/deposit")
    public ResponseEntity<String> depositIntoAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") BigDecimal amount) throws NotFoundException, AmountLessThanZeroException {
        return userService.depositIntoAccount(accountNumber, amount);
    }

    @PutMapping("/account/withdraw")
    public ResponseEntity<String> withdrawFromAccount(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") BigDecimal amount, @RequestParam("pin") String pin) throws NotFoundException, AmountLessThanZeroException, IncorrectPinException {
        return userService.withdrawFromAccount(accountNumber, amount, pin);
    }

    @PutMapping("/account/transfer")
    public ResponseEntity<String> transferMoney(@RequestParam("accountNumber") String accountNumber, @RequestParam("amount") BigDecimal amount, @RequestParam("pin") String pin, @RequestParam("receiver") String receiverAccountNumber) throws IncorrectPinException, AmountLessThanZeroException, NotFoundException {
        return userService.sendMoney(accountNumber,amount,pin,receiverAccountNumber);
    }

    @GetMapping("/account/transactions")
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory(@RequestParam("accountNumber") String accountNumber) throws NotFoundException {
        return userService.getTransactionHistory(accountNumber);
    }
}
