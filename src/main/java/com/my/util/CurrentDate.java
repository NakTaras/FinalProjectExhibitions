package com.my.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class CurrentDate {
    public static String getCurrentDate() {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        return simpleDateFormat.format(new java.util.Date());
        return LocalDate.now().toString();
    }
}
