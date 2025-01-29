package com.teller.utils;


import java.sql.Timestamp;


public class CommonUtils {
    public static Timestamp getCalendarDateWithoutTime() {
        return new Timestamp(System.currentTimeMillis());
    }
}
