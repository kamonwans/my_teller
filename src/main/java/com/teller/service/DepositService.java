package com.teller.service;

import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import org.springframework.stereotype.Service;

@Service
public class DepositService {

    public TellerResponse deposit(DepositRequest request) {
        TellerResponse response = new TellerResponse();

        return response;
    }
}
