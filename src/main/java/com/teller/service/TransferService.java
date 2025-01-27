package com.teller.service;

import com.teller.model.TellerResponse;
import com.teller.model.TransferRequest;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    public TellerResponse deposit(TransferRequest request) {
        TellerResponse response = new TellerResponse();

        return response;
    }
}
