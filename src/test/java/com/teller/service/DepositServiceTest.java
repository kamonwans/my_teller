package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(JUnit4.class)
class DepositServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private DepositService depositService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void deposit_Success() throws CommonException {
        doReturn(mockAccountData("1234567899", 2000.00)).when(accountRepository).findByAccountId(any());
        TellerResponse withdraw = depositService.deposit(mockDepositRequest(1000.00));
        Assertions.assertNotNull(withdraw);
    }

    @Test
    void deposit_Failed() throws CommonException {
        doReturn(mockAccountData("1234567891", 2000.00)).when(accountRepository).findByAccountId(any());
        TellerResponse deposit = depositService.deposit(mockDepositRequest(1000.00));
        Assertions.assertEquals(ResponseCode.FAILED.getCode(), deposit.getCode());
        Assertions.assertEquals(ResponseCode.FAILED.getDesc(), deposit.getStatus());
    }

    private Account mockAccountData(String accountId, double amount) {
        Account account = new Account();
        account.setCrmId("0000000000000000");
        account.setAccountId(accountId);
        account.setAmount(amount);
        return account;
    }

    private DepositRequest mockDepositRequest(double amount) {
        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAccountId("1234567899");
        depositRequest.setAmount(amount);
        return depositRequest;
    }
}