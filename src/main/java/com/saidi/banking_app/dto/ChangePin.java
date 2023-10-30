package com.saidi.banking_app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangePin {
    private String oldPin;
    private String newPin;
}
