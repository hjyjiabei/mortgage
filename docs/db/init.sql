-- ============================================================
-- 房贷计算器 - 数据库初始化主脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- 执行方式: mysql -uroot -p < init.sql
-- ============================================================

-- ============================================================
-- 一、创建数据库
-- ============================================================
CREATE DATABASE IF NOT EXISTS mortgage_calculator
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE mortgage_calculator;

-- ============================================================
-- 二、创建数据表
-- ============================================================

-- 1. 贷款申请记录表
DROP TABLE IF EXISTS loan_request;
CREATE TABLE loan_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    loan_amount DECIMAL(12, 2) NOT NULL COMMENT '贷款金额（元）',
    loan_term INT NOT NULL COMMENT '贷款期限（月）',
    annual_rate DECIMAL(8, 6) NOT NULL COMMENT '年利率（小数形式）',
    repayment_method VARCHAR(30) NOT NULL COMMENT '还款方式',
    start_date DATE NOT NULL COMMENT '放款日期',
    loan_type VARCHAR(20) DEFAULT 'COMMERCIAL' COMMENT '贷款类型',
    user_id VARCHAR(64) DEFAULT NULL COMMENT '用户标识',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贷款申请记录表';

-- 2. 还款计划表
DROP TABLE IF EXISTS repayment_plan;
CREATE TABLE repayment_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    plan_no VARCHAR(32) NOT NULL COMMENT '计划编号',
    loan_request_id BIGINT NOT NULL COMMENT '关联贷款申请ID',
    loan_amount DECIMAL(12, 2) NOT NULL COMMENT '贷款本金（元）',
    loan_term INT NOT NULL COMMENT '原始贷款期限（月）',
    actual_term INT NOT NULL COMMENT '实际还款期限（月）',
    annual_rate DECIMAL(8, 6) NOT NULL COMMENT '年利率',
    monthly_rate DECIMAL(10, 8) NOT NULL COMMENT '月利率',
    repayment_method VARCHAR(30) NOT NULL COMMENT '还款方式',
    monthly_payment DECIMAL(12, 2) DEFAULT NULL COMMENT '月供金额',
    first_payment DECIMAL(12, 2) NOT NULL COMMENT '首月还款（元）',
    last_payment DECIMAL(12, 2) NOT NULL COMMENT '末月还款（元）',
    total_payment DECIMAL(14, 2) NOT NULL COMMENT '总还款额（元）',
    total_interest DECIMAL(14, 2) NOT NULL COMMENT '总利息（元）',
    total_principal DECIMAL(14, 2) NOT NULL COMMENT '总本金（元）',
    interest_ratio DECIMAL(8, 4) NOT NULL COMMENT '利息占比（%）',
    first_payment_date DATE NOT NULL COMMENT '首次还款日期',
    last_payment_date DATE NOT NULL COMMENT '最后还款日期',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-已作废, 1-有效, 2-已结清',
    source VARCHAR(20) DEFAULT 'CALCULATION' COMMENT '来源',
    parent_plan_id BIGINT DEFAULT NULL COMMENT '父计划ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE INDEX uk_plan_no (plan_no),
    INDEX idx_loan_request_id (loan_request_id),
    INDEX idx_status (status),
    INDEX idx_parent_plan_id (parent_plan_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (loan_request_id) REFERENCES loan_request(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='还款计划表';

-- 3. 还款明细表
DROP TABLE IF EXISTS repayment_detail;
CREATE TABLE repayment_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    plan_id BIGINT NOT NULL COMMENT '关联还款计划ID',
    period INT NOT NULL COMMENT '期数',
    payment_date DATE NOT NULL COMMENT '还款日期',
    monthly_payment DECIMAL(12, 2) NOT NULL COMMENT '月供金额（元）',
    principal DECIMAL(12, 2) NOT NULL COMMENT '本金部分（元）',
    interest DECIMAL(12, 2) NOT NULL COMMENT '利息部分（元）',
    remaining_principal DECIMAL(14, 2) NOT NULL COMMENT '剩余本金（元）',
    cumulative_payment DECIMAL(14, 2) NOT NULL COMMENT '累计还款（元）',
    cumulative_principal DECIMAL(14, 2) NOT NULL COMMENT '累计本金（元）',
    cumulative_interest DECIMAL(14, 2) NOT NULL COMMENT '累计利息（元）',
    is_prepaid TINYINT NOT NULL DEFAULT 0 COMMENT '是否已提前还款',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    UNIQUE INDEX uk_plan_period (plan_id, period),
    INDEX idx_plan_id (plan_id),
    INDEX idx_payment_date (payment_date),
    FOREIGN KEY (plan_id) REFERENCES repayment_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='还款明细表';

-- 4. 提前还款记录表
DROP TABLE IF EXISTS prepay_record;
CREATE TABLE prepay_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    original_plan_id BIGINT NOT NULL COMMENT '原还款计划ID',
    new_plan_id BIGINT DEFAULT NULL COMMENT '新还款计划ID',
    prepay_period INT NOT NULL COMMENT '提前还款期数',
    prepay_date DATE NOT NULL COMMENT '提前还款日期',
    prepay_amount DECIMAL(12, 2) NOT NULL COMMENT '提前还款金额（元）',
    prepay_type VARCHAR(30) NOT NULL COMMENT '提前还款类型',
    remaining_principal_before DECIMAL(12, 2) NOT NULL COMMENT '提前还款前剩余本金',
    remaining_principal_after DECIMAL(12, 2) NOT NULL COMMENT '提前还款后剩余本金',
    original_remaining_term INT NOT NULL COMMENT '原剩余期限（月）',
    new_remaining_term INT DEFAULT NULL COMMENT '新剩余期限（月）',
    original_monthly_payment DECIMAL(12, 2) NOT NULL COMMENT '原月供（元）',
    new_monthly_payment DECIMAL(12, 2) DEFAULT NULL COMMENT '新月供（元）',
    saved_interest DECIMAL(12, 2) NOT NULL COMMENT '节省利息（元）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_original_plan_id (original_plan_id),
    INDEX idx_new_plan_id (new_plan_id),
    INDEX idx_prepay_date (prepay_date),
    FOREIGN KEY (original_plan_id) REFERENCES repayment_plan(id) ON DELETE CASCADE,
    FOREIGN KEY (new_plan_id) REFERENCES repayment_plan(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提前还款记录表';

-- 5. 方案对比记录表
DROP TABLE IF EXISTS compare_record;
CREATE TABLE compare_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    plan_a_id BIGINT NOT NULL COMMENT '方案A计划ID',
    plan_b_id BIGINT NOT NULL COMMENT '方案B计划ID',
    plan_a_method VARCHAR(30) NOT NULL COMMENT '方案A还款方式',
    plan_b_method VARCHAR(30) NOT NULL COMMENT '方案B还款方式',
    loan_amount DECIMAL(12, 2) NOT NULL COMMENT '贷款金额（元）',
    loan_term INT NOT NULL COMMENT '贷款期限（月）',
    annual_rate DECIMAL(8, 6) NOT NULL COMMENT '年利率',
    interest_diff DECIMAL(12, 2) NOT NULL COMMENT '利息差额（A-B）',
    first_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '首月月供差额',
    last_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '末月月供差额',
    total_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '总还款差额',
    interest_ratio_diff DECIMAL(8, 4) NOT NULL COMMENT '利息占比差额',
    user_id VARCHAR(64) DEFAULT NULL COMMENT '用户标识',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_plan_a_id (plan_a_id),
    INDEX idx_plan_b_id (plan_b_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (plan_a_id) REFERENCES repayment_plan(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_b_id) REFERENCES repayment_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='方案对比记录表';

-- ============================================================
-- 三、创建视图
-- ============================================================

-- 还款计划汇总视图
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

-- 提前还款记录汇总视图
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

-- 方案对比汇总视图
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

-- ============================================================
-- 四、创建应用用户
-- ============================================================
CREATE USER IF NOT EXISTS 'mortgage_app'@'%' IDENTIFIED BY 'Mortgage@2024#App';
GRANT SELECT, INSERT, UPDATE, DELETE ON mortgage_calculator.* TO 'mortgage_app'@'%';
FLUSH PRIVILEGES;

-- ============================================================
-- 五、插入测试数据
-- ============================================================
INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (1000000.00, 360, 0.043000, 'EQUAL_PRINCIPAL_INTEREST', '2024-01-01', 'COMMERCIAL', '示例：100万30年等额本息');

INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (1000000.00, 360, 0.043000, 'EQUAL_PRINCIPAL', '2024-01-01', 'COMMERCIAL', '示例：100万30年等额本金');

INSERT INTO loan_request (loan_amount, loan_term, annual_rate, repayment_method, start_date, loan_type, remark) 
VALUES (500000.00, 240, 0.032500, 'EQUAL_PRINCIPAL_INTEREST', '2024-01-01', 'PROVIDENT_FUND', '示例：50万20年公积金');

-- ============================================================
-- 初始化完成
-- ============================================================
SELECT '数据库初始化完成!' AS message;
SELECT COUNT(*) AS table_count FROM information_schema.tables WHERE table_schema = 'mortgage_calculator';
SHOW TABLES;