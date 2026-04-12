package com.mortgage.util;

import com.mortgage.common.Constants;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public final class PlanNoGenerator {
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final AtomicLong sequence = new AtomicLong(0);

    private PlanNoGenerator() {}

    public static String generate() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DATETIME_FORMATTER);
        long seq = sequence.incrementAndGet() % 10000;
        return Constants.PLAN_NO_PREFIX + timestamp + String.format("%04d", seq);
    }

    public static String generateWithPrefix(String prefix) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DATETIME_FORMATTER);
        long seq = sequence.incrementAndGet() % 10000;
        return prefix + timestamp + String.format("%04d", seq);
    }
}
