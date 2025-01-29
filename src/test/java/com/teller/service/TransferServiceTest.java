package com.teller.service;

import com.teller.constant.ResponseCode;
import com.teller.model.Account;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.TransferToAccountRequest;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(JUnit4.class)
class TransferServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void transfer_Success() throws ForbiddenException, CommonException {
        doReturn(mockAccountData("1234567899", 2000.00)).when(accountRepository).findByAccountId(any());
        TellerResponse transfer = transferService.transfer(mockTransferRequest());
        Assertions.assertNotNull(transfer);
    }

    @Test
    void transfer_account_not_found_Success() throws ForbiddenException, CommonException {
        doReturn(mockAccountData("1234567891", 2000.00)).when(accountRepository).findByAccountId(any());
        TellerResponse transfer = transferService.transfer(mockTransferRequest());
        Assertions.assertEquals(ResponseCode.FAILED.getCode(), transfer.getCode());
        Assertions.assertEquals(ResponseCode.FAILED.getDesc(), transfer.getStatus());
    }

    @Test
    void transfer_amount_Success() throws ForbiddenException, CommonException {
        doReturn(mockAccountData("1234567899", 100.00)).when(accountRepository).findByAccountId(any());
        TellerResponse transfer = transferService.transfer(mockTransferRequest());
        Assertions.assertEquals(ResponseCode.NOT_FOUND.getCode(), transfer.getCode());
        Assertions.assertEquals(ResponseCode.NOT_FOUND.getDesc(), transfer.getStatus());
    }

    private Account mockAccountData(String accountId, double amount) {
        Account account = new Account();
        account.setCrmId("0000000000000000");
        account.setAccountId(accountId);
        account.setAmount(amount);
        return account;
    }

    private TransferRequest mockTransferRequest() {
        TransferRequest transferRequest = new TransferRequest();
        List<TransferToAccountRequest> accountRequestList = new ArrayList<>();
        TransferToAccountRequest transferToAccountRequest = new TransferToAccountRequest();
        transferRequest.setFromAccountId("1234567899");
        transferToAccountRequest.setAmount(1000.00);
        transferToAccountRequest.setToAccountId("0000000100");
        accountRequestList.add(transferToAccountRequest);
        transferRequest.setTransferToAccountRequestList(accountRequestList);
        return transferRequest;
    }
}