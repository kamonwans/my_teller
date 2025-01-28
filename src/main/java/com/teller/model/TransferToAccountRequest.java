package com.teller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferToAccountRequest {
    @NotEmpty
    private String toAccountId;
    @NotNull
    private double amount;
}
