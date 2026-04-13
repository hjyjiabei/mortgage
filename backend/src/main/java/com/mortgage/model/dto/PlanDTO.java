package com.mortgage.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PlanDTO {
    private Long id;
    private String planNo;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private Integer actualTerm;
    private BigDecimal annualRate;
    private Integer rateFloatBp;
    private BigDecimal actualAnnualRate;
    private String repaymentMethod;
    private String repaymentMethodName;
    private BigDecimal monthlyPayment;
    private BigDecimal firstPayment;
    private BigDecimal lastPayment;
    private BigDecimal totalPayment;
    private BigDecimal totalInterest;
    private BigDecimal totalPrincipal;
    private BigDecimal interestRatio;
    private LocalDate firstPaymentDate;
    private LocalDate lastPaymentDate;
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlanNo() {
        return planNo;
    }

    public void setPlanNo(String planNo) {
        this.planNo = planNo;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Integer getActualTerm() {
        return actualTerm;
    }

    public void setActualTerm(Integer actualTerm) {
        this.actualTerm = actualTerm;
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

    public BigDecimal getFirstPayment() {
        return firstPayment;
    }

    public void setFirstPayment(BigDecimal firstPayment) {
        this.firstPayment = firstPayment;
    }

    public BigDecimal getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(BigDecimal lastPayment) {
        this.lastPayment = lastPayment;
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getTotalPrincipal() {
        return totalPrincipal;
    }

    public void setTotalPrincipal(BigDecimal totalPrincipal) {
        this.totalPrincipal = totalPrincipal;
    }

    public BigDecimal getInterestRatio() {
        return interestRatio;
    }

    public void setInterestRatio(BigDecimal interestRatio) {
        this.interestRatio = interestRatio;
    }

    public LocalDate getFirstPaymentDate() {
        return firstPaymentDate;
    }

    public void setFirstPaymentDate(LocalDate firstPaymentDate) {
        this.firstPaymentDate = firstPaymentDate;
    }

    public LocalDate getLastPaymentDate() {
        return lastPaymentDate;
    }

    public void setLastPaymentDate(LocalDate lastPaymentDate) {
        this.lastPaymentDate = lastPaymentDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
