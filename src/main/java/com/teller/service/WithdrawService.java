package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.WithdrawRequest;
import com.teller.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final AccountRepository accountRepository;

    public TellerResponse withdraw(WithdrawRequest request) {

        Account accountTransfer = accountRepository.findByAccountId(request.getAccountId());
        if (Objects.isNull(accountTransfer)) {
            return createResponse(ResponseCode.NOT_FOUND_ACCOUNT.getCode(), ResponseCode.NOT_FOUND_ACCOUNT.getDesc());
        }

        Map<String, String> validationError = validateWithdrawRequest(request, accountTransfer);
        if (validationError != null) {
            return createResponse(validationError.get("code"), validationError.get("desc"));
        }

        performWithdrawal(accountTransfer, request.getAmount());
        return createResponse(ResponseCode.SUCCESS_WITHDRAW.getCode(), ResponseCode.SUCCESS_WITHDRAW.getDesc());
    }

    private Map<String, String> validateWithdrawRequest(WithdrawRequest request, Account accountTransfer) {
        Map<String, String> response = new HashMap<>();

        if (!accountTransfer.getAccountId().equals(request.getAccountId())) {
            response.put("code", ResponseCode.NOT_FOUND_ACCOUNT.getCode());
            response.put("desc", ResponseCode.NOT_FOUND_ACCOUNT.getDesc());
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
    private void performWithdrawal(Account accountTransfer, double amount) {
        log.info("Before withdrawal: {}", accountTransfer.getAmount());
        accountTransfer.setAmount(accountTransfer.getAmount() - amount);
        accountRepository.save(accountTransfer);
        log.info("After withdrawal: {}",accountTransfer.getAmount() );
    }

    private TellerResponse createResponse(String code, String message) {
        TellerResponse response = new TellerResponse();
        response.setCode(code);
        response.setStatus(message);
        return response;
    }
}
