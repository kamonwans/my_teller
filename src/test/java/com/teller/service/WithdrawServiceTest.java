package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.WithdrawRequest;
import com.teller.repository.AccountRepository;
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

@RunWith(JUnit4.class)
class WithdrawServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private WithdrawService withdrawService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void withdraw_Success() {
        doReturn(mockAccountData("1234567899", 2000.00)).when(accountRepository).findByAccountId(any());
        TellerResponse withdraw = withdrawService.withdraw(mockWithdrawRequest(1000.00));
        Assertions.assertNotNull(withdraw);
    }

    @Test
    void withdraw_not_found_Success() {
        doReturn(mockAccountData("1234567899", 500.00)).when(accountRepository).findByAccountId(any());
        TellerResponse withdraw = withdrawService.withdraw(mockWithdrawRequest(1000.00));
        Assertions.assertEquals(ResponseCode.NOT_FOUND.getCode(), withdraw.getCode());
        Assertions.assertEquals(ResponseCode.NOT_FOUND.getDesc(), withdraw.getStatus());
    }

    @Test
    void withdraw_failed_Success() {
        doReturn(mockAccountData("1234567891", 500.00)).when(accountRepository).findByAccountId(any());
        TellerResponse withdraw = withdrawService.withdraw(mockWithdrawRequest(1000.00));
        Assertions.assertEquals(ResponseCode.FAILED.getCode(), withdraw.getCode());
        Assertions.assertEquals(ResponseCode.FAILED.getDesc(), withdraw.getStatus());
    }

    @Test
    void withdraw_invalid_amount_Success() {
        doReturn(mockAccountData("1234567899", 500.00)).when(accountRepository).findByAccountId(any());
        TellerResponse withdraw = withdrawService.withdraw(mockWithdrawRequest(0));
        Assertions.assertEquals(ResponseCode.INVALID_AMOUNT.getCode(), withdraw.getCode());
        Assertions.assertEquals(ResponseCode.INVALID_AMOUNT.getDesc(), withdraw.getStatus());
    }

    private Account mockAccountData(String accountId, double amount) {
        Account account = new Account();
        account.setCrmId("0000000000000000");
        account.setAccountId(accountId);
        account.setAmount(amount);
        return account;
    }

    private WithdrawRequest mockWithdrawRequest(double amount) {
        WithdrawRequest withdrawRequest = new WithdrawRequest();
        withdrawRequest.setAccountId("1234567899");
        withdrawRequest.setAmount(amount);
        return withdrawRequest;
    }
}