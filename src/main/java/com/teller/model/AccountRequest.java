package com.teller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {

    @NotEmpty
    private String crmId;
    @NotEmpty
    private String accountId;
    @NotEmpty
    private String accountName;
    @NotNull
    private double amount;
}
