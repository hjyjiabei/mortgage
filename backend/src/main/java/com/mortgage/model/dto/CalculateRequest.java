package com.mortgage.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CalculateRequest {
    @NotNull(message = "贷款金额不能为空")
    @DecimalMin(value = "10000", message = "贷款金额不能低于10000元")
    @DecimalMax(value = "100000000", message = "贷款金额不能超过1亿元")
    private BigDecimal loanAmount;

    @NotNull(message = "贷款期限不能为空")
    @Min(value = 1, message = "贷款期限不能低于1个月")
    @Max(value = 360, message = "贷款期限不能超过360个月")
    private Integer loanTerm;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0.001", message = "年利率不能低于0.001")
    @DecimalMax(value = "1", message = "年利率不能超过1")
    private BigDecimal annualRate;

    @NotNull(message = "还款方式不能为空")
    @Pattern(regexp = "EQUAL_PRINCIPAL_INTEREST|EQUAL_PRINCIPAL", message = "还款方式必须为等额本息或等额本金")
    private String repaymentMethod;

    @NotNull(message = "放款日期不能为空")
    private LocalDate startDate;

    private String loanType;
    private String userId;
    private String remark;

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

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
