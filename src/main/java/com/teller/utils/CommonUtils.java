package com.teller.utils;


import com.teller.constant.ResponseCode;
import io.micrometer.common.util.StringUtils;
import org.springframework.http.HttpStatus;

public class CommonUtils {

    public static String formatBankAccountNo(String accountId) throws CommonException {
        try {
            if (StringUtils.isNotBlank(accountId)) {
                StringBuilder formatBankAccountNo = new StringBuilder(accountId);
                formatBankAccountNo.insert(3, "-");
                formatBankAccountNo.insert(5, "-");
                formatBankAccountNo.insert(11, "-");
                return formatBankAccountNo.toString();

            }
            return "-";
        }catch (Exception e) {
            throw new CommonException(ResponseCode.FAILED.getCode(), "data is wrong", "teller", HttpStatus.BAD_REQUEST, e);
        }
    }
}
