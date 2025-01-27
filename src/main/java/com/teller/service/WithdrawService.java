package com.teller.service;

import com.teller.model.TellerResponse;
import com.teller.model.WithdrawRequest;
import org.springframework.stereotype.Service;

@Service
public class WithdrawService {

    public TellerResponse deposit(WithdrawRequest request) {
        TellerResponse response = new TellerResponse();

        return response;
    }
}
