package com.teller.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public enum ResponseCode implements Serializable {

    SUCCESS_DEPOSIT("200", "success", "การฝากเงินสำเร็จ"),
    SUCCESS_WITHDRAW("200", "success", "การถอนเงินสำเร็จ"),
    SUCCESS_TRANSFER("200", "success", "การโอนเงินสำเร็จ"),
    FAILED("400", "failed", "ข้อมูลไม่ถูกต้อง"),
    NOT_FOUND("403", "failed", "ยอดเงินไม่เพียงพอ"),
    NOT_FOUND_ACCOUNT("403", "failed", "ไม่พบบัญชี"),
    INVALID_AMOUNT("403", "failed", "จำนวนเงินถอนไม่ถูกต้อง จำนวนเงินต้องมากกว่า 0"),
    DB_FAILED("001", "Database error", "Cannot do this transaction"),
    INVALID_TOKEN("009", "Token", "Invalid token");


    private final String code;
    private final String message;
    private final String desc;

}
