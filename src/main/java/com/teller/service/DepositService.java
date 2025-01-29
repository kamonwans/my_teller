package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static com.teller.utils.CommonUtils.getCalendarDateWithoutTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;
    private static final String SERVICE_NAME = "teller-service";

    public TellerResponse deposit(DepositRequest request) throws CommonException, ForbiddenException, ParseException {

        Account accountDeposit = accountRepository.findByAccountId(request.getAccountId());
        validateDepositRequest(request.getAmount(), request.getAccountId(), accountDeposit);

        updateDeposit(accountDeposit, request.getAmount());
        return createResponse(ResponseCode.SUCCESS_DEPOSIT.getCode(), ResponseCode.SUCCESS_DEPOSIT.getDesc());
    }

    private void validateDepositRequest(double amount, String accountId, Account accountDeposit) throws CommonException {
        if (amount <= 0) {
            throw new CommonException(ResponseCode.INVALID_AMOUNT.getCode(), ResponseCode.INVALID_AMOUNT.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (StringUtil.isNullOrEmpty(accountId)) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(accountDeposit) || !accountId.equals(accountDeposit.getAccountId())) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateDeposit(Account accountDeposit, double amount) throws ParseException {
        log.info("Before withdrawal: {}", accountDeposit.getAmount());
        accountDeposit.setAmount(accountDeposit.getAmount() + amount);
        accountDeposit.setUpdateDate(getCalendarDateWithoutTime());
        accountRepository.save(accountDeposit);
        log.info("After withdrawal: {}", accountDeposit.getAmount());
    }

    private TellerResponse createResponse(String code, String status) {
        TellerResponse response = new TellerResponse();
        response.setCode(code);
        response.setStatus(status);
        return response;
    }
}
