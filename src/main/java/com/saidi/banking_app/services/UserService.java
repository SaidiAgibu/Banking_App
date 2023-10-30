package com.saidi.banking_app.services;

import com.saidi.banking_app.dto.ChangePin;
import com.saidi.banking_app.dto.LoginDto;
import com.saidi.banking_app.dto.RegisterDto;
import com.saidi.banking_app.dto.TransferDto;
import com.saidi.banking_app.exceptions.AlreadyExistsException;
import com.saidi.banking_app.exceptions.AmountLessThanZeroException;
import com.saidi.banking_app.exceptions.IncorrectPinException;
import com.saidi.banking_app.exceptions.NotFoundException;
import com.saidi.banking_app.response.AccountResponse;
import com.saidi.banking_app.response.LoginResponse;
import com.saidi.banking_app.response.TransactionHistory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface UserService {
    public ResponseEntity<String> registerUser(RegisterDto registerDto) throws AlreadyExistsException;

    public ResponseEntity<String> verifyToken(String token);

    public ResponseEntity<LoginResponse> loginUser(LoginDto loginDto);

    public ResponseEntity<AccountResponse> viewAccount(Long userId) throws NotFoundException;

    public ResponseEntity<String> changePin(String accountNumber, ChangePin changePin);

    public ResponseEntity<String> depositIntoAccount(String accountNumber, BigDecimal amount) throws NotFoundException, AmountLessThanZeroException;

    public ResponseEntity<String> withdrawFromAccount(String accountNumber, BigDecimal amount, String pin) throws AmountLessThanZeroException, NotFoundException, IncorrectPinException;


    public ResponseEntity<String> sendMoney(String accountNumber, BigDecimal amount, String pin, String receiverAccountNumber) throws AmountLessThanZeroException, IncorrectPinException, NotFoundException;

    public ResponseEntity<List<TransactionHistory>> getTransactionHistory(String accountNumber) throws NotFoundException;
}
