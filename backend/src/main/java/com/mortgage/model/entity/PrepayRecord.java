package com.mortgage.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("prepay_record")
public class PrepayRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long originalPlanId;
    private Long newPlanId;
    private Integer prepayPeriod;
    private LocalDate prepayDate;
    private BigDecimal prepayAmount;
    private String prepayType;
    private BigDecimal remainingPrincipalBefore;
    private BigDecimal remainingPrincipalAfter;
    private Integer originalRemainingTerm;
    private Integer newRemainingTerm;
    private BigDecimal originalMonthlyPayment;
    private BigDecimal newMonthlyPayment;
    private BigDecimal savedInterest;
    private String remark;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOriginalPlanId() {
        return originalPlanId;
    }

    public void setOriginalPlanId(Long originalPlanId) {
        this.originalPlanId = originalPlanId;
    }

    public Long getNewPlanId() {
        return newPlanId;
    }

    public void setNewPlanId(Long newPlanId) {
        this.newPlanId = newPlanId;
    }

    public Integer getPrepayPeriod() {
        return prepayPeriod;
    }

    public void setPrepayPeriod(Integer prepayPeriod) {
        this.prepayPeriod = prepayPeriod;
    }

    public LocalDate getPrepayDate() {
        return prepayDate;
    }

    public void setPrepayDate(LocalDate prepayDate) {
        this.prepayDate = prepayDate;
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

    public BigDecimal getRemainingPrincipalBefore() {
        return remainingPrincipalBefore;
    }

    public void setRemainingPrincipalBefore(BigDecimal remainingPrincipalBefore) {
        this.remainingPrincipalBefore = remainingPrincipalBefore;
    }

    public BigDecimal getRemainingPrincipalAfter() {
        return remainingPrincipalAfter;
    }

    public void setRemainingPrincipalAfter(BigDecimal remainingPrincipalAfter) {
        this.remainingPrincipalAfter = remainingPrincipalAfter;
    }

    public Integer getOriginalRemainingTerm() {
        return originalRemainingTerm;
    }

    public void setOriginalRemainingTerm(Integer originalRemainingTerm) {
        this.originalRemainingTerm = originalRemainingTerm;
    }

    public Integer getNewRemainingTerm() {
        return newRemainingTerm;
    }

    public void setNewRemainingTerm(Integer newRemainingTerm) {
        this.newRemainingTerm = newRemainingTerm;
    }

    public BigDecimal getOriginalMonthlyPayment() {
        return originalMonthlyPayment;
    }

    public void setOriginalMonthlyPayment(BigDecimal originalMonthlyPayment) {
        this.originalMonthlyPayment = originalMonthlyPayment;
    }

    public BigDecimal getNewMonthlyPayment() {
        return newMonthlyPayment;
    }

    public void setNewMonthlyPayment(BigDecimal newMonthlyPayment) {
        this.newMonthlyPayment = newMonthlyPayment;
    }

    public BigDecimal getSavedInterest() {
        return savedInterest;
    }

    public void setSavedInterest(BigDecimal savedInterest) {
        this.savedInterest = savedInterest;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
