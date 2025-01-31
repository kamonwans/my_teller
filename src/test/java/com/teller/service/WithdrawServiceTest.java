package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.AmountModel;
import com.teller.model.TellerResponse;
import com.teller.model.WithdrawRequest;
import com.teller.repository.AccountRepository;
import com.teller.utils.CommonException;
import com.teller.utils.ForbiddenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.apache.kafka.common.security.JaasUtils.SERVICE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void withdraw_Success() throws ForbiddenException, CommonException {
        doReturn(mockAccountData("1234567899", 2000.00)).when(accountRepository).findByAccountId(any());
        AmountModel withdraw = withdrawService.withdraw(mockWithdrawRequest(1000.00));
        Assertions.assertNotNull(withdraw);
    }

    @Test
    void withdraw_not_found_Success() {
        doReturn(mockAccountData("1234567899", 500.00)).when(accountRepository).findByAccountId(any());
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            withdrawService.withdraw(mockWithdrawRequest(1000.00));
            throw new ForbiddenException(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        });
        assertEquals(ResponseCode.NOT_FOUND.getCode(), exception.getMessage());
    }

    @Test
    void withdraw_failed_Success() {
        doReturn(mockAccountData("1234567891", 500.00)).when(accountRepository).findByAccountId(any());
        Exception exception = assertThrows(CommonException.class, () -> {
            withdrawService.withdraw(mockWithdrawRequest(1000.00));
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        });
        assertEquals(ResponseCode.FAILED.getCode(), exception.getMessage());
    }

    @Test
    void withdraw_invalid_amount_Success() throws ForbiddenException, CommonException {
        doReturn(mockAccountData("1234567899", 500.00)).when(accountRepository).findByAccountId(any());
        ForbiddenException exception = assertThrows(ForbiddenException.class, () -> {
            withdrawService.withdraw(mockWithdrawRequest(1000.00));
            throw new ForbiddenException(ResponseCode.INVALID_AMOUNT.getCode(), ResponseCode.INVALID_AMOUNT.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        });
        assertEquals(ResponseCode.INVALID_AMOUNT.getCode(), exception.getMessage());
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