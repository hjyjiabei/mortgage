# 房贷计算器项目指南

## 项目架构

前后端分离：
- **后端**: `backend/` - Spring Boot 3.2 + Java 21 + MyBatis Plus + MySQL 8.0
- **前端**: `frontend/` - Vue 2.6 + Element UI + ECharts + Vuex

## 开发命令

### 后端
```bash
cd backend
mvn spring-boot:run          # 启动开发服务器 (端口 8080)
mvn clean package            # 构建 JAR
```

### 前端
```bash
cd frontend
npm run serve                # 启动开发服务器 (端口 8081，代理 /api 到后端 8080)
npm run build                # 生产构建
npm run lint                 # 代码检查
```

## 数据库初始化

```bash
mysql -uroot -p < docs/db/init.sql
```

数据库连接信息：
- 数据库: `mortgage_calculator`
- 用户: `mortgage_app`
- 密码: `Mortgage@2024#App`

## 核心业务模块

| 模块 | 后端路径 | 前端页面 | 功能 |
|------|----------|----------|------|
| 计算 | `controller/CalculateController` | `views/Calculate.vue` | 等额本息/等额本金计算 |
| 对比 | `controller/CompareController` | `views/Compare.vue` | 多方案对比 |
| 提前还款 | `controller/PrepayController` | `views/Prepay.vue` | 缩期/减月供模拟 |
| 历史 | `controller/PlanController` | `views/History.vue` | 计划管理 |

核心计算逻辑: `backend/src/main/java/com/mortgage/util/CalculatorUtil.java`

## 代码规范

- 2 空格缩进
- 变量名: camelCase
- 函数名: 动词开头 (如 `calculatePayment`)
- 注释使用中文
- 禁止 `any` 类型，禁止 `@ts-ignore` / `eslint-disable`

## 注意事项

- 后端使用 Lombok，需要 IDE 安装插件
- 前端路由使用 history 模式
- 前端全局样式在 `src/assets/styles/global.scss`，会自动注入到所有组件
- 数据库密码已提交，仅用于开发环境