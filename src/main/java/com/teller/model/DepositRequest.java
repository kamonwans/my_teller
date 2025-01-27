package com.teller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest {

    @NotEmpty
    private String accountId;
    @NotNull
    private Integer amount;
}
