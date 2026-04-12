-- ============================================================
-- 房贷计算器 - 建表脚本
-- 数据库: MySQL 8.0+
-- 字符集: utf8mb4
-- ============================================================

USE mortgage_calculator;

-- ============================================================
-- 1. 贷款申请记录表
-- ============================================================
DROP TABLE IF EXISTS loan_request;
CREATE TABLE loan_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    loan_amount DECIMAL(12, 2) NOT NULL COMMENT '贷款金额（元）',
    loan_term INT NOT NULL COMMENT '贷款期限（月）',
    annual_rate DECIMAL(8, 6) NOT NULL COMMENT '年利率（小数形式，如0.043000）',
    repayment_method VARCHAR(30) NOT NULL COMMENT '还款方式: EQUAL_PRINCIPAL_INTEREST(等额本息), EQUAL_PRINCIPAL(等额本金)',
    start_date DATE NOT NULL COMMENT '放款日期',
    loan_type VARCHAR(20) DEFAULT 'COMMERCIAL' COMMENT '贷款类型: COMMERCIAL(商业贷款), PROVIDENT_FUND(公积金), COMBINED(组合贷款)',
    user_id VARCHAR(64) DEFAULT NULL COMMENT '用户标识（预留）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贷款申请记录表';

-- ============================================================
-- 2. 还款计划表
-- ============================================================
DROP TABLE IF EXISTS repayment_plan;
CREATE TABLE repayment_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    plan_no VARCHAR(32) NOT NULL COMMENT '计划编号（业务唯一标识）',
    loan_request_id BIGINT NOT NULL COMMENT '关联贷款申请ID',
    loan_amount DECIMAL(12, 2) NOT NULL COMMENT '贷款本金（元）',
    loan_term INT NOT NULL COMMENT '原始贷款期限（月）',
    actual_term INT NOT NULL COMMENT '实际还款期限（月，提前还款后可能变化）',
    annual_rate DECIMAL(8, 6) NOT NULL COMMENT '年利率',
    monthly_rate DECIMAL(10, 8) NOT NULL COMMENT '月利率',
    repayment_method VARCHAR(30) NOT NULL COMMENT '还款方式',
    monthly_payment DECIMAL(12, 2) DEFAULT NULL COMMENT '月供金额（等额本息时固定）',
    first_payment DECIMAL(12, 2) NOT NULL COMMENT '首月还款（元）',
    last_payment DECIMAL(12, 2) NOT NULL COMMENT '末月还款（元）',
    total_payment DECIMAL(14, 2) NOT NULL COMMENT '总还款额（元）',
    total_interest DECIMAL(14, 2) NOT NULL COMMENT '总利息（元）',
    total_principal DECIMAL(14, 2) NOT NULL COMMENT '总本金（元）',
    interest_ratio DECIMAL(8, 4) NOT NULL COMMENT '利息占比（%）',
    first_payment_date DATE NOT NULL COMMENT '首次还款日期',
    last_payment_date DATE NOT NULL COMMENT '最后还款日期',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-已作废, 1-有效, 2-已结清',
    source VARCHAR(20) DEFAULT 'CALCULATION' COMMENT '来源: CALCULATION(新计算), PREPAY(提前还款产生)',
    parent_plan_id BIGINT DEFAULT NULL COMMENT '父计划ID（提前还款场景）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    UNIQUE INDEX uk_plan_no (plan_no),
    INDEX idx_loan_request_id (loan_request_id),
    INDEX idx_status (status),
    INDEX idx_parent_plan_id (parent_plan_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (loan_request_id) REFERENCES loan_request(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='还款计划表';

-- ============================================================
-- 3. 还款明细表
-- ============================================================
DROP TABLE IF EXISTS repayment_detail;
CREATE TABLE repayment_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    plan_id BIGINT NOT NULL COMMENT '关联还款计划ID',
    period INT NOT NULL COMMENT '期数（第几期）',
    payment_date DATE NOT NULL COMMENT '还款日期',
    monthly_payment DECIMAL(12, 2) NOT NULL COMMENT '月供金额（元）',
    principal DECIMAL(12, 2) NOT NULL COMMENT '本金部分（元）',
    interest DECIMAL(12, 2) NOT NULL COMMENT '利息部分（元）',
    remaining_principal DECIMAL(14, 2) NOT NULL COMMENT '剩余本金（元）',
    cumulative_payment DECIMAL(14, 2) NOT NULL COMMENT '累计还款（元）',
    cumulative_principal DECIMAL(14, 2) NOT NULL COMMENT '累计本金（元）',
    cumulative_interest DECIMAL(14, 2) NOT NULL COMMENT '累计利息（元）',
    is_prepaid TINYINT NOT NULL DEFAULT 0 COMMENT '是否已提前还款: 0-否, 1-是',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    UNIQUE INDEX uk_plan_period (plan_id, period),
    INDEX idx_plan_id (plan_id),
    INDEX idx_payment_date (payment_date),
    FOREIGN KEY (plan_id) REFERENCES repayment_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='还款明细表';

-- ============================================================
-- 4. 提前还款记录表
-- ============================================================
DROP TABLE IF EXISTS prepay_record;
CREATE TABLE prepay_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    original_plan_id BIGINT NOT NULL COMMENT '原还款计划ID',
    new_plan_id BIGINT DEFAULT NULL COMMENT '新生成的还款计划ID',
    prepay_period INT NOT NULL COMMENT '提前还款发生的期数',
    prepay_date DATE NOT NULL COMMENT '提前还款日期',
    prepay_amount DECIMAL(12, 2) NOT NULL COMMENT '提前还款金额（元）',
    prepay_type VARCHAR(30) NOT NULL COMMENT '提前还款类型: SHORTEN_TERM(缩期), REDUCE_PAYMENT(减月供)',
    remaining_principal_before DECIMAL(12, 2) NOT NULL COMMENT '提前还款前剩余本金（元）',
    remaining_principal_after DECIMAL(12, 2) NOT NULL COMMENT '提前还款后剩余本金（元）',
    original_remaining_term INT NOT NULL COMMENT '原剩余期限（月）',
    new_remaining_term INT DEFAULT NULL COMMENT '新剩余期限（月，缩期时有变化）',
    original_monthly_payment DECIMAL(12, 2) NOT NULL COMMENT '原月供（元）',
    new_monthly_payment DECIMAL(12, 2) DEFAULT NULL COMMENT '新月供（元，减月供时有变化）',
    saved_interest DECIMAL(12, 2) NOT NULL COMMENT '节省利息（元）',
    remark VARCHAR(255) DEFAULT NULL COMMENT '备注',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_original_plan_id (original_plan_id),
    INDEX idx_new_plan_id (new_plan_id),
    INDEX idx_prepay_date (prepay_date),
    FOREIGN KEY (original_plan_id) REFERENCES repayment_plan(id) ON DELETE CASCADE,
    FOREIGN KEY (new_plan_id) REFERENCES repayment_plan(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='提前还款记录表';

-- ============================================================
-- 5. 方案对比记录表
-- ============================================================
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
    interest_diff DECIMAL(12, 2) NOT NULL COMMENT '利息差额（A-B，元）',
    first_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '首月月供差额（A-B，元）',
    last_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '末月月供差额（A-B，元）',
    total_payment_diff DECIMAL(12, 2) NOT NULL COMMENT '总还款差额（A-B，元）',
    interest_ratio_diff DECIMAL(8, 4) NOT NULL COMMENT '利息占比差额（A-B，%）',
    user_id VARCHAR(64) DEFAULT NULL COMMENT '用户标识（预留）',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    
    INDEX idx_plan_a_id (plan_a_id),
    INDEX idx_plan_b_id (plan_b_id),
    INDEX idx_created_at (created_at),
    FOREIGN KEY (plan_a_id) REFERENCES repayment_plan(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_b_id) REFERENCES repayment_plan(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='方案对比记录表';

-- ============================================================
-- 枚举值说明
-- ============================================================
-- repayment_method 还款方式:
--   EQUAL_PRINCIPAL_INTEREST = 等额本息
--   EQUAL_PRINCIPAL = 等额本金
--
-- prepay_type 提前还款类型:
--   SHORTEN_TERM = 缩短期限（月供不变，期限缩短）
--   REDUCE_PAYMENT = 减少月供（期限不变，月供减少）
--
-- loan_type 贷款类型:
--   COMMERCIAL = 商业贷款
--   PROVIDENT_FUND = 公积金贷款
--   COMBINED = 组合贷款
--
-- status 还款计划状态:
--   0 = 已作废
--   1 = 有效
--   2 = 已结清
--
-- source 计划来源:
--   CALCULATION = 新计算产生
--   PREPAY = 提前还款产生