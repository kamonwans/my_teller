package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.TransferToAccountRequest;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static com.teller.utils.CommonUtils.getCalendarDateWithoutTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;
    private static final String SERVICE_NAME = "teller-service";

    @SneakyThrows
    public TellerResponse transfer(TransferRequest request) throws ForbiddenException, CommonException {
        TellerResponse response = new TellerResponse();

        double totalAmount = calculateAmountTransToAccounts(request);
        Account accountTransfer = fetchAccount(request.getFromAccountId());
        validateTransfer(totalAmount, request.getFromAccountId(), accountTransfer);

        if (accountTransfer.getAccountId().equals(request.getFromAccountId())) {
            for (TransferToAccountRequest accountTo : request.getTransferToAccountRequestList()) {
                Account accountToUpdate = fetchAccount(accountTo.getToAccountId());
                if (Objects.isNull(accountToUpdate)) {
                    handleExceptionError();
                } else {
                    filterAccountAndUpdateToAccountTransfer(accountTo);
                }
            }

            validateAmount(accountTransfer, totalAmount, response);
        } else {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        return response;
    }

    private void validateAmount(Account accountTransfer, double totalAmount, TellerResponse response) throws ForbiddenException {
        if (accountTransfer.getAmount() >= totalAmount) {
            updateTransferOwnerAccount(accountTransfer, totalAmount);
            response.setCode(ResponseCode.SUCCESS_TRANSFER.getCode());
            response.setStatus(ResponseCode.SUCCESS_TRANSFER.getDesc());
        } else {
            throw new ForbiddenException(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getDesc(), SERVICE_NAME, HttpStatus.FORBIDDEN);
        }
    }

    private static void handleExceptionError() throws CommonException {
        try {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        } catch (CommonException e) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    @SneakyThrows
    private void filterAccountAndUpdateToAccountTransfer(TransferToAccountRequest accountTo) throws CommonException {
        Account accountToUpdate = fetchAccount(accountTo.getToAccountId());
        if (Objects.nonNull(accountToUpdate)) {
            updateRecipientAccount(accountTo, accountToUpdate);
        } else {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    private static double calculateAmountTransToAccounts(TransferRequest request) {
        return request.getTransferToAccountRequestList()
                .stream()
                .mapToDouble(TransferToAccountRequest::getAmount)
                .sum();
    }

    private void validateTransfer(double amount, String accountId, Account accountTransfer) throws CommonException {
        if (amount <= 0) {
            throw new CommonException(ResponseCode.INVALID_AMOUNT.getCode(), ResponseCode.INVALID_AMOUNT.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (StringUtil.isNullOrEmpty(accountId)) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(accountTransfer) || !accountId.equals(accountTransfer.getAccountId())) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }

    private void updateRecipientAccount(TransferToAccountRequest accountTo, Account accountToUpdate) {
        accountToUpdate.setAmount(accountToUpdate.getAmount() + accountTo.getAmount());
        accountToUpdate.setUpdateDate(new Date());
        accountRepository.save(accountToUpdate);
    }

    private void updateTransferOwnerAccount(Account accountTransfer, double totalAmount) {
        accountTransfer.setAmount(accountTransfer.getAmount() - totalAmount);
        accountTransfer.setUpdateDate(getCalendarDateWithoutTime());
        accountRepository.save(accountTransfer);
    }

    private Account fetchAccount(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}
