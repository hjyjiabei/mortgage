-- ============================================================
-- 房贷计算器 - 用户权限脚本
-- 数据库: MySQL 8.0+
-- ============================================================

USE mortgage_calculator;

-- ============================================================
-- 创建应用数据库用户
-- ============================================================

-- 创建 mortgage_app 用户（应用访问用户）
CREATE USER IF NOT EXISTS 'mortgage_app'@'%' IDENTIFIED BY 'Mortgage@2024#App';

-- 授予 mortgage_calculator 数据库的增删改查权限
GRANT SELECT, INSERT, UPDATE, DELETE ON mortgage_calculator.* TO 'mortgage_app'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- ============================================================
-- 用户说明
-- ============================================================
-- 用户名: mortgage_app
-- 密码: Mortgage@2024#App
-- 权限范围: 仅限 mortgage_calculator 数据库
-- 权限类型: SELECT, INSERT, UPDATE, DELETE（不含 DROP、ALTER 等危险权限）
-- 
-- 建议:
--   1. 生产环境请修改密码为更强密码
--   2. 可根据实际需求限制访问IP范围（如 'mortgage_app'@'192.168.%'）
--   3. 定期检查用户权限，确保符合安全要求

-- ============================================================
-- 验证用户权限
-- ============================================================
-- 查看用户权限
-- SHOW GRANTS FOR 'mortgage_app'@'%';

-- 查看所有用户
-- SELECT User, Host FROM mysql.user WHERE User = 'mortgage_app';