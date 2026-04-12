package com.mortgage.util;

import com.mortgage.enums.RepaymentMethod;
import com.mortgage.model.dto.DetailDTO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public final class CalculatorUtil {
    private static final int SCALE = 2;
    private static final int RATE_SCALE = 8;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private CalculatorUtil() {}

    public static BigDecimal calculateMonthlyRate(BigDecimal annualRate) {
        return annualRate.divide(BigDecimal.valueOf(12), RATE_SCALE, ROUNDING_MODE);
    }

    public static BigDecimal calculateEqualPrincipalInterestPayment(BigDecimal principal, BigDecimal monthlyRate, int term) {
        if (principal == null || monthlyRate == null || term <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal one = BigDecimal.ONE;
        BigDecimal ratePlusOne = one.add(monthlyRate);
        BigDecimal power = ratePlusOne.pow(term);
        BigDecimal numerator = principal.multiply(monthlyRate).multiply(power);
        BigDecimal denominator = power.subtract(one);
        return numerator.divide(denominator, SCALE, ROUNDING_MODE);
    }

    public static List<DetailDTO> calculateEqualPrincipalInterest(BigDecimal principal, BigDecimal annualRate, int term, LocalDate startDate) {
        List<DetailDTO> details = new ArrayList<>();
        BigDecimal monthlyRate = calculateMonthlyRate(annualRate);
        BigDecimal monthlyPayment = calculateEqualPrincipalInterestPayment(principal, monthlyRate, term);
        BigDecimal remainingPrincipal = principal;
        BigDecimal cumulativePayment = BigDecimal.ZERO;
        BigDecimal cumulativePrincipal = BigDecimal.ZERO;
        BigDecimal cumulativeInterest = BigDecimal.ZERO;

        for (int period = 1; period <= term; period++) {
            DetailDTO detail = new DetailDTO();
            detail.setPeriod(period);
            detail.setPaymentDate(DateUtil.calculatePaymentDate(startDate, period));
            detail.setMonthlyPayment(monthlyPayment);

            BigDecimal interest = remainingPrincipal.multiply(monthlyRate).setScale(SCALE, ROUNDING_MODE);
            BigDecimal principalPart = monthlyPayment.subtract(interest);
            
            if (period == term) {
                principalPart = remainingPrincipal;
                detail.setMonthlyPayment(principalPart.add(interest).setScale(SCALE, ROUNDING_MODE));
            }

            remainingPrincipal = remainingPrincipal.subtract(principalPart);
            if (remainingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
                remainingPrincipal = BigDecimal.ZERO;
            }

            cumulativePayment = cumulativePayment.add(detail.getMonthlyPayment());
            cumulativePrincipal = cumulativePrincipal.add(principalPart);
            cumulativeInterest = cumulativeInterest.add(interest);

            detail.setPrincipal(principalPart.setScale(SCALE, ROUNDING_MODE));
            detail.setInterest(interest);
            detail.setRemainingPrincipal(remainingPrincipal.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativePayment(cumulativePayment.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativePrincipal(cumulativePrincipal.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativeInterest(cumulativeInterest.setScale(SCALE, ROUNDING_MODE));

            details.add(detail);
        }
        return details;
    }

    public static List<DetailDTO> calculateEqualPrincipal(BigDecimal principal, BigDecimal annualRate, int term, LocalDate startDate) {
        List<DetailDTO> details = new ArrayList<>();
        BigDecimal monthlyRate = calculateMonthlyRate(annualRate);
        BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(term), SCALE, ROUNDING_MODE);
        BigDecimal remainingPrincipal = principal;
        BigDecimal cumulativePayment = BigDecimal.ZERO;
        BigDecimal cumulativePrincipal = BigDecimal.ZERO;
        BigDecimal cumulativeInterest = BigDecimal.ZERO;

        for (int period = 1; period <= term; period++) {
            DetailDTO detail = new DetailDTO();
            detail.setPeriod(period);
            detail.setPaymentDate(DateUtil.calculatePaymentDate(startDate, period));

            BigDecimal interest = remainingPrincipal.multiply(monthlyRate).setScale(SCALE, ROUNDING_MODE);
            BigDecimal principalPart = monthlyPrincipal;
            
            if (period == term) {
                principalPart = remainingPrincipal;
            }

            BigDecimal monthlyPayment = principalPart.add(interest);
            remainingPrincipal = remainingPrincipal.subtract(principalPart);
            if (remainingPrincipal.compareTo(BigDecimal.ZERO) < 0) {
                remainingPrincipal = BigDecimal.ZERO;
            }

            cumulativePayment = cumulativePayment.add(monthlyPayment);
            cumulativePrincipal = cumulativePrincipal.add(principalPart);
            cumulativeInterest = cumulativeInterest.add(interest);

            detail.setMonthlyPayment(monthlyPayment.setScale(SCALE, ROUNDING_MODE));
            detail.setPrincipal(principalPart.setScale(SCALE, ROUNDING_MODE));
            detail.setInterest(interest);
            detail.setRemainingPrincipal(remainingPrincipal.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativePayment(cumulativePayment.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativePrincipal(cumulativePrincipal.setScale(SCALE, ROUNDING_MODE));
            detail.setCumulativeInterest(cumulativeInterest.setScale(SCALE, ROUNDING_MODE));

            details.add(detail);
        }
        return details;
    }

    public static List<DetailDTO> calculate(BigDecimal principal, BigDecimal annualRate, int term, LocalDate startDate, RepaymentMethod method) {
        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            return calculateEqualPrincipalInterest(principal, annualRate, term, startDate);
        } else {
            return calculateEqualPrincipal(principal, annualRate, term, startDate);
        }
    }

    public static BigDecimal calculateTotalInterest(List<DetailDTO> details) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetailDTO detail : details) {
            total = total.add(detail.getInterest());
        }
        return total.setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal calculateTotalPayment(List<DetailDTO> details) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetailDTO detail : details) {
            total = total.add(detail.getMonthlyPayment());
        }
        return total.setScale(SCALE, ROUNDING_MODE);
    }

    public static BigDecimal calculateInterestRatio(BigDecimal totalInterest, BigDecimal totalPayment) {
        if (totalPayment == null || totalPayment.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return totalInterest.divide(totalPayment, 4, ROUNDING_MODE).multiply(BigDecimal.valueOf(100));
    }

    public static BigDecimal calculateRemainingPrincipalAfterPrepay(BigDecimal remainingPrincipal, BigDecimal prepayAmount) {
        BigDecimal after = remainingPrincipal.subtract(prepayAmount);
        return after.setScale(SCALE, ROUNDING_MODE);
    }

    public static int calculateNewTermShorten(BigDecimal remainingPrincipal, BigDecimal monthlyPayment, BigDecimal monthlyRate) {
        if (remainingPrincipal == null || remainingPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        if (monthlyPayment == null || monthlyPayment.compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        BigDecimal one = BigDecimal.ONE;
        BigDecimal ratePlusOne = one.add(monthlyRate);
        BigDecimal numerator = remainingPrincipal.multiply(monthlyRate);
        BigDecimal denominator = monthlyPayment.subtract(numerator);
        if (denominator.compareTo(BigDecimal.ZERO) <= 0) {
            return remainingPrincipal.divide(monthlyPayment, 0, ROUNDING_MODE).intValue();
        }
        double logValue = Math.log(monthlyPayment.doubleValue() / denominator.doubleValue());
        double logRate = Math.log(ratePlusOne.doubleValue());
        return (int) Math.round(logValue / logRate);
    }

    public static BigDecimal calculateNewMonthlyPaymentReduce(BigDecimal remainingPrincipal, int newTerm, BigDecimal monthlyRate) {
        return calculateEqualPrincipalInterestPayment(remainingPrincipal, monthlyRate, newTerm);
    }

    public static BigDecimal calculateSavedInterest(BigDecimal originalTotalInterest, BigDecimal newTotalInterest) {
        return originalTotalInterest.subtract(newTotalInterest).setScale(SCALE, ROUNDING_MODE);
    }
}
