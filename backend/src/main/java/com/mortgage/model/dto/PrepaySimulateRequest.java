package com.mortgage.model.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PrepaySimulateRequest {
    @NotNull(message = "未还本金不能为空")
    @DecimalMin(value = "1000", message = "未还本金不能低于1000元")
    @DecimalMax(value = "100000000", message = "未还本金不能超过1亿元")
    private BigDecimal remainingPrincipal;

    @NotNull(message = "年利率不能为空")
    @DecimalMin(value = "0.001", message = "年利率不能低于0.001")
    @DecimalMax(value = "1", message = "年利率不能超过1")
    private BigDecimal annualRate;

    @Min(value = -500, message = "利率浮动幅度不能低于-500bp")
    @Max(value = 500, message = "利率浮动幅度不能超过500bp")
    private Integer rateFloatBp;

    @NotNull(message = "剩余期数不能为空")
    @Min(value = 1, message = "剩余期数不能低于1个月")
    @Max(value = 360, message = "剩余期数不能超过360个月")
    private Integer remainingTerm;

    @NotNull(message = "还款方式不能为空")
    @Pattern(regexp = "EQUAL_PRINCIPAL_INTEREST|EQUAL_PRINCIPAL", message = "还款方式必须为等额本息或等额本金")
    private String repaymentMethod;

    @NotNull(message = "下次还款日期不能为空")
    private LocalDate nextPaymentDate;

    @NotNull(message = "提前还款金额不能为空")
    @DecimalMin(value = "1000", message = "提前还款金额不能低于1000元")
    private BigDecimal prepayAmount;

    private String remark;

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

    public LocalDate getNextPaymentDate() {
        return nextPaymentDate;
    }

    public void setNextPaymentDate(LocalDate nextPaymentDate) {
        this.nextPaymentDate = nextPaymentDate;
    }

    public BigDecimal getPrepayAmount() {
        return prepayAmount;
    }

    public void setPrepayAmount(BigDecimal prepayAmount) {
        this.prepayAmount = prepayAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}