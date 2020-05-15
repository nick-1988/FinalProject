package com.songnan.finalproject.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.*;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

public abstract class DateUtils {
    public static final String HISTORY_DATE = "MM,dd";

    private DateUtils() {
    }

    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }



}
