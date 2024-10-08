package com.bankingAppDemo.bankingAppDemo.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    public static String dateToString(LocalDateTime dateTime){
        return dateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }
}
