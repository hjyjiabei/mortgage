package com.mortgage.service;

import com.mortgage.enums.RepaymentMethod;
import com.mortgage.mapper.LoanRequestMapper;
import com.mortgage.mapper.RepaymentDetailMapper;
import com.mortgage.mapper.RepaymentPlanMapper;
import com.mortgage.model.dto.CalculateRequest;
import com.mortgage.model.dto.CalculateResponse;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.entity.LoanRequest;
import com.mortgage.model.entity.RepaymentDetail;
import com.mortgage.model.entity.RepaymentPlan;
import com.mortgage.util.CalculatorUtil;
import com.mortgage.util.DateUtil;
import com.mortgage.util.DecimalUtil;
import com.mortgage.util.PlanNoGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculateService {
    private final LoanRequestMapper loanRequestMapper;
    private final RepaymentPlanMapper repaymentPlanMapper;
    private final RepaymentDetailMapper repaymentDetailMapper;

    public CalculateService(LoanRequestMapper loanRequestMapper,
                            RepaymentPlanMapper repaymentPlanMapper,
                            RepaymentDetailMapper repaymentDetailMapper) {
        this.loanRequestMapper = loanRequestMapper;
        this.repaymentPlanMapper = repaymentPlanMapper;
        this.repaymentDetailMapper = repaymentDetailMapper;
    }

    public CalculateResponse calculate(CalculateRequest request) {
        RepaymentMethod method = RepaymentMethod.valueOf(request.getRepaymentMethod());
        List<DetailDTO> details = CalculatorUtil.calculate(
            request.getLoanAmount(),
            request.getAnnualRate(),
            request.getLoanTerm(),
            request.getStartDate(),
            method
        );
        return buildResponse(request, details, method);
    }

    @Transactional
    public CalculateResponse calculateAndSave(CalculateRequest request) {
        RepaymentMethod method = RepaymentMethod.valueOf(request.getRepaymentMethod());
        List<DetailDTO> details = CalculatorUtil.calculate(
            request.getLoanAmount(),
            request.getAnnualRate(),
            request.getLoanTerm(),
            request.getStartDate(),
            method
        );
        LoanRequest loanRequest = saveLoanRequest(request);
        RepaymentPlan plan = saveRepaymentPlan(loanRequest, details, method);
        saveRepaymentDetails(plan.getId(), details);
        return buildResponse(request, details, method, plan);
    }

    private LoanRequest saveLoanRequest(CalculateRequest request) {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.setLoanAmount(request.getLoanAmount());
        loanRequest.setLoanTerm(request.getLoanTerm());
        loanRequest.setAnnualRate(request.getAnnualRate());
        loanRequest.setRepaymentMethod(request.getRepaymentMethod());
        loanRequest.setStartDate(request.getStartDate());
        loanRequest.setLoanType(request.getLoanType() != null ? request.getLoanType() : "COMMERCIAL");
        loanRequest.setUserId(request.getUserId());
        loanRequest.setRemark(request.getRemark());
        loanRequest.setCreatedAt(LocalDateTime.now());
        loanRequest.setUpdatedAt(LocalDateTime.now());
        loanRequestMapper.insert(loanRequest);
        return loanRequest;
    }

    private RepaymentPlan saveRepaymentPlan(LoanRequest loanRequest, List<DetailDTO> details, RepaymentMethod method) {
        RepaymentPlan plan = new RepaymentPlan();
        plan.setPlanNo(PlanNoGenerator.generate());
        plan.setLoanRequestId(loanRequest.getId());
        plan.setLoanAmount(loanRequest.getLoanAmount());
        plan.setLoanTerm(loanRequest.getLoanTerm());
        plan.setActualTerm(loanRequest.getLoanTerm());
        plan.setAnnualRate(loanRequest.getAnnualRate());
        plan.setMonthlyRate(CalculatorUtil.calculateMonthlyRate(loanRequest.getAnnualRate()));
        plan.setRepaymentMethod(method.name());
        plan.setFirstPayment(details.get(0).getMonthlyPayment());
        plan.setLastPayment(details.get(details.size() - 1).getMonthlyPayment());
        plan.setTotalPayment(CalculatorUtil.calculateTotalPayment(details));
        plan.setTotalInterest(CalculatorUtil.calculateTotalInterest(details));
        plan.setTotalPrincipal(loanRequest.getLoanAmount());
        plan.setInterestRatio(CalculatorUtil.calculateInterestRatio(plan.getTotalInterest(), plan.getTotalPayment()));
        plan.setFirstPaymentDate(DateUtil.getFirstPaymentDate(loanRequest.getStartDate()));
        plan.setLastPaymentDate(DateUtil.getLastPaymentDate(loanRequest.getStartDate(), loanRequest.getLoanTerm()));
        plan.setStatus(1);
        plan.setSource("CALCULATION");
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            plan.setMonthlyPayment(details.get(0).getMonthlyPayment());
        }
        repaymentPlanMapper.insert(plan);
        return plan;
    }

    private void saveRepaymentDetails(Long planId, List<DetailDTO> details) {
        List<RepaymentDetail> entities = details.stream().map(detail -> {
            RepaymentDetail entity = new RepaymentDetail();
            entity.setPlanId(planId);
            entity.setPeriod(detail.getPeriod());
            entity.setPaymentDate(detail.getPaymentDate());
            entity.setMonthlyPayment(detail.getMonthlyPayment());
            entity.setPrincipal(detail.getPrincipal());
            entity.setInterest(detail.getInterest());
            entity.setRemainingPrincipal(detail.getRemainingPrincipal());
            entity.setCumulativePayment(detail.getCumulativePayment());
            entity.setCumulativePrincipal(detail.getCumulativePrincipal());
            entity.setCumulativeInterest(detail.getCumulativeInterest());
            entity.setIsPrepaid(0);
            entity.setCreatedAt(LocalDateTime.now());
            return entity;
        }).collect(Collectors.toList());
        repaymentDetailMapper.batchInsert(entities);
    }

    private CalculateResponse buildResponse(CalculateRequest request, List<DetailDTO> details, RepaymentMethod method) {
        CalculateResponse response = new CalculateResponse();
        response.setLoanAmount(request.getLoanAmount());
        response.setLoanTerm(request.getLoanTerm());
        response.setAnnualRate(request.getAnnualRate());
        response.setMonthlyRate(CalculatorUtil.calculateMonthlyRate(request.getAnnualRate()));
        response.setRepaymentMethod(method.name());
        response.setRepaymentMethodName(method.getName());
        response.setFirstPayment(details.get(0).getMonthlyPayment());
        response.setLastPayment(details.get(details.size() - 1).getMonthlyPayment());
        response.setTotalPayment(CalculatorUtil.calculateTotalPayment(details));
        response.setTotalInterest(CalculatorUtil.calculateTotalInterest(details));
        response.setTotalPrincipal(request.getLoanAmount());
        response.setInterestRatio(CalculatorUtil.calculateInterestRatio(response.getTotalInterest(), response.getTotalPayment()));
        response.setFirstPaymentDate(DateUtil.getFirstPaymentDate(request.getStartDate()));
        response.setLastPaymentDate(DateUtil.getLastPaymentDate(request.getStartDate(), request.getLoanTerm()));
        response.setDetails(details);
        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            response.setMonthlyPayment(details.get(0).getMonthlyPayment());
        }
        return response;
    }

    private CalculateResponse buildResponse(CalculateRequest request, List<DetailDTO> details, RepaymentMethod method, RepaymentPlan plan) {
        CalculateResponse response = buildResponse(request, details, method);
        response.setPlanNo(plan.getPlanNo());
        return response;
    }
}
