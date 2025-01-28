package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;

    public TellerResponse deposit(DepositRequest request) throws CommonException {
        validateDepositRequest(request);

        try {
            Account account = accountRepository.findByAccountId(request.getAccountId());
            if (account == null) {
                return createResponse(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc());
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

    private void validateDepositRequest(DepositRequest request) throws CommonException {
        if (request.getAmount() <= 0) {
            throw new CommonException(
                    ResponseCode.INVALID_AMOUNT.getCode(),
                    ResponseCode.INVALID_AMOUNT.getDesc(),
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }
        if (request.getAccountId() == null || request.getAccountId().isEmpty()) {
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
