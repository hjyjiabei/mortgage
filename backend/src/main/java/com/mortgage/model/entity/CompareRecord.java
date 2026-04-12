package com.mortgage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("compare_record")
public class CompareRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planAId;
    private Long planBId;
    private String planAMethod;
    private String planBMethod;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private BigDecimal annualRate;
    private BigDecimal interestDiff;
    private BigDecimal firstPaymentDiff;
    private BigDecimal lastPaymentDiff;
    private BigDecimal totalPaymentDiff;
    private BigDecimal interestRatioDiff;
    private String userId;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanAId() {
        return planAId;
    }

    public void setPlanAId(Long planAId) {
        this.planAId = planAId;
    }

    public Long getPlanBId() {
        return planBId;
    }

    public void setPlanBId(Long planBId) {
        this.planBId = planBId;
    }

    public String getPlanAMethod() {
        return planAMethod;
    }

    public void setPlanAMethod(String planAMethod) {
        this.planAMethod = planAMethod;
    }

    public String getPlanBMethod() {
        return planBMethod;
    }

    public void setPlanBMethod(String planBMethod) {
        this.planBMethod = planBMethod;
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

    public BigDecimal getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(BigDecimal annualRate) {
        this.annualRate = annualRate;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
