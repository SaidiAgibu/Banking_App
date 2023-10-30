package com.saidi.banking_app.response;

import com.saidi.banking_app.models.Account;
import com.saidi.banking_app.models.TransactionType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TransactionHistory {
    private Long transactionId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    private String senderAccount;
    private String receiverAccount;
}
