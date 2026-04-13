package com.mortgage.util;

import com.mortgage.enums.RepaymentMethod;
import com.mortgage.model.dto.DetailDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CalculatorUtil 计算工具类测试")
class CalculatorUtilTest {

    private static final BigDecimal DELTA = new BigDecimal("0.01");
    private static final BigDecimal TOLERANCE = new BigDecimal("10");
    private static final int SCALE = 2;

    @Test
    @DisplayName("利率浮动计算 - 实际贷款数据验证")
    void testCalculateActualAnnualRate_realData() {
        BigDecimal baseRate = new BigDecimal("0.035");
        Integer rateFloatBp = -30;
        
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(baseRate, rateFloatBp);
        
        BigDecimal expected = new BigDecimal("0.032");
        assertTrue(actualRate.compareTo(expected) == 0, "基准利率3.5%下浮30bp应为3.2%");
    }

    @Test
    @DisplayName("利率浮动计算 - 上浮")
    void testCalculateActualAnnualRate_positive() {
        BigDecimal baseRate = new BigDecimal("0.043");
        Integer rateFloatBp = 50;
        
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(baseRate, rateFloatBp);
        
        BigDecimal expected = new BigDecimal("0.048");
        assertTrue(actualRate.compareTo(expected) == 0, "基准利率4.3%上浮50bp应为4.8%");
    }

    @Test
    @DisplayName("利率浮动计算 - 无浮动")
    void testCalculateActualAnnualRate_zero() {
        BigDecimal baseRate = new BigDecimal("0.032");
        Integer rateFloatBp = 0;
        
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(baseRate, rateFloatBp);
        
        assertEquals(baseRate, actualRate, "无浮动时实际利率应等于基准利率");
    }

    @Test
    @DisplayName("利率浮动计算 - null浮动值")
    void testCalculateActualAnnualRate_nullBp() {
        BigDecimal baseRate = new BigDecimal("0.032");
        
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(baseRate, null);
        
        assertEquals(baseRate, actualRate, "浮动值为null时应返回基准利率");
    }

    @Test
    @DisplayName("利率浮动计算 - null基准利率")
    void testCalculateActualAnnualRate_nullBase() {
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(null, -30);
        
        assertEquals(BigDecimal.ZERO, actualRate, "基准利率为null时应返回0");
    }

    @ParameterizedTest
    @ValueSource(ints = {-500, -200, -100, 100, 200, 500})
    @DisplayName("利率浮动计算 - 边界值测试")
    void testCalculateActualAnnualRate_boundary(int rateFloatBp) {
        BigDecimal baseRate = new BigDecimal("0.043");
        
        BigDecimal actualRate = CalculatorUtil.calculateActualAnnualRate(baseRate, rateFloatBp);
        
        BigDecimal adjustment = BigDecimal.valueOf(rateFloatBp)
            .divide(BigDecimal.valueOf(10000), 12, RoundingMode.HALF_UP);
        BigDecimal expected = baseRate.add(adjustment);
        
        assertTrue(actualRate.compareTo(expected) == 0, 
            "浮动" + rateFloatBp + "bp计算结果应正确");
    }

    @Test
    @DisplayName("月利率计算 - 实际贷款数据验证")
    void testCalculateMonthlyRate_realData() {
        BigDecimal annualRate = new BigDecimal("0.032");
        
        BigDecimal monthlyRate = CalculatorUtil.calculateMonthlyRate(annualRate);
        
        assertTrue(monthlyRate.compareTo(new BigDecimal("0.0026")) > 0 && monthlyRate.compareTo(new BigDecimal("0.0027")) < 0,
            "年利率3.2%月利率应约为0.266667%，实际:" + monthlyRate);
    }

    @Test
    @DisplayName("等额本息月供计算 - 实际贷款数据验证")
    void testCalculateEqualPrincipalInterestPayment_realData() {
        BigDecimal principal = new BigDecimal("1263983.31");
        BigDecimal annualRate = new BigDecimal("0.032");
        int remainingTerm = 275;
        
        BigDecimal monthlyPayment = CalculatorUtil.calculateEqualPrincipalInterestPayment(principal, annualRate, remainingTerm);
        
        BigDecimal expectedPayment = new BigDecimal("6491.63");
        assertTrue(monthlyPayment.subtract(expectedPayment).abs().compareTo(new BigDecimal("1")) < 0,
            "剩余本金1263983.31元，实际利率3.2%，剩余275期，月供应为6491.63元，实际计算:" + monthlyPayment);
        
        BigDecimal totalPayment = monthlyPayment.multiply(BigDecimal.valueOf(remainingTerm));
        BigDecimal totalInterest = totalPayment.subtract(principal);
        assertTrue(totalInterest.compareTo(BigDecimal.ZERO) > 0, "总利息应大于0");
    }

    @Test
    @DisplayName("等额本息月供计算 - 标准验证")
    void testCalculateEqualPrincipalInterestPayment_standard() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        
        BigDecimal monthlyPayment = CalculatorUtil.calculateEqualPrincipalInterestPayment(principal, annualRate, term);
        
        assertTrue(monthlyPayment.compareTo(new BigDecimal("4900")) > 0 && monthlyPayment.compareTo(new BigDecimal("5000")) < 0,
            "100万30年4.3%利率，月供应在4900-5000元之间，实际:" + monthlyPayment);
    }

    @Test
    @DisplayName("等额本息月供计算 - 无效参数")
    void testCalculateEqualPrincipalInterestPayment_invalidParams() {
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateEqualPrincipalInterestPayment(null, new BigDecimal("0.032"), 360));
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateEqualPrincipalInterestPayment(new BigDecimal("1000000"), null, 360));
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateEqualPrincipalInterestPayment(new BigDecimal("1000000"), new BigDecimal("0.032"), 0));
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateEqualPrincipalInterestPayment(new BigDecimal("1000000"), new BigDecimal("0.032"), -1));
    }

    @Test
    @DisplayName("等额本息明细计算 - 验证首末月还款")
    void testCalculateEqualPrincipalInterest_details() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 12;
        LocalDate startDate = LocalDate.of(2024, 1, 11);
        
        List<DetailDTO> details = CalculatorUtil.calculateEqualPrincipalInterest(principal, annualRate, term, startDate);
        
        assertEquals(12, details.size(), "应生成12期明细");
        
        DetailDTO firstDetail = details.get(0);
        assertEquals(1, firstDetail.getPeriod());
        assertEquals(LocalDate.of(2024, 2, 11), firstDetail.getPaymentDate());
        
        DetailDTO lastDetail = details.get(11);
        assertEquals(12, lastDetail.getPeriod());
        assertTrue(lastDetail.getRemainingPrincipal().compareTo(DELTA) < 0, 
            "最后一期剩余本金应接近0");
    }

    @Test
    @DisplayName("等额本息明细计算 - 累计数据验证")
    void testCalculateEqualPrincipalInterest_cumulative() {
        BigDecimal principal = new BigDecimal("100000");
        BigDecimal annualRate = new BigDecimal("0.05");
        int term = 24;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculateEqualPrincipalInterest(principal, annualRate, term, startDate);
        
        BigDecimal totalPayment = CalculatorUtil.calculateTotalPayment(details);
        BigDecimal totalPrincipal = details.get(details.size() - 1).getCumulativePrincipal();
        
        assertTrue(totalPrincipal.subtract(principal).abs().compareTo(DELTA) < 0,
            "累计本金应等于贷款本金");
        assertTrue(totalPayment.compareTo(principal) > 0,
            "总还款应大于贷款本金（含利息）");
    }

    @Test
    @DisplayName("等额本金明细计算 - 验证首末月还款递减")
    void testCalculateEqualPrincipal_details() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 12;
        LocalDate startDate = LocalDate.of(2024, 1, 11);
        
        List<DetailDTO> details = CalculatorUtil.calculateEqualPrincipal(principal, annualRate, term, startDate);
        
        assertEquals(12, details.size(), "应生成12期明细");
        
        DetailDTO firstDetail = details.get(0);
        DetailDTO secondDetail = details.get(1);
        
        assertTrue(firstDetail.getMonthlyPayment().compareTo(secondDetail.getMonthlyPayment()) > 0,
            "等额本金首月月供应大于次月月供");
        
        DetailDTO lastDetail = details.get(11);
        assertTrue(lastDetail.getRemainingPrincipal().compareTo(DELTA) < 0,
            "最后一期剩余本金应接近0");
    }

    @Test
    @DisplayName("等额本金明细计算 - 本金固定")
    void testCalculateEqualPrincipal_fixedPrincipal() {
        BigDecimal principal = new BigDecimal("120000");
        BigDecimal annualRate = new BigDecimal("0.05");
        int term = 12;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculateEqualPrincipal(principal, annualRate, term, startDate);
        
        BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(term), SCALE, RoundingMode.HALF_UP);
        
        for (int i = 0; i < details.size() - 1; i++) {
            assertTrue(details.get(i).getPrincipal().subtract(monthlyPrincipal).abs().compareTo(DELTA) < 0,
                "每期本金应固定为" + monthlyPrincipal);
        }
    }

    @Test
    @DisplayName("通用计算方法 - 等额本息")
    void testCalculate_equalPrincipalInterest() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        
        assertEquals(360, details.size(), "应生成360期明细");
        BigDecimal monthlyPayment = details.get(0).getMonthlyPayment();
        BigDecimal lastPayment = details.get(359).getMonthlyPayment();
        
        assertTrue(monthlyPayment.subtract(lastPayment).abs().compareTo(new BigDecimal("10")) < 0,
            "等额本息首末月供差额应小于10元，首月:" + monthlyPayment + "，末月:" + lastPayment);
    }

    @Test
    @DisplayName("通用计算方法 - 等额本金")
    void testCalculate_equalPrincipal() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL);
        
        assertEquals(360, details.size(), "应生成360期明细");
        assertTrue(details.get(0).getMonthlyPayment().compareTo(details.get(359).getMonthlyPayment()) > 0,
            "等额本金首月月供应大于末月月供");
    }

    @Test
    @DisplayName("总利息计算")
    void testCalculateTotalInterest() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        
        BigDecimal totalInterest = CalculatorUtil.calculateTotalInterest(details);
        
        assertTrue(totalInterest.compareTo(BigDecimal.ZERO) > 0, "总利息应大于0");
        assertTrue(totalInterest.compareTo(principal) < 0,
            "30年期总利息应小于贷款本金，实际:" + totalInterest);
    }

    @Test
    @DisplayName("总还款计算")
    void testCalculateTotalPayment() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> details = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        
        BigDecimal totalPayment = CalculatorUtil.calculateTotalPayment(details);
        BigDecimal totalInterest = CalculatorUtil.calculateTotalInterest(details);
        
        assertTrue(totalPayment.subtract(principal.add(totalInterest)).abs().compareTo(DELTA) < 0,
            "总还款应等于本金加利息");
    }

    @Test
    @DisplayName("利息占比计算")
    void testCalculateInterestRatio() {
        BigDecimal totalInterest = new BigDecimal("500000");
        BigDecimal totalPayment = new BigDecimal("1500000");
        
        BigDecimal ratio = CalculatorUtil.calculateInterestRatio(totalInterest, totalPayment);
        
        BigDecimal expected = new BigDecimal("33.3333");
        assertTrue(ratio.subtract(expected).abs().compareTo(new BigDecimal("0.1")) < 0,
            "利息占比应约为33.33%");
    }

    @Test
    @DisplayName("利息占比计算 - 无效参数")
    void testCalculateInterestRatio_invalidParams() {
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateInterestRatio(null, new BigDecimal("100000")));
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateInterestRatio(new BigDecimal("50000"), null));
        assertEquals(BigDecimal.ZERO, CalculatorUtil.calculateInterestRatio(new BigDecimal("50000"), BigDecimal.ZERO));
    }

    @Test
    @DisplayName("提前还款后剩余本金计算")
    void testCalculateRemainingPrincipalAfterPrepay() {
        BigDecimal remainingPrincipal = new BigDecimal("1263983.31");
        BigDecimal prepayAmount = new BigDecimal("100000");
        
        BigDecimal afterPrepay = CalculatorUtil.calculateRemainingPrincipalAfterPrepay(remainingPrincipal, prepayAmount);
        
        BigDecimal expected = new BigDecimal("1163983.31");
        assertEquals(expected, afterPrepay, "提前还款后剩余本金应正确计算");
    }

    @Test
    @DisplayName("缩期后新期限计算")
    void testCalculateNewTermShorten() {
        BigDecimal remainingPrincipal = new BigDecimal("500000");
        BigDecimal monthlyPayment = new BigDecimal("3000");
        BigDecimal annualRate = new BigDecimal("0.036");
        
        int newTerm = CalculatorUtil.calculateNewTermShorten(remainingPrincipal, monthlyPayment, annualRate);
        
        assertTrue(newTerm > 0, "新期限应大于0，实际:" + newTerm);
    }

    @Test
    @DisplayName("减月供后新月供计算")
    void testCalculateNewMonthlyPaymentReduce() {
        BigDecimal remainingPrincipal = new BigDecimal("500000");
        int newTerm = 200;
        BigDecimal annualRate = new BigDecimal("0.036");
        
        BigDecimal newMonthlyPayment = CalculatorUtil.calculateNewMonthlyPaymentReduce(remainingPrincipal, newTerm, annualRate);
        
        assertTrue(newMonthlyPayment.compareTo(BigDecimal.ZERO) > 0, "新月供应大于0");
        assertTrue(newMonthlyPayment.compareTo(remainingPrincipal.divide(BigDecimal.valueOf(newTerm), 2, RoundingMode.HALF_UP)) > 0,
            "新月供应大于每月本金部分");
    }

    @Test
    @DisplayName("节省利息计算")
    void testCalculateSavedInterest() {
        BigDecimal originalInterest = new BigDecimal("200000");
        BigDecimal newInterest = new BigDecimal("150000");
        
        BigDecimal savedInterest = CalculatorUtil.calculateSavedInterest(originalInterest, newInterest);
        
        BigDecimal expected = new BigDecimal("50000");
        assertTrue(savedInterest.compareTo(expected) == 0, "节省利息应为50000元，实际:" + savedInterest);
    }

    @Test
    @DisplayName("完整流程测试 - 实际贷款数据验证")
    void testFullFlow_withRealData() {
        BigDecimal baseAnnualRate = new BigDecimal("0.035");
        Integer rateFloatBp = -30;
        BigDecimal principal = new BigDecimal("1263983.31");
        int totalTerm = 348;
        int paidPeriods = 73;
        int remainingTerm = totalTerm - paidPeriods;
        LocalDate startDate = LocalDate.of(2020, 3, 11);
        
        BigDecimal actualAnnualRate = CalculatorUtil.calculateActualAnnualRate(baseAnnualRate, rateFloatBp);
        assertTrue(actualAnnualRate.compareTo(new BigDecimal("0.032")) == 0, "实际年利率应为3.2%");
        
        List<DetailDTO> details = CalculatorUtil.calculate(
            principal, 
            actualAnnualRate, 
            remainingTerm, 
            startDate, 
            RepaymentMethod.EQUAL_PRINCIPAL_INTEREST
        );
        
        assertEquals(275, details.size(), "应生成275期明细（348期-已还73期）");
        
        BigDecimal monthlyPayment = details.get(0).getMonthlyPayment();
        BigDecimal expectedPayment = new BigDecimal("6491.63");
        assertTrue(monthlyPayment.subtract(expectedPayment).abs().compareTo(new BigDecimal("1")) < 0,
            "月供应为6491.63元，实际计算: " + monthlyPayment);
        
        DetailDTO firstDetail = details.get(0);
        assertEquals(1, firstDetail.getPeriod(), "首期期数应为1");
        assertEquals(LocalDate.of(2020, 4, 11), firstDetail.getPaymentDate(), "首期还款日应为2020年4月11日");
        
        DetailDTO lastDetail = details.get(details.size() - 1);
        assertEquals(275, lastDetail.getPeriod(), "末期期数应为275");
        assertTrue(lastDetail.getRemainingPrincipal().compareTo(DELTA) < 0,
            "最后一期剩余本金应接近0，实际:" + lastDetail.getRemainingPrincipal());
        
        BigDecimal totalPayment = CalculatorUtil.calculateTotalPayment(details);
        BigDecimal totalInterest = CalculatorUtil.calculateTotalInterest(details);
        assertTrue(totalPayment.subtract(principal.add(totalInterest)).abs().compareTo(new BigDecimal("1")) < 0,
            "总还款应等于本金加利息");
        
        BigDecimal interestRatio = CalculatorUtil.calculateInterestRatio(totalInterest, totalPayment);
        assertTrue(interestRatio.compareTo(new BigDecimal("20")) > 0 && interestRatio.compareTo(new BigDecimal("30")) < 0,
            "利息占比应在20-30%范围内，实际:" + interestRatio);
    }

    @Test
    @DisplayName("等额本息与等额本金对比 - 利息差额")
    void testCompareInterestDifference() {
        BigDecimal principal = new BigDecimal("1000000");
        BigDecimal annualRate = new BigDecimal("0.043");
        int term = 360;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        
        List<DetailDTO> detailsEPI = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL_INTEREST);
        List<DetailDTO> detailsEP = CalculatorUtil.calculate(principal, annualRate, term, startDate, RepaymentMethod.EQUAL_PRINCIPAL);
        
        BigDecimal interestEPI = CalculatorUtil.calculateTotalInterest(detailsEPI);
        BigDecimal interestEP = CalculatorUtil.calculateTotalInterest(detailsEP);
        
        assertTrue(interestEPI.compareTo(interestEP) > 0,
            "等额本息总利息应大于等额本金总利息");
        
        BigDecimal firstPaymentEPI = detailsEPI.get(0).getMonthlyPayment();
        BigDecimal firstPaymentEP = detailsEP.get(0).getMonthlyPayment();
        
        assertTrue(firstPaymentEP.compareTo(firstPaymentEPI) > 0,
            "等额本金首月月供应大于等额本息首月月供");
    }
}