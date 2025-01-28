package com.teller.utils;

import java.util.Base64;

public class EncryptAmountUtils {

    public static String encryptAmount(double amount) {
        String plainText = String.valueOf(amount);
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    public static double decryptAmount(String encryptAmount) {
        byte[] decode = Base64.getDecoder().decode(encryptAmount);
        return Double.parseDouble(new String(decode));
    }
}
