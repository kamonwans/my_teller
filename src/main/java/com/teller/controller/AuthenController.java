package com.teller.controller;

import com.teller.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String crmId) {
        String token = TokenUtil.generateToken(crmId);
        return ResponseEntity.ok("Bearer " + token);
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validate(@RequestHeader("Authorization") String token) {
        String crmId = TokenUtil.validateToken(token.replace("Bearer ", ""));
        return ResponseEntity.ok("Valid token for crmId: " + crmId);
    }

    @GetMapping("/hello")
    public ResponseEntity<String> validate() {
        return ResponseEntity.ok("It's work");
    }

}
