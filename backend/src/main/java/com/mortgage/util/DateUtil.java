package com.mortgage.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public final class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateUtil() {}

    public static LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static String format(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(DATE_FORMATTER);
    }

    public static LocalDate calculatePaymentDate(LocalDate startDate, int period) {
        if (startDate == null) {
            return null;
        }
        LocalDate firstPaymentDate = startDate.plusMonths(1);
        return firstPaymentDate.plusMonths(period - 1);
    }

    public static LocalDate getFirstPaymentDate(LocalDate startDate) {
        if (startDate == null) {
            return null;
        }
        return startDate.plusMonths(1);
    }

    public static LocalDate getLastPaymentDate(LocalDate startDate, int totalTerm) {
        if (startDate == null) {
            return null;
        }
        return startDate.plusMonths(totalTerm);
    }

    public static int calculateRemainingTerm(LocalDate lastPaymentDate, LocalDate currentDate) {
        if (lastPaymentDate == null || currentDate == null) {
            return 0;
        }
YearMonth lastPaymentMonth = YearMonth.from(lastPaymentDate);
        YearMonth currentMonth = YearMonth.from(currentDate);
        long monthsDiff = ChronoUnit.MONTHS.between(currentMonth, lastPaymentMonth);
        return Math.max(0, (int) monthsDiff + 1);
    }

    public static boolean isAfterOrEqual(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return !date1.isBefore(date2);
    }
}
