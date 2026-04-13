package com.mortgage.model.dto;

import java.math.BigDecimal;
import java.util.List;

public class PrepayCompareResponse {
    private LoanInfoDTO originalLoan;
    private PrepayResultDTO shortenTermResult;
    private PrepayResultDTO reducePaymentResult;
    private CompareDataDTO compareData;

    public LoanInfoDTO getOriginalLoan() {
        return originalLoan;
    }

    public void setOriginalLoan(LoanInfoDTO originalLoan) {
        this.originalLoan = originalLoan;
    }

    public PrepayResultDTO getShortenTermResult() {
        return shortenTermResult;
    }

    public void setShortenTermResult(PrepayResultDTO shortenTermResult) {
        this.shortenTermResult = shortenTermResult;
    }

    public PrepayResultDTO getReducePaymentResult() {
        return reducePaymentResult;
    }

    public void setReducePaymentResult(PrepayResultDTO reducePaymentResult) {
        this.reducePaymentResult = reducePaymentResult;
    }

    public CompareDataDTO getCompareData() {
        return compareData;
    }

    public void setCompareData(CompareDataDTO compareData) {
        this.compareData = compareData;
    }

    public static class LoanInfoDTO {
        private BigDecimal remainingPrincipal;
        private BigDecimal annualRate;
        private Integer rateFloatBp;
        private BigDecimal actualAnnualRate;
        private Integer remainingTerm;
        private String repaymentMethod;
        private String repaymentMethodName;
        private BigDecimal monthlyPayment;
        private BigDecimal totalInterest;

        public BigDecimal getRemainingPrincipal() {
            return remainingPrincipal;
        }

        public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
            this.remainingPrincipal = remainingPrincipal;
        }

        public BigDecimal getAnnualRate() {
            return annualRate;
        }

        public void setAnnualRate(BigDecimal annualRate) {
            this.annualRate = annualRate;
        }

        public Integer getRateFloatBp() {
            return rateFloatBp;
        }

        public void setRateFloatBp(Integer rateFloatBp) {
            this.rateFloatBp = rateFloatBp;
        }

        public BigDecimal getActualAnnualRate() {
            return actualAnnualRate;
        }

        public void setActualAnnualRate(BigDecimal actualAnnualRate) {
            this.actualAnnualRate = actualAnnualRate;
        }

        public Integer getRemainingTerm() {
            return remainingTerm;
        }

        public void setRemainingTerm(Integer remainingTerm) {
            this.remainingTerm = remainingTerm;
        }

        public String getRepaymentMethod() {
            return repaymentMethod;
        }

        public void setRepaymentMethod(String repaymentMethod) {
            this.repaymentMethod = repaymentMethod;
        }

        public String getRepaymentMethodName() {
            return repaymentMethodName;
        }

        public void setRepaymentMethodName(String repaymentMethodName) {
            this.repaymentMethodName = repaymentMethodName;
        }

        public BigDecimal getMonthlyPayment() {
            return monthlyPayment;
        }

        public void setMonthlyPayment(BigDecimal monthlyPayment) {
            this.monthlyPayment = monthlyPayment;
        }

        public BigDecimal getTotalInterest() {
            return totalInterest;
        }

        public void setTotalInterest(BigDecimal totalInterest) {
            this.totalInterest = totalInterest;
        }
    }

    public static class PrepayResultDTO {
        private BigDecimal prepayAmount;
        private BigDecimal remainingPrincipalAfter;
        private Integer newTerm;
        private BigDecimal newMonthlyPayment;
        private BigDecimal newTotalInterest;
        private BigDecimal savedInterest;
        private BigDecimal savedInterestRatio;
        private List<DetailDTO> details;

        public BigDecimal getPrepayAmount() {
            return prepayAmount;
        }

        public void setPrepayAmount(BigDecimal prepayAmount) {
            this.prepayAmount = prepayAmount;
        }

        public BigDecimal getRemainingPrincipalAfter() {
            return remainingPrincipalAfter;
        }

        public void setRemainingPrincipalAfter(BigDecimal remainingPrincipalAfter) {
            this.remainingPrincipalAfter = remainingPrincipalAfter;
        }

        public Integer getNewTerm() {
            return newTerm;
        }

        public void setNewTerm(Integer newTerm) {
            this.newTerm = newTerm;
        }

        public BigDecimal getNewMonthlyPayment() {
            return newMonthlyPayment;
        }

        public void setNewMonthlyPayment(BigDecimal newMonthlyPayment) {
            this.newMonthlyPayment = newMonthlyPayment;
        }

        public BigDecimal getNewTotalInterest() {
            return newTotalInterest;
        }

        public void setNewTotalInterest(BigDecimal newTotalInterest) {
            this.newTotalInterest = newTotalInterest;
        }

        public BigDecimal getSavedInterest() {
            return savedInterest;
        }

        public void setSavedInterest(BigDecimal savedInterest) {
            this.savedInterest = savedInterest;
        }

        public BigDecimal getSavedInterestRatio() {
            return savedInterestRatio;
        }

        public void setSavedInterestRatio(BigDecimal savedInterestRatio) {
            this.savedInterestRatio = savedInterestRatio;
        }

        public List<DetailDTO> getDetails() {
            return details;
        }

        public void setDetails(List<DetailDTO> details) {
            this.details = details;
        }
    }

    public static class CompareDataDTO {
        private Integer termDiff;
        private BigDecimal monthlyPaymentDiff;
        private BigDecimal savedInterestDiff;
        private String recommendation;

        public Integer getTermDiff() {
            return termDiff;
        }

        public void setTermDiff(Integer termDiff) {
            this.termDiff = termDiff;
        }

        public BigDecimal getMonthlyPaymentDiff() {
            return monthlyPaymentDiff;
        }

        public void setMonthlyPaymentDiff(BigDecimal monthlyPaymentDiff) {
            this.monthlyPaymentDiff = monthlyPaymentDiff;
        }

        public BigDecimal getSavedInterestDiff() {
            return savedInterestDiff;
        }

        public void setSavedInterestDiff(BigDecimal savedInterestDiff) {
            this.savedInterestDiff = savedInterestDiff;
        }

        public String getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(String recommendation) {
            this.recommendation = recommendation;
        }
    }
}