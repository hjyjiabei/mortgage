package com.mortgage.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DetailDTO {
    private Integer period;
    private LocalDate paymentDate;
    private BigDecimal monthlyPayment;
    private BigDecimal principal;
    private BigDecimal interest;
    private BigDecimal remainingPrincipal;
    private BigDecimal cumulativePayment;
    private BigDecimal cumulativePrincipal;
    private BigDecimal cumulativeInterest;

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(BigDecimal monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getRemainingPrincipal() {
        return remainingPrincipal;
    }

    public void setRemainingPrincipal(BigDecimal remainingPrincipal) {
        this.remainingPrincipal = remainingPrincipal;
    }

    public BigDecimal getCumulativePayment() {
        return cumulativePayment;
    }

    public void setCumulativePayment(BigDecimal cumulativePayment) {
        this.cumulativePayment = cumulativePayment;
    }

    public BigDecimal getCumulativePrincipal() {
        return cumulativePrincipal;
    }

    public void setCumulativePrincipal(BigDecimal cumulativePrincipal) {
        this.cumulativePrincipal = cumulativePrincipal;
    }

    public BigDecimal getCumulativeInterest() {
        return cumulativeInterest;
    }

    public void setCumulativeInterest(BigDecimal cumulativeInterest) {
        this.cumulativeInterest = cumulativeInterest;
    }
}
