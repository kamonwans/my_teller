package com.teller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {
    @NotEmpty
    private String accountId;
    @NotEmpty
    private String toAccountId;
    @NotNull
    private Integer amount;
}
