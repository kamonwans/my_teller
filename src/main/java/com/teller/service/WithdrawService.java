package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.WithdrawRequest;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.teller.utils.CommonUtils.getCalendarDateWithoutTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawService {
    private final AccountRepository accountRepository;
    private static final String SERVICE_NAME = "teller-service";

    public TellerResponse withdraw(WithdrawRequest request) throws ForbiddenException, CommonException {
        Account accountTransfer = accountRepository.findByAccountId(request.getAccountId());
        validateWithdrawRequest(request, accountTransfer);
        updateWithdrawal(accountTransfer, request.getAmount());
        return createResponse(ResponseCode.SUCCESS_WITHDRAW.getCode(), ResponseCode.SUCCESS_WITHDRAW.getDesc());
    }

    private void validateWithdrawRequest(WithdrawRequest request, Account accountTransfer) throws ForbiddenException, CommonException {
        if (Objects.isNull(accountTransfer) || !accountTransfer.getAccountId().equals(request.getAccountId())) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
        if (request.getAmount() <= 0) {
            throw new ForbiddenException(ResponseCode.INVALID_AMOUNT.getCode(), ResponseCode.INVALID_AMOUNT.getDesc(), SERVICE_NAME, HttpStatus.FORBIDDEN);
        }
        if (request.getAmount() > accountTransfer.getAmount()) {
            throw new ForbiddenException(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateWithdrawal(Account accountTransfer, double amount) {
        log.info("Before withdrawal: {}", accountTransfer.getAmount());
        accountTransfer.setAmount(accountTransfer.getAmount() - amount);
        accountTransfer.setUpdateDate(getCalendarDateWithoutTime());
        accountRepository.save(accountTransfer);
        log.info("After withdrawal: {}", accountTransfer.getAmount());
    }

    private TellerResponse createResponse(String code, String message) {
        TellerResponse response = new TellerResponse();
        response.setCode(code);
        response.setStatus(message);
        return response;
    }

}
