package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.AmountModel;
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

import java.text.ParseException;
import java.util.Objects;

import static com.teller.utils.CommonUtils.getCalendarDateWithoutTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositService {

    private final AccountRepository accountRepository;
    private static final String SERVICE_NAME = "teller-service";

    public AmountModel deposit(DepositRequest request) throws CommonException, ForbiddenException {

        AmountModel amountModel = new AmountModel();
        Account accountDeposit = accountRepository.findByAccountId(request.getAccountId());
        validateDepositRequest(request.getAmount(), request.getAccountId(), accountDeposit);
        updateDeposit(accountDeposit, request.getAmount());
        Account depositResponse = accountRepository.findByAccountId(request.getAccountId());
        amountModel.setAmount(String.valueOf(depositResponse.getAmount()));
        return amountModel;
    }

    private void validateDepositRequest(double amount, String accountId, Account accountDeposit) throws CommonException, ForbiddenException {
        if (StringUtil.isNullOrEmpty(accountId)) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (amount <= 0) {
            throw new ForbiddenException(ResponseCode.INVALID_AMOUNT.getCode(), ResponseCode.INVALID_AMOUNT.getDesc(), SERVICE_NAME, HttpStatus.FORBIDDEN);
        }

        if (Objects.isNull(accountDeposit) || !accountId.equals(accountDeposit.getAccountId())) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateDeposit(Account accountDeposit, double amount) {
        log.info("Before withdrawal: {}", accountDeposit.getAmount());
        accountDeposit.setAmount(accountDeposit.getAmount() + amount);
        accountDeposit.setUpdateDate(getCalendarDateWithoutTime());
        accountRepository.save(accountDeposit);
        log.info("After withdrawal: {}", accountDeposit.getAmount());
    }
}
