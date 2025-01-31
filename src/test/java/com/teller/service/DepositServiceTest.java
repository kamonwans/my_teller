package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.AmountModel;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
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

import java.text.ParseException;

import static org.apache.kafka.common.security.JaasUtils.SERVICE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

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
    void deposit_Success() throws CommonException, ForbiddenException, ParseException {
        doReturn(mockAccountData("1234567899", 2000.00)).when(accountRepository).findByAccountId(any());
        AmountModel deposit = depositService.deposit(mockDepositRequest(1000.00));
        Assertions.assertNotNull(deposit);
    }

    @Test
    void deposit_Failed()  {
        doReturn(mockAccountData("1234567891", 2000.00)).when(accountRepository).findByAccountId(any());
        Exception exception = assertThrows(CommonException.class, () -> {
            depositService.deposit(mockDepositRequest(10.0));
            throw new CommonException(ResponseCode.FAILED.getCode(), ResponseCode.FAILED.getDesc(), SERVICE_NAME, HttpStatus.BAD_REQUEST);
        });
        assertEquals(ResponseCode.FAILED.getCode(), exception.getMessage());
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