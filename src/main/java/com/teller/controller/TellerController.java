package com.teller.controller;

import com.teller.constant.ResponseCode;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.WithdrawRequest;
import com.teller.service.DepositService;
import com.teller.service.TransferService;
import com.teller.service.WithdrawService;
import com.teller.utils.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TellerController {
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;


    @PostMapping("/deposit")
    public ResponseEntity<TellerResponse<TellerResponse>> deposit(@RequestBody DepositRequest request) throws Exception {
        TellerResponse<TellerResponse> oneTmbOneServiceResponse = new TellerResponse<>();

        try {
            TellerResponse deposit = depositService.deposit(request);
            oneTmbOneServiceResponse.setStatus(ResponseCode.SUCCESS_DEPOSIT.getDesc());
            oneTmbOneServiceResponse.setCode(ResponseCode.SUCCESS_DEPOSIT.getCode());
            return ResponseEntity.ok().body(deposit);
        } catch (CommonException e) {
            throw e;
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TellerResponse> withdraw(@RequestBody WithdrawRequest request) {
        TellerResponse deposit = withdrawService.withdraw(request);

        return ResponseEntity.ok().body(deposit);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TellerResponse> transfer(@RequestBody TransferRequest request) {
        TellerResponse deposit = transferService.transfer(request);

        return ResponseEntity.ok().body(deposit);
    }

}
