package com.teller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teller.constant.ResponseCode;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.WithdrawRequest;
import com.teller.service.DepositService;
import com.teller.service.TransferService;
import com.teller.service.ValidateTokenService;
import com.teller.service.WithdrawService;
import com.teller.utils.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TellerController {
    private final DepositService depositService;
    private final WithdrawService withdrawService;
    private final TransferService transferService;
    private final ValidateTokenService validate;

    @PostMapping("/deposit")
    public ResponseEntity<TellerResponse<TellerResponse>> deposit(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody DepositRequest request) throws Exception {
        TellerResponse<TellerResponse> tellerResponse = new TellerResponse<>();
        String token = extractBearerToken(authorizationHeader);
        validate.validateToken(token);

        try {
            TellerResponse deposit = depositService.deposit(request);
            tellerResponse.setStatus(ResponseCode.SUCCESS_DEPOSIT.getDesc());
            tellerResponse.setCode(ResponseCode.SUCCESS_DEPOSIT.getCode());
            return ResponseEntity.ok().body(deposit);
        } catch (CommonException e) {
            ResponseEntity
            throw e;
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TellerResponse> withdraw(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                   @RequestBody WithdrawRequest request) throws CommonException, JsonProcessingException {
        String token = extractBearerToken(authorizationHeader);
        validate.validateToken(token);
        TellerResponse deposit = withdrawService.withdraw(request);
        return ResponseEntity.ok().body(deposit);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TellerResponse> transfer(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                   @RequestBody TransferRequest request) throws CommonException, JsonProcessingException {
        String token = extractBearerToken(authorizationHeader);
        validate.validateToken(token);
        TellerResponse deposit = transferService.transfer(request);
        return ResponseEntity.ok().body(deposit);
    }

    private String extractBearerToken(String authorizationHeader) throws CommonException {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new CommonException(
                    ResponseCode.INVALID_TOKEN.getCode(),
                    "Missing or invalid Authorization header",
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }
    }
}
