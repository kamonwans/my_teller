package com.teller.controller;

import com.teller.utils.EncryptAmountUtils;
import com.teller.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenController {
    private final TokenUtil tokenUtil;
    private final EncryptAmountUtils encryptAmountUtils;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String crmId) {
        String token = tokenUtil.generateToken(crmId);
        return ResponseEntity.ok("Bearer " + token);
    }


    @PostMapping("/encryption")
    public ResponseEntity<String> encryptionAmount(@RequestBody String amount) {
        String encryptAmount = encryptAmountUtils.encryptAmount(amount);
        return ResponseEntity.ok(encryptAmount);
    }

}
