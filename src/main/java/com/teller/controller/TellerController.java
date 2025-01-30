package com.teller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teller.constant.ResponseCode;
import com.teller.model.AccountIdModel;
import com.teller.model.AccountRequest;
import com.teller.model.AmountModel;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.WithdrawRequest;
import com.teller.service.CreateService;
import com.teller.service.DepositService;
import com.teller.service.InquiryService;
import com.teller.service.TransferService;
import com.teller.service.ValidateTokenService;
import com.teller.service.WithdrawService;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import io.jsonwebtoken.JwtException;
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
    private final InquiryService inquiryService;
    private final CreateService createService;
    private final ValidateTokenService validate;

    @PostMapping("/deposit")
    public ResponseEntity<TellerResponse<AmountModel>> deposit(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody DepositRequest request) throws Exception {
        TellerResponse<AmountModel> tellerResponse = new TellerResponse<>();
        String token = extractBearerToken(authorizationHeader);

        try {
            validate.validateToken(token);
            AmountModel deposit = depositService.deposit(request);
            tellerResponse.setStatus(ResponseCode.SUCCESS_DEPOSIT.getDesc());
            tellerResponse.setCode(ResponseCode.SUCCESS_DEPOSIT.getCode());
            tellerResponse.setData(deposit);
            return ResponseEntity.ok().body(tellerResponse);

        } catch (ForbiddenException ex) {
            return handleForbiddenException(ex, tellerResponse);

        } catch (CommonException ex) {
            return handleCommonException(ex, tellerResponse);
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TellerResponse<AmountModel>> withdraw(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                                @RequestBody WithdrawRequest request) throws Exception {
        TellerResponse<AmountModel> tellerResponse = new TellerResponse<>();
        String token = extractBearerToken(authorizationHeader);

        try {
            validate.validateToken(token);
            AmountModel withdraw = withdrawService.withdraw(request);
            tellerResponse.setStatus(ResponseCode.SUCCESS_WITHDRAW.getDesc());
            tellerResponse.setCode(ResponseCode.SUCCESS_WITHDRAW.getCode());
            tellerResponse.setData(withdraw);
            return ResponseEntity.ok().body(tellerResponse);

        } catch (ForbiddenException ex) {
            return handleForbiddenException(ex, tellerResponse);

        } catch (CommonException e) {
            return handleCommonException(e, tellerResponse);
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<TellerResponse<AmountModel>> transfer(@RequestHeader(value = "Authorization") String authorizationHeader,
                                                                @RequestBody TransferRequest request) throws CommonException, JsonProcessingException {
        String token = extractBearerToken(authorizationHeader);
        TellerResponse<AmountModel> tellerResponse = new TellerResponse<>();

        try {
            validate.validateToken(token);
            AmountModel transfer = transferService.transfer(request);
            tellerResponse.setStatus(ResponseCode.SUCCESS_DEPOSIT.getDesc());
            tellerResponse.setCode(ResponseCode.SUCCESS_DEPOSIT.getCode());
            tellerResponse.setData(transfer);
            return ResponseEntity.ok().body(tellerResponse);

        } catch (ForbiddenException ex) {
            return handleForbiddenException(ex, tellerResponse);

        } catch (CommonException e) {
            return handleCommonException(e, tellerResponse);
        }

    }

    @PostMapping("/inquiry")
    public ResponseEntity<TellerResponse<AmountModel>> inquiry(
            @RequestHeader(value = "Authorization") String authorizationHeader,
            @RequestBody AccountIdModel request) throws Exception {
        TellerResponse<AmountModel> amountModelTellerResponse = new TellerResponse<>();
        String token = extractBearerToken(authorizationHeader);

        try {
            validate.validateToken(token);
            AmountModel inquiry = inquiryService.inquiry(request);
            amountModelTellerResponse.setStatus(ResponseCode.SUCCESS.getDesc());
            amountModelTellerResponse.setCode(ResponseCode.SUCCESS.getCode());
            amountModelTellerResponse.setData(inquiry);
            return ResponseEntity.ok().body(amountModelTellerResponse);

        } catch (ForbiddenException ex) {
            return handleForbiddenException(ex, amountModelTellerResponse);

        } catch (CommonException e) {
            return handleCommonException(e, amountModelTellerResponse);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<TellerResponse<String>> create(
            @RequestBody AccountRequest request) throws Exception {
        TellerResponse<String> tellerResponse = new TellerResponse<>();

        createService.create(request);
        tellerResponse.setStatus(ResponseCode.SUCCESS.getDesc());
        tellerResponse.setCode(ResponseCode.SUCCESS.getCode());
        return ResponseEntity.ok().body(tellerResponse);

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

    private static <T> ResponseEntity<TellerResponse<T>> handleCommonException(CommonException e, TellerResponse<T> tellerResponse) {
        tellerResponse.setCode(e.getErrorCode());
        tellerResponse.setStatus(e.getErrorMessage());
        return new ResponseEntity<>(tellerResponse, HttpStatus.BAD_REQUEST);
    }

    private static <T> ResponseEntity<TellerResponse<T>> handleForbiddenException(ForbiddenException ex, TellerResponse<T> tellerResponse) {
        tellerResponse.setCode(ex.getErrorCode());
        tellerResponse.setStatus(ex.getErrorMessage());
        return new ResponseEntity<>(tellerResponse, HttpStatus.FORBIDDEN);
    }
}
