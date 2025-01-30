package com.teller.service;

import com.teller.model.Account;
import com.teller.model.AccountRequest;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateService {

    private final AccountRepository accountRepository;

    public void create(AccountRequest request) throws CommonException {
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setAccountId(request.getAccountId());
        account.setAccountName(request.getAccountName());
        account.setCrmId(request.getCrmId());
        account.setAmount(request.getAmount());
        account.setCreateDate(CommonUtils.getCalendarDateWithoutTime());
        account.setUpdateDate(CommonUtils.getCalendarDateWithoutTime());
        accountRepository.save(account);
    }

}
