package com.teller.controller;

import com.teller.constant.ResponseCode;
import com.teller.model.AmountModel;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import com.teller.model.WithdrawRequest;
import com.teller.service.DepositService;
import com.teller.service.TransferService;
import com.teller.service.ValidateTokenService;
import com.teller.service.WithdrawService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@RunWith(JUnit4.class)
class TellerControllerTest {
    @Mock
    private DepositService depositService;
    @Mock
    private ValidateTokenService validateTokenService;
    @Mock
    private WithdrawService withdrawService;
    @Mock
    private TransferService transferService;
    @InjectMocks
    private TellerController tellerController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void deposit_Success() throws Exception {
        TellerResponse tellerResponse = mockTellerDepositSuccess();
        doReturn(true).when(validateTokenService).validateToken(anyString());
        doReturn(tellerResponse).when(depositService).deposit(any());
        ResponseEntity<TellerResponse<AmountModel>> responseEntity = tellerController.deposit("Bearer N3T_OB9Q0", new DepositRequest());
        Assertions.assertNotNull(responseEntity);
    }

    @Test
    void withdraw_Success() throws Exception {
        TellerResponse tellerResponse = mockTellerDepositSuccess();
        doReturn(tellerResponse).when(withdrawService).withdraw(any());
        doReturn(true).when(validateTokenService).validateToken(anyString());
        ResponseEntity<TellerResponse<AmountModel>> responseEntity = tellerController.withdraw("Bearer N3T_OB9Q0", new WithdrawRequest());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertNotNull(responseEntity);
    }

    @Test
    void transfer_Success() throws Exception {
        TellerResponse tellerResponse = mockTellerDepositSuccess();
        doReturn(true).when(validateTokenService).validateToken(anyString());
        doReturn(tellerResponse).when(transferService).transfer(any());
        ResponseEntity<TellerResponse<AmountModel>> responseEntity = tellerController.transfer("Bearer N3T_OB9Q0", new TransferRequest());
        Assertions.assertNotNull(responseEntity);
    }

    private static TellerResponse mockTellerDepositSuccess() {
        TellerResponse tellerResponse = new TellerResponse();
        tellerResponse.setCode(ResponseCode.SUCCESS_DEPOSIT.getCode());
        tellerResponse.setStatus(ResponseCode.SUCCESS_DEPOSIT.getDesc());
        return tellerResponse;
    }


}