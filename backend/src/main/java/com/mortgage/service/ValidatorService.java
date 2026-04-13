package com.mortgage.service;

import com.mortgage.common.Constants;
import com.mortgage.exception.BusinessException;
import com.mortgage.model.dto.CalculateRequest;
import com.mortgage.model.dto.PrepaySimulateRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ValidatorService {
    public void validateCalculateRequest(CalculateRequest request) {
        if (request.getLoanAmount() == null) {
            throw new BusinessException("贷款金额不能为空");
        }
        if (request.getLoanAmount().compareTo(BigDecimal.valueOf(Constants.MIN_LOAN_AMOUNT)) < 0) {
            throw new BusinessException("贷款金额不能低于" + Constants.MIN_LOAN_AMOUNT + "元");
        }
        if (request.getLoanAmount().compareTo(BigDecimal.valueOf(Constants.MAX_LOAN_AMOUNT)) > 0) {
            throw new BusinessException("贷款金额不能超过" + Constants.MAX_LOAN_AMOUNT + "元");
        }
        if (request.getLoanTerm() == null) {
            throw new BusinessException("贷款期限不能为空");
        }
        if (request.getLoanTerm() < Constants.MIN_LOAN_YEARS * 12) {
            throw new BusinessException("贷款期限不能低于" + Constants.MIN_LOAN_YEARS + "年");
        }
        if (request.getLoanTerm() > Constants.MAX_LOAN_YEARS * 12) {
            throw new BusinessException("贷款期限不能超过" + Constants.MAX_LOAN_YEARS + "年");
        }
        if (request.getAnnualRate() == null) {
            throw new BusinessException("年利率不能为空");
        }
        if (request.getAnnualRate().compareTo(BigDecimal.valueOf(0.001)) < 0) {
            throw new BusinessException("年利率不能低于0.1%");
        }
        if (request.getAnnualRate().compareTo(BigDecimal.ONE) > 0) {
            throw new BusinessException("年利率不能超过100%");
        }
        if (request.getStartDate() == null) {
            throw new BusinessException("放款日期不能为空");
        }
    }

    public void validatePrepaySimulateRequest(PrepaySimulateRequest request) {
        if (request.getRemainingPrincipal() == null) {
            throw new BusinessException("未还本金不能为空");
        }
        if (request.getRemainingPrincipal().compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new BusinessException("未还本金不能低于1000元");
        }
        if (request.getPrepayAmount() == null) {
            throw new BusinessException("提前还款金额不能为空");
        }
        if (request.getPrepayAmount().compareTo(BigDecimal.valueOf(1000)) < 0) {
            throw new BusinessException("提前还款金额不能低于1000元");
        }
        if (request.getPrepayAmount().compareTo(request.getRemainingPrincipal()) >= 0) {
            throw new BusinessException("提前还款金额不能超过或等于剩余本金");
        }
        if (request.getRemainingTerm() == null || request.getRemainingTerm() < 1) {
            throw new BusinessException("剩余期数不能为空或小于1");
        }
    }
}
