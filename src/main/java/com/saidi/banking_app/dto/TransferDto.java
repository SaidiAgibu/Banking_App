package com.saidi.banking_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferDto {
    private String fromAccountNumber;
    private String toAccountNumber;
}
