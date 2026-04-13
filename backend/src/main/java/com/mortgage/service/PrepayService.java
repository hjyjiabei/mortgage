package com.mortgage.service;

import com.mortgage.enums.RepaymentMethod;
import com.mortgage.exception.BusinessException;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PrepayCompareResponse;
import com.mortgage.model.dto.PrepayCompareResponse.CompareDataDTO;
import com.mortgage.model.dto.PrepayCompareResponse.LoanInfoDTO;
import com.mortgage.model.dto.PrepayCompareResponse.PrepayResultDTO;
import com.mortgage.model.dto.PrepaySimulateRequest;
import com.mortgage.util.CalculatorUtil;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class PrepayService {

    public PrepayCompareResponse simulatePrepay(PrepaySimulateRequest request) {
        if (request.getPrepayAmount().compareTo(request.getRemainingPrincipal()) >= 0) {
            throw new BusinessException("提前还款金额不能超过或等于剩余本金");
        }

        BigDecimal actualAnnualRate = CalculatorUtil.calculateActualAnnualRate(
            request.getAnnualRate(), 
            request.getRateFloatBp()
        );
        RepaymentMethod method = RepaymentMethod.valueOf(request.getRepaymentMethod());
        
        LocalDate nextPaymentDate = request.getNextPaymentDate();
        LocalDate baseDate = nextPaymentDate.minusMonths(1);

        List<DetailDTO> originalDetails = CalculatorUtil.calculate(
            request.getRemainingPrincipal(),
            actualAnnualRate,
            request.getRemainingTerm(),
            baseDate,
            method
        );

        BigDecimal originalTotalInterest = CalculatorUtil.calculateTotalInterest(originalDetails);
        BigDecimal originalMonthlyPayment = originalDetails.get(0).getMonthlyPayment();

        PrepayCompareResponse response = new PrepayCompareResponse();

        LoanInfoDTO originalLoan = new LoanInfoDTO();
        originalLoan.setRemainingPrincipal(request.getRemainingPrincipal());
        originalLoan.setAnnualRate(request.getAnnualRate());
        originalLoan.setRateFloatBp(request.getRateFloatBp() != null ? request.getRateFloatBp() : 0);
        originalLoan.setActualAnnualRate(actualAnnualRate);
        originalLoan.setRemainingTerm(request.getRemainingTerm());
        originalLoan.setRepaymentMethod(method.name());
        originalLoan.setRepaymentMethodName(method.getName());
        originalLoan.setMonthlyPayment(originalMonthlyPayment);
        originalLoan.setTotalInterest(originalTotalInterest);
        response.setOriginalLoan(originalLoan);

        BigDecimal remainingPrincipalAfter = CalculatorUtil.calculateRemainingPrincipalAfterPrepay(
            request.getRemainingPrincipal(), 
            request.getPrepayAmount()
        );

        PrepayResultDTO shortenTermResult = calculateShortenTerm(
            remainingPrincipalAfter, 
            originalMonthlyPayment,
            actualAnnualRate,
            baseDate,
            method,
            originalTotalInterest,
            request.getPrepayAmount()
        );
        response.setShortenTermResult(shortenTermResult);

        PrepayResultDTO reducePaymentResult = calculateReducePayment(
            remainingPrincipalAfter,
            request.getRemainingTerm(),
            actualAnnualRate,
            baseDate,
            method,
            originalTotalInterest,
            request.getPrepayAmount()
        );
        response.setReducePaymentResult(reducePaymentResult);

        CompareDataDTO compareData = new CompareDataDTO();
        compareData.setTermDiff(request.getRemainingTerm() - shortenTermResult.getNewTerm());
        compareData.setMonthlyPaymentDiff(shortenTermResult.getNewMonthlyPayment().subtract(reducePaymentResult.getNewMonthlyPayment()));
        compareData.setSavedInterestDiff(shortenTermResult.getSavedInterest().subtract(reducePaymentResult.getSavedInterest()));
        compareData.setRecommendation(generateRecommendation(shortenTermResult, reducePaymentResult));
        response.setCompareData(compareData);

        return response;
    }

    private PrepayResultDTO calculateShortenTerm(BigDecimal remainingPrincipal, 
                                                   BigDecimal originalMonthlyPayment,
                                                   BigDecimal actualAnnualRate,
                                                   LocalDate baseDate,
                                                   RepaymentMethod method,
                                                   BigDecimal originalTotalInterest,
                                                   BigDecimal prepayAmount) {
        PrepayResultDTO result = new PrepayResultDTO();
        result.setPrepayAmount(prepayAmount);
        result.setRemainingPrincipalAfter(remainingPrincipal);

        int newTerm;
        BigDecimal newMonthlyPayment;

        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            newTerm = CalculatorUtil.calculateNewTermShorten(remainingPrincipal, originalMonthlyPayment, actualAnnualRate);
            if (newTerm <= 0) {
                newTerm = 1;
            }
            newMonthlyPayment = originalMonthlyPayment;
            
            result.setNewTerm(newTerm);
            result.setNewMonthlyPayment(newMonthlyPayment);
            
            List<DetailDTO> newDetails = CalculatorUtil.calculateEqualPrincipalInterestWithFixedPayment(
                remainingPrincipal, 
                actualAnnualRate, 
                newTerm, 
                baseDate, 
                originalMonthlyPayment
            );
            
            BigDecimal newTotalInterest = originalMonthlyPayment.multiply(BigDecimal.valueOf(newTerm))
                .subtract(remainingPrincipal)
                .setScale(2, RoundingMode.HALF_UP);
            
            result.setNewTotalInterest(newTotalInterest);
            result.setSavedInterest(originalTotalInterest.subtract(newTotalInterest).setScale(2, RoundingMode.HALF_UP));
            result.setSavedInterestRatio(calculateSavedRatio(originalTotalInterest, newTotalInterest));
            result.setDetails(newDetails);
        } else {
            BigDecimal firstMonthInterest = remainingPrincipal.multiply(actualAnnualRate)
                .divide(BigDecimal.valueOf(12), 12, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);
            BigDecimal targetMonthlyPrincipal = originalMonthlyPayment.subtract(firstMonthInterest);
            if (targetMonthlyPrincipal.compareTo(BigDecimal.ZERO) <= 0) {
                targetMonthlyPrincipal = remainingPrincipal.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_UP);
            }
            newTerm = remainingPrincipal.divide(targetMonthlyPrincipal, 0, RoundingMode.HALF_UP).intValue();
            if (newTerm <= 0) {
                newTerm = 1;
            }
            BigDecimal monthlyPrincipal = remainingPrincipal.divide(BigDecimal.valueOf(newTerm), 2, RoundingMode.HALF_UP);
            newMonthlyPayment = monthlyPrincipal.add(firstMonthInterest);
            
            result.setNewTerm(newTerm);
            result.setNewMonthlyPayment(newMonthlyPayment);

            List<DetailDTO> newDetails = CalculatorUtil.calculate(
                remainingPrincipal, 
                actualAnnualRate, 
                newTerm, 
                baseDate, 
                method
            );

            result.setNewMonthlyPayment(newDetails.get(0).getMonthlyPayment());

            BigDecimal newTotalInterest = CalculatorUtil.calculateTotalInterest(newDetails);
            result.setNewTotalInterest(newTotalInterest);
            result.setSavedInterest(originalTotalInterest.subtract(newTotalInterest).setScale(2, RoundingMode.HALF_UP));
            result.setSavedInterestRatio(calculateSavedRatio(originalTotalInterest, newTotalInterest));
            result.setDetails(newDetails);
        }

        return result;
    }

    private PrepayResultDTO calculateReducePayment(BigDecimal remainingPrincipal,
                                                     int remainingTerm,
                                                     BigDecimal actualAnnualRate,
                                                     LocalDate baseDate,
                                                     RepaymentMethod method,
                                                     BigDecimal originalTotalInterest,
                                                     BigDecimal prepayAmount) {
        PrepayResultDTO result = new PrepayResultDTO();
        result.setPrepayAmount(prepayAmount);
        result.setRemainingPrincipalAfter(remainingPrincipal);
        result.setNewTerm(remainingTerm);

        List<DetailDTO> newDetails = CalculatorUtil.calculate(
            remainingPrincipal, 
            actualAnnualRate, 
            remainingTerm, 
            baseDate, 
            method
        );

        BigDecimal newMonthlyPayment;
        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            newMonthlyPayment = CalculatorUtil.calculateNewMonthlyPaymentReduce(
                remainingPrincipal, 
                remainingTerm, 
                actualAnnualRate
            );
        } else {
            newMonthlyPayment = newDetails.get(0).getMonthlyPayment();
        }
        result.setNewMonthlyPayment(newMonthlyPayment);

        BigDecimal newTotalInterest = CalculatorUtil.calculateTotalInterest(newDetails);
        result.setNewTotalInterest(newTotalInterest);
        result.setSavedInterest(originalTotalInterest.subtract(newTotalInterest).setScale(2, RoundingMode.HALF_UP));
        result.setSavedInterestRatio(calculateSavedRatio(originalTotalInterest, newTotalInterest));
        result.setDetails(newDetails);

        return result;
    }

    private BigDecimal calculateSavedRatio(BigDecimal originalInterest, BigDecimal newInterest) {
        if (originalInterest == null || originalInterest.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return originalInterest.subtract(newInterest)
            .divide(originalInterest, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }

    private String generateRecommendation(PrepayResultDTO shortenTerm, PrepayResultDTO reducePayment) {
        BigDecimal savedDiff = shortenTerm.getSavedInterest().subtract(reducePayment.getSavedInterest());
        
        if (savedDiff.compareTo(BigDecimal.ZERO) > 0) {
            return "缩期方式可多节省利息" + savedDiff.setScale(2, RoundingMode.HALF_UP) + "元，适合希望尽快还清贷款的用户";
        } else if (savedDiff.compareTo(BigDecimal.ZERO) < 0) {
            return "减月供方式可多节省利息" + savedDiff.abs().setScale(2, RoundingMode.HALF_UP) + "元，适合希望减轻月供压力的用户";
        } else {
            return "两种方式节省利息相同，可根据个人需求选择";
        }
    }
}