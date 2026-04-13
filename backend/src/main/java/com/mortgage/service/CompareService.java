package com.mortgage.service;

import com.mortgage.enums.RepaymentMethod;
import com.mortgage.mapper.CompareRecordMapper;
import com.mortgage.mapper.RepaymentPlanMapper;
import com.mortgage.model.dto.CompareRequest;
import com.mortgage.model.dto.CompareResponse;
import com.mortgage.model.dto.DetailDTO;
import com.mortgage.model.dto.PlanDTO;
import com.mortgage.util.CalculatorUtil;
import com.mortgage.util.DateUtil;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CompareService {
    private final CalculateService calculateService;
    private final CompareRecordMapper compareRecordMapper;
    private final RepaymentPlanMapper repaymentPlanMapper;

    public CompareService(CalculateService calculateService,
                          CompareRecordMapper compareRecordMapper,
                          RepaymentPlanMapper repaymentPlanMapper) {
        this.calculateService = calculateService;
        this.compareRecordMapper = compareRecordMapper;
        this.repaymentPlanMapper = repaymentPlanMapper;
    }

public CompareResponse compare(CompareRequest request) {
        BigDecimal actualAnnualRate = CalculatorUtil.calculateActualAnnualRate(
            request.getAnnualRate(), 
            request.getRateFloatBp()
        );
        List<DetailDTO> detailsA = CalculatorUtil.calculate(
            request.getLoanAmount(),
            actualAnnualRate,
            request.getLoanTerm(),
            request.getStartDate(),
            RepaymentMethod.EQUAL_PRINCIPAL_INTEREST
        );
        List<DetailDTO> detailsB = CalculatorUtil.calculate(
            request.getLoanAmount(),
            actualAnnualRate,
            request.getLoanTerm(),
            request.getStartDate(),
            RepaymentMethod.EQUAL_PRINCIPAL
        );
        PlanDTO planA = buildPlanDTO(detailsA, request, RepaymentMethod.EQUAL_PRINCIPAL_INTEREST, actualAnnualRate);
        PlanDTO planB = buildPlanDTO(detailsB, request, RepaymentMethod.EQUAL_PRINCIPAL, actualAnnualRate);
        CompareResponse response = new CompareResponse();
        response.setPlanA(planA);
        response.setPlanB(planB);
        response.setInterestDiff(planA.getTotalInterest().subtract(planB.getTotalInterest()));
        response.setFirstPaymentDiff(planA.getFirstPayment().subtract(planB.getFirstPayment()));
        response.setLastPaymentDiff(planA.getLastPayment().subtract(planB.getLastPayment()));
        response.setTotalPaymentDiff(planA.getTotalPayment().subtract(planB.getTotalPayment()));
        response.setInterestRatioDiff(planA.getInterestRatio().subtract(planB.getInterestRatio()));
        response.setRecommendation(generateRecommendation(response));
        return response;
    }

    private PlanDTO buildPlanDTO(List<DetailDTO> details, CompareRequest request, RepaymentMethod method, BigDecimal actualAnnualRate) {
        PlanDTO plan = new PlanDTO();
        plan.setLoanAmount(request.getLoanAmount());
        plan.setLoanTerm(request.getLoanTerm());
        plan.setActualTerm(request.getLoanTerm());
        plan.setAnnualRate(request.getAnnualRate());
        plan.setRateFloatBp(request.getRateFloatBp() != null ? request.getRateFloatBp() : 0);
        plan.setActualAnnualRate(actualAnnualRate);
        plan.setRepaymentMethod(method.name());
        plan.setRepaymentMethodName(method.getName());
        plan.setFirstPayment(details.get(0).getMonthlyPayment());
        plan.setLastPayment(details.get(details.size() - 1).getMonthlyPayment());
        plan.setTotalPayment(CalculatorUtil.calculateTotalPayment(details));
        plan.setTotalInterest(CalculatorUtil.calculateTotalInterest(details));
        plan.setTotalPrincipal(request.getLoanAmount());
        plan.setInterestRatio(CalculatorUtil.calculateInterestRatio(plan.getTotalInterest(), plan.getTotalPayment()));
        plan.setFirstPaymentDate(DateUtil.getFirstPaymentDate(request.getStartDate()));
        plan.setLastPaymentDate(DateUtil.getLastPaymentDate(request.getStartDate(), request.getLoanTerm()));
        if (method == RepaymentMethod.EQUAL_PRINCIPAL_INTEREST) {
            plan.setMonthlyPayment(details.get(0).getMonthlyPayment());
        }
        return plan;
    }

    private String generateRecommendation(CompareResponse response) {
        BigDecimal interestDiff = response.getInterestDiff();
        if (interestDiff.compareTo(BigDecimal.ZERO) > 0) {
            return "等额本金方式可节省" + interestDiff.setScale(2, RoundingMode.HALF_UP) + "元利息，但首月月供较高";
        } else if (interestDiff.compareTo(BigDecimal.ZERO) < 0) {
            return "等额本息方式可节省" + interestDiff.abs().setScale(2, RoundingMode.HALF_UP) + "元利息";
        } else {
            return "两种还款方式利息相同";
        }
    }
}
