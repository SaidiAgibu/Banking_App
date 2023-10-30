package com.saidi.banking_app.repository;

import com.saidi.banking_app.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderAccount_AccountNumberOrReceiverAccount_AccountNumber(String senderAccount, String receiverAccount);
    List<Transaction> findBySenderAccount_AccountNumber(String accountNumber);

}
