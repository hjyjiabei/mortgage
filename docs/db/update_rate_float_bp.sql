-- 利率浮动幅度字段增量更新脚本
-- 执行方式: mysql -uroot -p mortgage_calculator < update_rate_float_bp.sql

USE mortgage_calculator;

-- loan_request 表增加 rate_float_bp 字段
ALTER TABLE loan_request 
ADD COLUMN rate_float_bp INT DEFAULT 0 COMMENT '利率浮动幅度（单位：bp，正数为上浮，负数为下浮）' 
 AFTER annual_rate;

-- repayment_plan 表增加 rate_float_bp 字段
ALTER TABLE repayment_plan 
ADD COLUMN rate_float_bp INT DEFAULT 0 COMMENT '利率浮动幅度（单位：bp）' 
 AFTER annual_rate;

-- 更新完成
SELECT '表结构更新完成!' AS message;