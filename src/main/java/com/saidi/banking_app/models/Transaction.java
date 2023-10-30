package com.saidi.banking_app.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {
    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_sequence")
    private Long transactionId;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    private LocalDateTime transactionDate;
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account senderAccount;
    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiverAccount;
}
