package com.teller.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teller.model.AmountModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@AllArgsConstructor
public class EncryptAmountUtils {

    public static String encryptAmount(String amount) {
        String plainText = String.valueOf(amount);
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    public static double decryptAmount(String encryptAmount) throws JsonProcessingException {
        byte[] decode = Base64.getDecoder().decode(encryptAmount);
        String decodedString = new String(decode);
        ObjectMapper objectMapper = new ObjectMapper();
        AmountModel amountModel = objectMapper.readValue(decodedString, AmountModel.class);
        return Double.parseDouble(amountModel.getAmount());
    }
}
