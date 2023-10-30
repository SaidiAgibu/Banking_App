package com.saidi.banking_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@AllArgsConstructor
@Builder
@Entity
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long accountId;
    private String pin;
    private String accountNumber;
    private BigDecimal balance;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactionHistory = new ArrayList<>();


    public Account() {
        this.accountNumber = generateRandomAccountNumber();
        this.balance = BigDecimal.ZERO;
        this.pin = generatePin();
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;

        int generatedAccount = random.nextInt(max - min + 1) + min;

        return String.valueOf(generatedAccount);
    }

    private String generatePin() {
        Random random = new Random();
        int min = 1000;
        int max = 9999;

        int generatePin = random.nextInt(max - min + 1) + min;
        return String.valueOf(generatePin);
    }

}
