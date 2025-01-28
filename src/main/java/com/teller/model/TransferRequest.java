package com.teller.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransferRequest {
    @NotEmpty
    private String fromAccountId;
    private List<TransferToAccountRequest> transferToAccountRequestList;

}
