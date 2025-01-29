package com.teller.controller;

import com.teller.controller.AuthenController;
import com.teller.model.DepositRequest;
import com.teller.model.TellerResponse;
import com.teller.utils.EncryptAmountUtils;
import com.teller.utils.TokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(JUnit4.class)
public class AuthenControllerTest {
    @Mock
    private TokenUtil tokenUtil;
    @Mock
    private EncryptAmountUtils encryptAmountUtils;
    @InjectMocks
    private AuthenController authenController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin() {
        doReturn("Bearer N3T_OB9Q0").when(tokenUtil).generateToken(any());
        ResponseEntity<String> login = authenController.login("0000000000");
        Assertions.assertNotNull(login);
    }
}