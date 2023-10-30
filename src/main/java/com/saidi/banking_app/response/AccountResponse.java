package com.saidi.banking_app.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccountResponse {
    private String firstName;
    private String lastName;
    private List<String> accountNumber;
    private String email;
    private String phoneNumber;
    private String address;

}
