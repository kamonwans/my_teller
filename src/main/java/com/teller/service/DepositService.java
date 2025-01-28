package com.teller.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.EncryptAmountUtils;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;

    public TellerResponse deposit(DepositRequest request) throws CommonException {
        validateDepositRequest(request.getAmount(), request.getAccountId());

        try {
            Account account = accountRepository.findByAccountId(request.getAccountId());
            if (Objects.isNull(account)) {
                return createResponse(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), ResponseCode.NOT_FOUND_ACCOUNT.getDesc());
            }

            account.setAmount(account.getAmount() + request.getAmount());
            accountRepository.save(account);
            return createResponse(ResponseCode.SUCCESS_DEPOSIT.getCode(), ResponseCode.SUCCESS_DEPOSIT.getDesc());

        } catch (Exception e) {
            throw new CommonException(
                    ResponseCode.DB_FAILED.getCode(),
                    ResponseCode.DB_FAILED.getDesc(),
                    "teller-service",
                    HttpStatus.BAD_REQUEST,
                    e
            );
        }
    }

    private void validateDepositRequest(double amount, String accountId) throws CommonException {
        if (amount <= 0) {
            throw new CommonException(
                    ResponseCode.INVALID_AMOUNT.getCode(),
                    ResponseCode.INVALID_AMOUNT.getDesc(),
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }
        if (StringUtil.isNullOrEmpty(accountId)) {
            throw new CommonException(
                    ResponseCode.NOT_FOUND_ACCOUNT.getCode(),
                    ResponseCode.NOT_FOUND_ACCOUNT.getDesc(),
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    private TellerResponse createResponse(String code, String status) {
        TellerResponse response = new TellerResponse();
        response.setCode(code);
        response.setStatus(status);
        return response;
    }
}
