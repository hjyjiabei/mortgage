package com.mortgage.model.dto;

import java.math.BigDecimal;

public class CompareResponse {
    private PlanDTO planA;
    private PlanDTO planB;
    private BigDecimal interestDiff;
    private BigDecimal firstPaymentDiff;
    private BigDecimal lastPaymentDiff;
    private BigDecimal totalPaymentDiff;
    private BigDecimal interestRatioDiff;
    private String recommendation;

    public PlanDTO getPlanA() {
        return planA;
    }

    public void setPlanA(PlanDTO planA) {
        this.planA = planA;
    }

    public PlanDTO getPlanB() {
        return planB;
    }

    public void setPlanB(PlanDTO planB) {
        this.planB = planB;
    }

    public BigDecimal getInterestDiff() {
        return interestDiff;
    }

    public void setInterestDiff(BigDecimal interestDiff) {
        this.interestDiff = interestDiff;
    }

    public BigDecimal getFirstPaymentDiff() {
        return firstPaymentDiff;
    }

    public void setFirstPaymentDiff(BigDecimal firstPaymentDiff) {
        this.firstPaymentDiff = firstPaymentDiff;
    }

    public BigDecimal getLastPaymentDiff() {
        return lastPaymentDiff;
    }

    public void setLastPaymentDiff(BigDecimal lastPaymentDiff) {
        this.lastPaymentDiff = lastPaymentDiff;
    }

    public BigDecimal getTotalPaymentDiff() {
        return totalPaymentDiff;
    }

    public void setTotalPaymentDiff(BigDecimal totalPaymentDiff) {
        this.totalPaymentDiff = totalPaymentDiff;
    }

    public BigDecimal getInterestRatioDiff() {
        return interestRatioDiff;
    }

    public void setInterestRatioDiff(BigDecimal interestRatioDiff) {
        this.interestRatioDiff = interestRatioDiff;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }
}
