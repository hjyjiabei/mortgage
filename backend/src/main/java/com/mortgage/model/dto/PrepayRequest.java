package com.mortgage.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PrepayRequest {
    @NotNull(message = "计划ID不能为空")
    private Long planId;

    @NotNull(message = "提前还款期数不能为空")
    @Min(value = 1, message = "提前还款期数不能小于1")
    private Integer prepayPeriod;

    @NotNull(message = "提前还款金额不能为空")
    @DecimalMin(value = "1000", message = "提前还款金额不能低于1000元")
    private BigDecimal prepayAmount;

    @NotNull(message = "提前还款类型不能为空")
    @Pattern(regexp = "SHORTEN_TERM|REDUCE_PAYMENT", message = "提前还款类型必须为缩期或减月供")
    private String prepayType;

    private LocalDate prepayDate;

    private String remark;

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Integer getPrepayPeriod() {
        return prepayPeriod;
    }

    public void setPrepayPeriod(Integer prepayPeriod) {
        this.prepayPeriod = prepayPeriod;
    }

    public BigDecimal getPrepayAmount() {
        return prepayAmount;
    }

    public void setPrepayAmount(BigDecimal prepayAmount) {
        this.prepayAmount = prepayAmount;
    }

    public String getPrepayType() {
        return prepayType;
    }

    public void setPrepayType(String prepayType) {
        this.prepayType = prepayType;
    }

    public LocalDate getPrepayDate() {
        return prepayDate;
    }

    public void setPrepayDate(LocalDate prepayDate) {
        this.prepayDate = prepayDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
