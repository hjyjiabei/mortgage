-- ============================================================
-- 房贷计算器 - 测试数据脚本
-- 数据库: MySQL 8.0+
-- ============================================================

USE mortgage_calculator;

-- ============================================================
-- 插入示例贷款申请数据
-- ============================================================

-- 示例1: 100万30年等额本息商业贷款
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (1000000.00, 360, 0.043000, 'EQUAL_PRINCIPAL_INTEREST', '2024-01-01', 'COMMERCIAL', '示例：100万30年等额本息商业贷款');

-- 示例2: 100万30年等额本金商业贷款
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (1000000.00, 360, 0.043000, 'EQUAL_PRINCIPAL', '2024-01-01', 'COMMERCIAL', '示例：100万30年等额本金商业贷款');

-- 示例3: 50万20年公积金贷款
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (500000.00, 240, 0.032500, 'EQUAL_PRINCIPAL_INTEREST', '2024-01-01', 'PROVIDENT_FUND', '示例：50万20年公积金贷款');

-- 示例4: 200万20年等额本息商业贷款
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (2000000.00, 240, 0.041000, 'EQUAL_PRINCIPAL_INTEREST', '2024-01-01', 'COMMERCIAL', '示例：200万20年等额本息商业贷款');

-- 示例5: 150万25年等额本金商业贷款
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (1500000.00, 300, 0.042000, 'EQUAL_PRINCIPAL', '2024-01-01', 'COMMERCIAL', '示例：150万25年等额本金商业贷款');

-- ============================================================
-- 验证插入数据
-- ============================================================
-- SELECT * FROM loan_request ORDER BY created_at DESC;

-- ============================================================
-- 清理测试数据（可选，谨慎使用）
-- ============================================================
-- TRUNCATE TABLE loan_request;
-- TRUNCATE TABLE repayment_plan;
-- TRUNCATE TABLE repayment_detail;
-- TRUNCATE TABLE prepay_record;
-- TRUNCATE TABLE compare_record;