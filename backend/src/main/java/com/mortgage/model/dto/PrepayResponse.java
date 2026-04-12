package com.mortgage.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PrepayResponse {
    private Long originalPlanId;
    private Long newPlanId;
    private String newPlanNo;
    private Integer prepayPeriod;
    private LocalDate prepayDate;
    private BigDecimal prepayAmount;
    private String prepayType;
    private String prepayTypeName;
    private BigDecimal remainingPrincipalBefore;
    private BigDecimal remainingPrincipalAfter;
    private Integer originalRemainingTerm;
    private Integer newRemainingTerm;
    private BigDecimal originalMonthlyPayment;
    private BigDecimal newMonthlyPayment;
    private BigDecimal savedInterest;
    private PlanDTO newPlan;
    private List<DetailDTO> newDetails;

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

    public String getNewPlanNo() {
        return newPlanNo;
    }

    public void setNewPlanNo(String newPlanNo) {
        this.newPlanNo = newPlanNo;
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

    public String getPrepayTypeName() {
        return prepayTypeName;
    }

    public void setPrepayTypeName(String prepayTypeName) {
        this.prepayTypeName = prepayTypeName;
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

    public PlanDTO getNewPlan() {
        return newPlan;
    }

    public void setNewPlan(PlanDTO newPlan) {
        this.newPlan = newPlan;
    }

    public List<DetailDTO> getNewDetails() {
        return newDetails;
    }

    public void setNewDetails(List<DetailDTO> newDetails) {
        this.newDetails = newDetails;
    }
}
