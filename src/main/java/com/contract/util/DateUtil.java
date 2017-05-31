package com.contract.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by julbarra on 5/31/17.
 */
public class DateUtil {

    public static long getDiffYears(LocalDate first, LocalDate last) {
        long years = last.until(first, ChronoUnit.YEARS);

        return years;
    }

    public static long getDiffMonths(LocalDate first, LocalDate last) {
        long months = last.until(first, ChronoUnit.MONTHS);

        return months;
    }
}
