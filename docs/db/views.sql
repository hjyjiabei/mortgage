-- ============================================================
-- 房贷计算器 - 视图脚本
-- 数据库: MySQL 8.0+
-- ============================================================

USE mortgage_calculator;

-- ============================================================
-- 1. 还款计划汇总视图
-- ============================================================
DROP VIEW IF EXISTS v_plan_summary;
CREATE VIEW v_plan_summary AS
SELECT 
    rp.id,
    rp.plan_no,
    lr.loan_amount / 10000 AS loan_amount_wan,
    lr.loan_term,
    lr.annual_rate * 100 AS annual_rate_percent,
    CASE rp.repayment_method 
        WHEN 'EQUAL_PRINCIPAL_INTEREST' THEN '等额本息'
        WHEN 'EQUAL_PRINCIPAL' THEN '等额本金'
    END AS repayment_method_name,
    rp.monthly_payment,
    rp.first_payment,
    rp.last_payment,
    rp.total_payment,
    rp.total_interest,
    rp.total_principal,
    rp.interest_ratio,
    rp.first_payment_date,
    rp.last_payment_date,
    CASE rp.status 
        WHEN 0 THEN '已作废'
        WHEN 1 THEN '有效'
        WHEN 2 THEN '已结清'
    END AS status_name,
    rp.source,
    rp.created_at
FROM repayment_plan rp
JOIN loan_request lr ON rp.loan_request_id = lr.id
WHERE rp.status = 1;

-- ============================================================
-- 2. 提前还款记录汇总视图
-- ============================================================
DROP VIEW IF EXISTS v_prepay_summary;
CREATE VIEW v_prepay_summary AS
SELECT 
    pr.id,
    pr.prepay_period,
    pr.prepay_date,
    pr.prepay_amount,
    CASE pr.prepay_type 
        WHEN 'SHORTEN_TERM' THEN '缩期'
        WHEN 'REDUCE_PAYMENT' THEN '减月供'
    END AS prepay_type_name,
    pr.remaining_principal_before,
    pr.remaining_principal_after,
    pr.original_remaining_term,
    pr.new_remaining_term,
    pr.original_monthly_payment,
    pr.new_monthly_payment,
    pr.saved_interest,
    rp.plan_no AS original_plan_no,
    rp_new.plan_no AS new_plan_no,
    pr.created_at
FROM prepay_record pr
JOIN repayment_plan rp ON pr.original_plan_id = rp.id
LEFT JOIN repayment_plan rp_new ON pr.new_plan_id = rp_new.id;

-- ============================================================
-- 3. 方案对比汇总视图
-- ============================================================
DROP VIEW IF EXISTS v_compare_summary;
CREATE VIEW v_compare_summary AS
SELECT 
    cr.id,
    cr.loan_amount / 10000 AS loan_amount_wan,
    cr.loan_term,
    cr.annual_rate * 100 AS annual_rate_percent,
    CASE cr.plan_a_method 
        WHEN 'EQUAL_PRINCIPAL_INTEREST' THEN '等额本息'
        WHEN 'EQUAL_PRINCIPAL' THEN '等额本金'
    END AS plan_a_method_name,
    CASE cr.plan_b_method 
        WHEN 'EQUAL_PRINCIPAL_INTEREST' THEN '等额本息'
        WHEN 'EQUAL_PRINCIPAL' THEN '等额本金'
    END AS plan_b_method_name,
    cr.interest_diff,
    cr.first_payment_diff,
    cr.last_payment_diff,
    cr.total_payment_diff,
    cr.interest_ratio_diff,
    rp_a.plan_no AS plan_a_no,
    rp_b.plan_no AS plan_b_no,
    cr.created_at
FROM compare_record cr
JOIN repayment_plan rp_a ON cr.plan_a_id = rp_a.id
JOIN repayment_plan rp_b ON cr.plan_b_id = rp_b.id;