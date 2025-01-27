package com.teller.controller;

import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.WithdrawRequest;
import com.teller.service.DepositService;
import com.teller.service.TransferService;
import com.teller.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TellerController {
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;

    @PostMapping("/deposit")
    public TellerResponse deposit(DepositRequest request) {
        TellerResponse deposit = depositService.deposit(request);

        return deposit;
    }

    @PostMapping("/withdraw")
    public TellerResponse withdraw(WithdrawRequest request) {
        TellerResponse deposit = withdrawService.deposit(request);

        return deposit;
    }

    @PostMapping("/transfer")
    public TellerResponse transfer(TransferRequest request) {
        TellerResponse deposit = transferService.deposit(request);

        return deposit;
    }

}
