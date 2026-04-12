package com.mortgage.service;

import com.mortgage.exception.BusinessException;
import com.mortgage.mapper.RepaymentDetailMapper;
import com.mortgage.mapper.RepaymentPlanMapper;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PlanDTO;
import com.mortgage.model.entity.RepaymentDetail;
import com.mortgage.model.entity.RepaymentPlan;
import com.mortgage.enums.RepaymentMethod;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanService {
    private final RepaymentPlanMapper repaymentPlanMapper;
    private final RepaymentDetailMapper repaymentDetailMapper;

    public PlanService(RepaymentPlanMapper repaymentPlanMapper,
                       RepaymentDetailMapper repaymentDetailMapper) {
        this.repaymentPlanMapper = repaymentPlanMapper;
        this.repaymentDetailMapper = repaymentDetailMapper;
    }

    public PlanDTO getPlanById(Long planId) {
        RepaymentPlan plan = repaymentPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException("还款计划不存在");
        }
        return convertToPlanDTO(plan);
    }

    public List<DetailDTO> getDetailsByPlanId(Long planId) {
        List<RepaymentDetail> details = repaymentDetailMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RepaymentDetail>()
                .eq(RepaymentDetail::getPlanId, planId)
                .orderByAsc(RepaymentDetail::getPeriod)
        );
        return details.stream().map(this::convertToDetailDTO).collect(Collectors.toList());
    }

    public List<PlanDTO> getHistory(int page, int size) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<RepaymentPlan> pageParam =
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        List<RepaymentPlan> plans = repaymentPlanMapper.selectPage(pageParam,
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RepaymentPlan>()
                .eq(RepaymentPlan::getStatus, 1)
                .orderByDesc(RepaymentPlan::getCreatedAt)
        ).getRecords();
        return plans.stream().map(this::convertToPlanDTO).collect(Collectors.toList());
    }

    private PlanDTO convertToPlanDTO(RepaymentPlan plan) {
        PlanDTO dto = new PlanDTO();
        dto.setId(plan.getId());
        dto.setPlanNo(plan.getPlanNo());
        dto.setLoanAmount(plan.getLoanAmount());
        dto.setLoanTerm(plan.getLoanTerm());
        dto.setActualTerm(plan.getActualTerm());
        dto.setAnnualRate(plan.getAnnualRate());
        dto.setRepaymentMethod(plan.getRepaymentMethod());
        RepaymentMethod method = RepaymentMethod.valueOf(plan.getRepaymentMethod());
        dto.setRepaymentMethodName(method.getName());
        dto.setMonthlyPayment(plan.getMonthlyPayment());
        dto.setFirstPayment(plan.getFirstPayment());
        dto.setLastPayment(plan.getLastPayment());
        dto.setTotalPayment(plan.getTotalPayment());
        dto.setTotalInterest(plan.getTotalInterest());
        dto.setTotalPrincipal(plan.getTotalPrincipal());
        dto.setInterestRatio(plan.getInterestRatio());
        dto.setFirstPaymentDate(plan.getFirstPaymentDate());
        dto.setLastPaymentDate(plan.getLastPaymentDate());
        dto.setStatus(plan.getStatus());
        return dto;
    }

    private DetailDTO convertToDetailDTO(RepaymentDetail detail) {
        DetailDTO dto = new DetailDTO();
        dto.setPeriod(detail.getPeriod());
        dto.setPaymentDate(detail.getPaymentDate());
        dto.setMonthlyPayment(detail.getMonthlyPayment());
        dto.setPrincipal(detail.getPrincipal());
        dto.setInterest(detail.getInterest());
        dto.setRemainingPrincipal(detail.getRemainingPrincipal());
        dto.setCumulativePayment(detail.getCumulativePayment());
        dto.setCumulativePrincipal(detail.getCumulativePrincipal());
        dto.setCumulativeInterest(detail.getCumulativeInterest());
        return dto;
    }
}
