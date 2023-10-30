package com.saidi.banking_app.services;

import com.saidi.banking_app.config.JWTGenerator;
import com.saidi.banking_app.dto.ChangePin;
import com.saidi.banking_app.dto.LoginDto;
import com.saidi.banking_app.dto.RegisterDto;
import com.saidi.banking_app.dto.TransferDto;
import com.saidi.banking_app.exceptions.AlreadyExistsException;
import com.saidi.banking_app.exceptions.AmountLessThanZeroException;
import com.saidi.banking_app.exceptions.IncorrectPinException;
import com.saidi.banking_app.exceptions.NotFoundException;
import com.saidi.banking_app.models.*;
import com.saidi.banking_app.repository.AccountRepository;
import com.saidi.banking_app.repository.ConfirmationRepository;
import com.saidi.banking_app.repository.TransactionRepository;
import com.saidi.banking_app.repository.UserRepository;
import com.saidi.banking_app.response.AccountResponse;
import com.saidi.banking_app.response.LoginResponse;
import com.saidi.banking_app.response.TransactionHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JWTGenerator jwtGenerator;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public ResponseEntity<String> registerUser(RegisterDto registerDto) throws AlreadyExistsException {
        Optional<User> user = userRepository.findByEmailIgnoreCase(registerDto.getEmail());
        if(user.isPresent()) {
            throw new AlreadyExistsException("User with email " + registerDto.getEmail() + " already exists");
        }

        Account account = new Account();

        User newUser = User.builder()
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .address(registerDto.getAddress())
                .phoneNumber(registerDto.getPhoneNumber())
                .accounts(Arrays.asList(account))
                .enabled(false)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(newUser);

        Confirmation confirmation = new Confirmation(newUser);
        confirmationRepository.save(confirmation);
        emailService.sendEmailToUser(registerDto.getFirstName(),registerDto.getEmail(), confirmation.getToken());

        return new ResponseEntity<>("Please verify your email to continue", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        if(token != null) {
            User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail()).get();
            user.setEnabled(true);
            userRepository.save(user);
            return new ResponseEntity<>("Account verified successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to verify account", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<LoginResponse> loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AccountResponse> viewAccount(Long userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        User userExists = user.get();
        AccountResponse accountResponse = mapAccountToResponse(userExists);

        return new ResponseEntity<>(accountResponse,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePin(String accountNumber, ChangePin changePin) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account != null) {
            if(account.getPin().matches(changePin.getOldPin())) {
                account.setPin(changePin.getNewPin());
                accountRepository.save(account);
                return new ResponseEntity<>("Pin changed successfully", HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Old Pin is wrong", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Failed to change pin", HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<String> depositIntoAccount(String accountNumber, BigDecimal amount) throws NotFoundException, AmountLessThanZeroException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if(account != null) {
            if(amount.compareTo(BigDecimal.ZERO) <= 0.00  ) {
                throw new AmountLessThanZeroException("Amount cannot be less than 0");
            }
            BigDecimal newBalance = account.getBalance().add(amount);
            account.setBalance(newBalance);
            accountRepository.save(account);

            //creating transaction
            Transaction transaction = new Transaction();
            transaction.setAmount(amount);
            transaction.setTransactionDate(LocalDateTime.now());
            transaction.setTransactionType(TransactionType.DEPOSIT);
            transaction.setSenderAccount(account);
            transaction.setReceiverAccount(account);
            transactionRepository.save(transaction);
            return new ResponseEntity<>("You have successfully deposited " + amount + " into your account " + " your new balance is " + newBalance, HttpStatus.OK );
        }

        throw new NotFoundException("Account number " + accountNumber + " not found");

    }

    @Override
    public ResponseEntity<String> withdrawFromAccount(String accountNumber, BigDecimal amount, String pin) throws AmountLessThanZeroException, NotFoundException, IncorrectPinException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            if (pin.equals(account.getPin())) {
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new AmountLessThanZeroException("Amount cannot be less than or equal to 0");
                } else if (amount.compareTo(account.getBalance()) > 0) {
                    throw new AmountLessThanZeroException("You can't withdraw an amount greater than available in the account");
                } else {
                    BigDecimal newBalance = account.getBalance().subtract(amount);
                    account.setBalance(newBalance);
                    accountRepository.save(account);
                    //creating transaction
                    Transaction transaction = new Transaction();
                    transaction.setAmount(amount);
                    transaction.setTransactionDate(LocalDateTime.now());
                    transaction.setTransactionType(TransactionType.WITHDRAWAL);
                    transaction.setSenderAccount(account);
                    transaction.setReceiverAccount(account);
                    transactionRepository.save(transaction);
                    return new ResponseEntity<>("You have successfully withdrawn " + amount + " from your account. Your new balance is " + newBalance, HttpStatus.OK);
                }
            } else {
                throw new IncorrectPinException("Incorrect PIN");
            }
        } else {
            throw new NotFoundException("Account number " + accountNumber + " not found");
        }
    }

    @Override
    public ResponseEntity<String> sendMoney(String accountNumber, BigDecimal amount, String pin, String receiverAccountNumber) throws AmountLessThanZeroException, IncorrectPinException, NotFoundException {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account != null) {
            if (pin.equals(account.getPin())) {
                Account receiverAccount = accountRepository.findByAccountNumber(receiverAccountNumber);
                if(receiverAccount != null) {
                    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new AmountLessThanZeroException("Amount cannot be less than or equal to 0");
                    } else if (amount.compareTo(account.getBalance()) > 0) {
                        throw new AmountLessThanZeroException("You can't send amount greater than available in the account");
                    } else {
                        BigDecimal newBalance = account.getBalance().subtract(amount);
                        BigDecimal receiverBalance = receiverAccount.getBalance().add(amount);
                        account.setBalance(newBalance);
                        receiverAccount.setBalance(receiverBalance);
                        accountRepository.save(account);


                        //creating transaction
                        Transaction transaction = new Transaction();
                        transaction.setAmount(amount);
                        transaction.setTransactionDate(LocalDateTime.now());
                        transaction.setTransactionType(TransactionType.TRANSFER);
                        transaction.setSenderAccount(account);
                        transaction.setReceiverAccount(receiverAccount);
                        transactionRepository.save(transaction);
                        return new ResponseEntity<>("You have successfully send " + amount + " to " + receiverAccountNumber + ". Your new balance is " + newBalance, HttpStatus.OK);
                    }
                } else {
                    throw new NotFoundException("Receiver account number " + receiverAccountNumber + " not found");
                }

            } else {
                throw new IncorrectPinException("Incorrect PIN");
            }
        } else {
            throw new NotFoundException("Account number " + accountNumber + " not found");
        }
    }

    @Override
    public ResponseEntity<List<TransactionHistory>> getTransactionHistory(String accountNumber) throws NotFoundException {
        List<Transaction> transactions = transactionRepository.findBySenderAccount_AccountNumberOrReceiverAccount_AccountNumber(accountNumber,accountNumber);
        if(transactions.isEmpty()) {
            throw new NotFoundException("No transactions found");
        }

        List<TransactionHistory> transactionHistoryList = transactions.stream()
                .map(this::mapToTransactionDto)
                .sorted((t1, t2) -> t2.getTransactionDate().compareTo(t1.getTransactionDate()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(transactionHistoryList, HttpStatus.OK);
    }


    private AccountResponse mapAccountToResponse(User user){
        return AccountResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .accountNumber(user.getAccounts().stream().map(Account::getAccountNumber).collect(Collectors.toList()))
                .address(user.getAddress())
                .phoneNumber(user.getPhoneNumber())
                .build();

    }

    private TransactionHistory mapToTransactionDto(Transaction transaction) {
        return TransactionHistory.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .transactionType(transaction.getTransactionType())
                .senderAccount(transaction.getSenderAccount().getAccountNumber())
                .receiverAccount(transaction.getReceiverAccount().getAccountNumber())
                .build();

    }
}
