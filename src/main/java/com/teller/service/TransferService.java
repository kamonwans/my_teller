package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.TransferToAccountRequest;
import com.teller.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;

    public TellerResponse transfer(TransferRequest request) {
        TellerResponse response = new TellerResponse();

        double totalAmount = request.getTransferToAccountRequestList()
                .stream()
                .mapToDouble(TransferToAccountRequest::getAmount)
                .sum();

        Account accountTransfer = fetchAccount(request.getFromAccountId());

        if (Objects.nonNull(accountTransfer) && accountTransfer.getAccountId().equals(request.getFromAccountId())) {
            if (accountTransfer.getAmount() >= totalAmount) {
                request.getTransferToAccountRequestList().forEach(accountTo -> {
                    Account accountToUpdate = fetchAccount(accountTo.getToAccountId());
                    if (accountToUpdate != null) {
                        log.info("Before update (toAccountId)): {}", accountToUpdate.getAmount());
                        accountToUpdate.setAmount(accountToUpdate.getAmount() + accountTo.getAmount());
                        accountRepository.save(accountToUpdate);
                        log.info("After update (toAccountId): {}", accountToUpdate.getAmount());

                    } else {
                        log.info("Account not found for toAccountId:: {}", accountTo.getToAccountId());

                    }
                });

                log.info("Before update (fromAccountId): {}", accountTransfer.getAmount());
                accountTransfer.setAmount(accountTransfer.getAmount() - totalAmount);
                accountRepository.save(accountTransfer);
                log.info("After update (fromAccountId): {}", accountTransfer.getAmount());

                response.setCode(ResponseCode.SUCCESS_TRANSFER.getCode());
                response.setStatus(ResponseCode.SUCCESS_TRANSFER.getDesc());
            } else {
                response.setCode(ResponseCode.NOT_FOUND.getCode());
                response.setStatus(ResponseCode.NOT_FOUND.getDesc());
                log.info("Insufficient balance in fromAccountId: {}", request.getFromAccountId());
            }
        } else {
            response.setCode(ResponseCode.FAILED.getCode());
            response.setStatus(ResponseCode.FAILED.getDesc());
            log.info("Invalid fromAccountId: {}", request.getFromAccountId());
        }


        return response;
    }

    private Account fetchAccount(String accountId) {
        return accountRepository.findByAccountId(accountId);
    }
}
