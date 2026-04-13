package com.mortgage.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CompareRequest {
    @NotNull(message = "贷款金额不能为空")
    @DecimalMin(value = "10000", message = "贷款金额不能低于10000元")
    private BigDecimal loanAmount;

    @NotNull(message = "贷款期限不能为空")
    @Min(value = 1, message = "贷款期限不能低于1个月")
    @Max(value = 360, message = "贷款期限不能超过360个月")
    private Integer loanTerm;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0.001", message = "年利率不能低于0.001")
    private BigDecimal annualRate;

    @Min(value = -500, message = "利率浮动幅度不能低于-500bp")
    @Max(value = 500, message = "利率浮动幅度不能超过500bp")
    private Integer rateFloatBp;

    @NotNull(message = "放款日期不能为空")
    private LocalDate startDate;

    private String userId;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
