package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;

    public TellerResponse deposit(DepositRequest request) throws CommonException {
        validateDepositRequest(request.getAmount(), request.getAccountId());

        Account accountDeposit = accountRepository.findByAccountId(request.getAccountId());
        Map<String, String> validationError = validateWithdrawRequest(request, accountDeposit);
        if (validationError != null) {
            return createResponse(validationError.get("code"), validationError.get("desc"));
        }

        updateDeposit(accountDeposit, request.getAmount());
        return createResponse(ResponseCode.SUCCESS_DEPOSIT.getCode(), ResponseCode.SUCCESS_DEPOSIT.getDesc());
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
                    ResponseCode.FAILED.getCode(),
                    ResponseCode.FAILED.getDesc(),
                    "teller-service",
                    HttpStatus.FORBIDDEN
            );
        }
    }

    private Map<String, String> validateWithdrawRequest(DepositRequest request, Account accountTransfer) {
        Map<String, String> response = new HashMap<>();

        if (!accountTransfer.getAccountId().equals(request.getAccountId())) {
            response.put("code", ResponseCode.FAILED.getCode());
            response.put("desc", ResponseCode.FAILED.getDesc());
            return response;
        }
        if (request.getAmount() <= 0) {
            response.put("code", ResponseCode.INVALID_AMOUNT.getCode());
            response.put("desc", ResponseCode.INVALID_AMOUNT.getDesc());
            return response;
        }
        if (request.getAmount() > accountTransfer.getAmount()) {
            response.put("code", ResponseCode.NOT_FOUND.getCode());
            response.put("desc", ResponseCode.NOT_FOUND.getDesc());
            return response;
        }
        return null;
    }

    private void updateDeposit(Account accountTransfer, double amount) {
        log.info("Before withdrawal: {}", accountTransfer.getAmount());
        accountTransfer.setAmount(accountTransfer.getAmount() - amount);
        accountRepository.save(accountTransfer);
        log.info("After withdrawal: {}",accountTransfer.getAmount() );
    }

    private TellerResponse createResponse(String code, String status) {
        TellerResponse response = new TellerResponse();
        response.setCode(code);
        response.setStatus(status);
        return response;
    }
}
