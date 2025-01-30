package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.AmountModel;
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

    public AmountModel withdraw(WithdrawRequest request) throws ForbiddenException, CommonException {
        AmountModel amountModel = new AmountModel();
        Account accountTransfer = accountRepository.findByAccountId(request.getAccountId());
        validateWithdrawRequest(request, accountTransfer);
        updateWithdrawal(accountTransfer, request.getAmount());
        Account withdrawResponse = accountRepository.findByAccountId(request.getAccountId());
        amountModel.setAmount(String.valueOf(withdrawResponse.getAmount()));
        return amountModel;
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
}
