package com.mortgage.service;

import com.mortgage.enums.PrepayType;
import com.mortgage.enums.RepaymentMethod;
import com.mortgage.exception.BusinessException;
import com.mortgage.mapper.PrepayRecordMapper;
import com.mortgage.mapper.RepaymentDetailMapper;
import com.mortgage.mapper.RepaymentPlanMapper;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PrepayRequest;
import com.mortgage.model.dto.PrepayResponse;
import com.mortgage.model.entity.PrepayRecord;
import com.mortgage.model.entity.RepaymentDetail;
import com.mortgage.model.entity.RepaymentPlan;
import com.mortgage.util.CalculatorUtil;
import com.mortgage.util.DateUtil;
import com.mortgage.util.DecimalUtil;
import com.mortgage.util.PlanNoGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PrepayService {
    private final RepaymentPlanMapper repaymentPlanMapper;
    private final RepaymentDetailMapper repaymentDetailMapper;
    private final PrepayRecordMapper prepayRecordMapper;

    public PrepayService(RepaymentPlanMapper repaymentPlanMapper,
                         RepaymentDetailMapper repaymentDetailMapper,
                         PrepayRecordMapper prepayRecordMapper) {
        this.repaymentPlanMapper = repaymentPlanMapper;
        this.repaymentDetailMapper = repaymentDetailMapper;
        this.prepayRecordMapper = prepayRecordMapper;
    }

    public PrepayResponse simulatePrepay(PrepayRequest request) {
        RepaymentPlan originalPlan = repaymentPlanMapper.selectById(request.getPlanId());
        if (originalPlan == null) {
            throw new BusinessException("还款计划不存在");
        }
        RepaymentDetail detail = getDetailByPeriod(request.getPlanId(), request.getPrepayPeriod());
        if (detail == null) {
            throw new BusinessException("指定期数明细不存在");
        }
        BigDecimal remainingPrincipal = detail.getRemainingPrincipal();
        if (request.getPrepayAmount().compareTo(remainingPrincipal) > 0) {
            throw new BusinessException("提前还款金额不能超过剩余本金");
        }
        PrepayType prepayType = PrepayType.valueOf(request.getPrepayType());
        BigDecimal newPrincipal = CalculatorUtil.calculateRemainingPrincipalAfterPrepay(remainingPrincipal, request.getPrepayAmount());
        int originalRemainingTerm = originalPlan.getActualTerm() - request.getPrepayPeriod();
        BigDecimal monthlyRate = originalPlan.getMonthlyRate();
        BigDecimal originalTotalInterest = calculateFutureInterest(originalPlan, request.getPrepayPeriod());
        PrepayResponse response = new PrepayResponse();
        response.setOriginalPlanId(originalPlan.getId());
        response.setPrepayPeriod(request.getPrepayPeriod());
        response.setPrepayAmount(request.getPrepayAmount());
        response.setPrepayType(prepayType.name());
        response.setPrepayTypeName(prepayType.getName());
        response.setRemainingPrincipalBefore(remainingPrincipal);
        response.setRemainingPrincipalAfter(newPrincipal);
        response.setOriginalRemainingTerm(originalRemainingTerm);
        response.setOriginalMonthlyPayment(originalPlan.getMonthlyPayment() != null ? originalPlan.getMonthlyPayment() : detail.getMonthlyPayment());
        RepaymentMethod method = RepaymentMethod.valueOf(originalPlan.getRepaymentMethod());
        LocalDate startDate = request.getPrepayDate() != null ? request.getPrepayDate() : detail.getPaymentDate();
        if (prepayType == PrepayType.SHORTEN_TERM) {
            BigDecimal monthlyPayment = response.getOriginalMonthlyPayment();
            int newTerm = CalculatorUtil.calculateNewTermShorten(newPrincipal, monthlyPayment, monthlyRate);
            if (newTerm <= 0) {
                newTerm = 1;
            }
            response.setNewRemainingTerm(newTerm);
            response.setNewMonthlyPayment(monthlyPayment);
            List<DetailDTO> newDetails = CalculatorUtil.calculate(newPrincipal, originalPlan.getAnnualRate(), newTerm, startDate, method);
            BigDecimal newTotalInterest = CalculatorUtil.calculateTotalInterest(newDetails);
            response.setSavedInterest(originalTotalInterest.subtract(newTotalInterest));
        } else {
            int newTerm = originalRemainingTerm;
            BigDecimal newMonthlyPayment = CalculatorUtil.calculateNewMonthlyPaymentReduce(newPrincipal, newTerm, monthlyRate);
            response.setNewRemainingTerm(newTerm);
            response.setNewMonthlyPayment(newMonthlyPayment);
            List<DetailDTO> newDetails = CalculatorUtil.calculate(newPrincipal, originalPlan.getAnnualRate(), newTerm, startDate, method);
            BigDecimal newTotalInterest = CalculatorUtil.calculateTotalInterest(newDetails);
            response.setSavedInterest(originalTotalInterest.subtract(newTotalInterest));
        }
        return response;
    }

    private BigDecimal calculateFutureInterest(RepaymentPlan plan, int fromPeriod) {
        List<RepaymentDetail> details = repaymentDetailMapper.selectList(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RepaymentDetail>()
                .eq(RepaymentDetail::getPlanId, plan.getId())
                .ge(RepaymentDetail::getPeriod, fromPeriod)
        );
        BigDecimal total = BigDecimal.ZERO;
        for (RepaymentDetail detail : details) {
            total = total.add(detail.getInterest());
        }
        return total;
    }

    private RepaymentDetail getDetailByPeriod(Long planId, int period) {
        return repaymentDetailMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<RepaymentDetail>()
                .eq(RepaymentDetail::getPlanId, planId)
                .eq(RepaymentDetail::getPeriod, period)
        );
    }
}
