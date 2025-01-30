package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.AccountIdModel;
import com.teller.model.AmountModel;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryService {

    private final AccountRepository accountRepository;
    private static final String SERVICE_NAME = "teller-service";

    public AmountModel inquiry(AccountIdModel request) throws CommonException {
        AmountModel amountModel = new AmountModel();
        Account account = accountRepository.findByAccountId(request.getAccountId());
        validateDepositRequest(request.getAccountId(), account);

        amountModel.setAmount(String.valueOf(account.getAmount()));
        return amountModel;
    }

    private void validateDepositRequest(String accountId, Account accountDeposit) throws CommonException {
        if (StringUtil.isNullOrEmpty(accountId)) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(accountDeposit) || !accountId.equals(accountDeposit.getAccountId())) {
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        }
    }
}
