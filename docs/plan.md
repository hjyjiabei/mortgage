# 房贷计算器项目开发计划

## 一、项目结构总览

```
mortgage/
├── backend/                          # 后端项目
│   ├── pom.xml                       # Maven 配置
│   └── src/
│       ├── main/
│       │   ├── java/com/mortgage/
│       │   │   ├── MortgageApplication.java
│       │   │   ├── config/           # 配置类
│       │   │   ├── controller/       # 控制器层
│       │   │   ├── service/          # 服务层
│       │   │   ├── mapper/           # 数据访问层
│       │   │   ├── model/            # 实体类
│       │   │   ├── enums/            # 枚举类
│       │   │   ├── util/             # 工具类
│       │   │   ├── exception/        # 异常处理
│       │   │   └── common/           # 公共类
│       │   └── resources/
│       │       ├── application.yml   # 配置文件
│       │       └── mapper/           # MyBatis XML
│       └── test/                     # 测试代码
│
├── frontend/                         # 前端项目
│   ├── package.json                  # 依赖配置
│   ├── vue.config.js                 # Vue 配置
│   ├── public/
│   │   └── index.html
│   └── src/
│       ├── main.js                   # 入口文件
│       ├── App.vue                   # 根组件
│       ├── router/                   # 路由
│       ├── store/                    # Vuex 状态
│       ├── views/                    # 页面组件
│       ├── components/               # 通用组件
│       ├── api/                      # API 接口
│       ├── utils/                    # 工具函数
│       ├── assets/                   # 静态资源
│       └── constants/                # 常量
│
├── docs/                             # 文档目录
│   ├── 需求文档.md
│   ├── 概要设计文档.md
│   ├── plan.md                       # 开发计划
│   └── db/                           # 数据库脚本
│       ├── init.sql                  # 初始化主脚本
│       ├── tables.sql                # 建表脚本
│       ├── views.sql                 # 视图脚本
│       ├── users.sql                 # 用户脚本
│       └── test_data.sql             # 测试数据
│
└── README.md                         # 项目说明
```

---

## 二、Phase 1: 数据库初始化（预计 0.5 天）

### 执行步骤

| 步骤 | 任务 | 文件 | 内容 |
|------|------|------|------|
| 1.1 | 创建数据库脚本目录 | docs/db/ | 创建目录结构 |
| 1.2 | 编写建表脚本 | docs/db/tables.sql | 5张核心表 |
| 1.3 | 编写视图脚本 | docs/db/views.sql | 2个查询视图 |
| 1.4 | 编写用户脚本 | docs/db/users.sql | mortgage_app 用户 |
| 1.5 | 编写测试数据脚本 | docs/db/test_data.sql | 示例数据 |
| 1.6 | 编写主脚本 | docs/db/init.sql | 整合所有脚本 |
| 1.7 | 执行初始化 | Docker MySQL | 执行SQL脚本 |

### 数据库表清单

| 序号 | 表名 | 说明 |
|------|------|------|
| 1 | loan_request | 贷款申请记录表 |
| 2 | repayment_plan | 还款计划表 |
| 3 | repayment_detail | 还款明细表 |
| 4 | prepay_record | 提前还款记录表 |
| 5 | compare_record | 方案对比记录表 |

---

## 三、Phase 2: 后端基础框架搭建（预计 1 天）

### 执行步骤

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 2.1 | 创建后端目录 | backend/ | 创建 Maven 项目结构 |
| 2.2 | 编写 pom.xml | backend/pom.xml | Maven 依赖配置 |
| 2.3 | 编写启动类 | backend/src/main/java/com/mortgage/MortgageApplication.java | Spring Boot 启动入口 |
| 2.4 | 编写配置文件 | backend/src/main/resources/application.yml | 数据库连接、MyBatis-Plus 配置 |
| 2.5 | MyBatis-Plus 配置类 | backend/src/main/java/com/mortgage/config/MybatisPlusConfig.java | 分页插件、Mapper扫描 |
| 2.6 | CORS 配置类 | backend/src/main/java/com/mortgage/config/CorsConfig.java | 跨域配置 |
| 2.7 | Web 配置类 | backend/src/main/java/com/mortgage/config/WebConfig.java | Web 相关配置 |
| 2.8 | 全局异常处理 | backend/src/main/java/com/mortgage/exception/GlobalExceptionHandler.java | 统一异常处理 |
| 2.9 | 业务异常类 | backend/src/main/java/com/mortgage/exception/BusinessException.java | 自定义业务异常 |
| 2.10 | 统一响应类 | backend/src/main/java/com/mortgage/common/Result.java | API 统一响应格式 |
| 2.11 | 常量类 | backend/src/main/java/com/mortgage/common/Constants.java | 系统常量定义 |
| 2.12 | 枚举类 - 还款方式 | backend/src/main/java/com/mortgage/enums/RepaymentMethod.java | 等额本息/等额本金 |
| 2.13 | 枚举类 - 提前还款类型 | backend/src/main/java/com/mortgage/enums/PrepayType.java | 缩期/减月供 |
| 2.14 | 枚举类 - 贷款类型 | backend/src/main/java/com/mortgage/enums/LoanType.java | 商业/公积金/组合 |
| 2.15 | 枚举类 - 计划状态 | backend/src/main/java/com/mortgage/enums/PlanStatus.java | 作废/有效/结清 |

### pom.xml 依赖清单

```xml
<dependencies>
    <!-- Spring Boot -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis-Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.5</version>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
    
    <!-- OpenPDF -->
    <dependency>
        <groupId>com.github.librepdf</groupId>
        <artifactId>openpdf</artifactId>
        <version>1.3.30</version>
    </dependency>
    
    <!-- Validation -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    
    <!-- Test -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

---

## 四、Phase 3: 后端核心业务开发（预计 3 天）

### 3.1 Entity 实体类开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.1.1 | LoanRequest 实体 | model/entity/LoanRequest.java | 贷款申请实体 |
| 3.1.2 | RepaymentPlan 实体 | model/entity/RepaymentPlan.java | 还款计划实体 |
| 3.1.3 | RepaymentDetail 实体 | model/entity/RepaymentDetail.java | 还款明细实体 |
| 3.1.4 | PrepayRecord 实体 | model/entity/PrepayRecord.java | 提前还款记录实体 |
| 3.1.5 | CompareRecord 实体 | model/entity/CompareRecord.java | 对比记录实体 |

### 3.2 DTO 数据传输对象开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.2.1 | CalculateRequest | model/dto/CalculateRequest.java | 计算请求DTO |
| 3.2.2 | CalculateResponse | model/dto/CalculateResponse.java | 计算响应DTO |
| 3.2.3 | CompareRequest | model/dto/CompareRequest.java | 对比请求DTO |
| 3.2.4 | CompareResponse | model/dto/CompareResponse.java | 对比响应DTO |
| 3.2.5 | PrepayRequest | model/dto/PrepayRequest.java | 提前还款请求DTO |
| 3.2.6 | PrepayResponse | model/dto/PrepayResponse.java | 提前还款响应DTO |
| 3.2.7 | DetailDTO | model/dto/DetailDTO.java | 明细DTO |
| 3.2.8 | PlanDTO | model/dto/PlanDTO.java | 计划DTO |

### 3.3 Mapper 数据访问层开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.3.1 | LoanRequestMapper | mapper/LoanRequestMapper.java | 贷款申请Mapper |
| 3.3.2 | RepaymentPlanMapper | mapper/RepaymentPlanMapper.java | 还款计划Mapper |
| 3.3.3 | RepaymentDetailMapper | mapper/RepaymentDetailMapper.java | 还款明细Mapper |
| 3.3.4 | PrepayRecordMapper | mapper/PrepayRecordMapper.java | 提前还款记录Mapper |
| 3.3.5 | CompareRecordMapper | mapper/CompareRecordMapper.java | 对比记录Mapper |
| 3.3.6 | RepaymentDetailMapper.xml | resources/mapper/RepaymentDetailMapper.xml | 批量插入SQL |

### 3.4 Util 工具类开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.4.1 | CalculatorUtil | util/CalculatorUtil.java | 核心计算工具（等额本息/等额本金/提前还款） |
| 3.4.2 | DateUtil | util/DateUtil.java | 日期工具（还款日期计算） |
| 3.4.3 | PlanNoGenerator | util/PlanNoGenerator.java | 计划编号生成器 |
| 3.4.4 | DecimalUtil | util/DecimalUtil.java | 精度处理工具 |

### 3.5 Service 服务层开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.5.1 | CalculateService | service/CalculateService.java | 计算服务（核心业务逻辑） |
| 3.5.2 | CompareService | service/CompareService.java | 方案对比服务 |
| 3.5.3 | PrepayService | service/PrepayService.java | 提前还款服务 |
| 3.5.4 | PlanService | service/PlanService.java | 计划管理服务 |
| 3.5.5 | ExportService | service/ExportService.java | PDF导出服务 |
| 3.5.6 | ValidatorService | service/ValidatorService.java | 参数校验服务 |

### 3.6 Controller 控制器层开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 3.6.1 | CalculateController | controller/CalculateController.java | 计算接口 |
| 3.6.2 | CompareController | controller/CompareController.java | 对比接口 |
| 3.6.3 | PrepayController | controller/PrepayController.java | 提前还款接口 |
| 3.6.4 | PlanController | controller/PlanController.java | 计划管理接口 |
| 3.6.5 | ExportController | controller/ExportController.java | 导出接口 |

---

## 五、Phase 4: 前端基础框架搭建（预计 1 天）

### 执行步骤

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 4.1 | 创建前端目录 | frontend/ | 创建 Vue 项目结构 |
| 4.2 | 初始化 Vue 项目 | frontend/ | vue create 或手动创建 |
| 4.3 | 编写 package.json | frontend/package.json | 依赖配置 |
| 4.4 | 编写 vue.config.js | frontend/vue.config.js | Vue CLI 配置 |
| 4.5 | 编写入口 HTML | frontend/public/index.html | HTML 模板 |
| 4.6 | 编写入口 JS | frontend/src/main.js | Vue 入口文件 |
| 4.7 | 编写根组件 | frontend/src/App.vue | 根组件 |
| 4.8 | 编写路由配置 | frontend/src/router/index.js | Vue Router 配置 |
| 4.9 | 编写 Vuex 配置 | frontend/src/store/index.js | Vuex 状态管理 |
| 4.10 | 编写 Axios 封装 | frontend/src/utils/request.js | HTTP 请求封装 |
| 4.11 | 编写格式化工具 | frontend/src/utils/format.js | 数据格式化 |
| 4.12 | 编写校验工具 | frontend/src/utils/validate.js | 表单校验规则 |
| 4.13 | 编写常量定义 | frontend/src/constants/index.js | 前端常量 |

### package.json 依赖清单

```json
{
  "dependencies": {
    "vue": "^2.6.14",
    "vue-router": "^3.5.3",
    "vuex": "^3.6.2",
    "element-ui": "^2.15.14",
    "echarts": "^5.4.3",
    "axios": "^0.27.2",
    "file-saver": "^2.0.5"
  },
  "devDependencies": {
    "@vue/cli-service": "^5.0.8",
    "vue-template-compiler": "^2.6.14",
    "sass": "^1.32.7",
    "sass-loader": "^12.0.0"
  }
}
```

---

## 六、Phase 5: 前端页面开发（预计 2 天）

### 6.1 API 接口封装

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 5.1.1 | calculate.js | api/calculate.js | 计算相关 API |
| 5.1.2 | compare.js | api/compare.js | 对比相关 API |
| 5.1.3 | prepay.js | api/prepay.js | 提前还款相关 API |
| 5.1.4 | plan.js | api/plan.js | 计划管理 API |
| 5.1.5 | export.js | api/export.js | 导出相关 API |

### 6.2 通用组件开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 5.2.1 | LoanForm.vue | components/LoanForm.vue | 贷款输入表单组件 |
| 5.2.2 | PlanTable.vue | components/PlanTable.vue | 还款明细表格组件 |
| 5.2.3 | PlanChart.vue | components/PlanChart.vue | 图表展示组件 |
| 5.2.4 | CompareResult.vue | components/CompareResult.vue | 对比结果展示组件 |
| 5.2.5 | PrepayForm.vue | components/PrepayForm.vue | 提前还款表单组件 |
| 5.2.6 | SummaryCard.vue | components/SummaryCard.vue | 汇总信息卡片组件 |

### 6.3 页面组件开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 5.3.1 | Calculate.vue | views/Calculate.vue | 贷款计算主页面 |
| 5.3.2 | Compare.vue | views/Compare.vue | 方案对比页面 |
| 5.3.3 | Prepay.vue | views/Prepay.vue | 提前还款页面 |
| 5.3.4 | Detail.vue | views/Detail.vue | 明细详情页面 |
| 5.3.5 | History.vue | views/History.vue | 历史记录页面 |

### 6.4 Vuex 模块开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 5.4.1 | calculator.js | store/modules/calculator.js | 计算器状态模块 |

### 6.5 静态资源开发

| 步骤 | 任务 | 文件路径 | 说明 |
|------|------|----------|------|
| 5.5.1 | global.scss | assets/styles/global.scss | 全局样式 |

---

## 七、Phase 6: 前后端联调（预计 1 天）

| 步骤 | 任务 | 说明 |
|------|------|------|
| 6.1 | 启动后端服务 | 运行 Spring Boot，验证 API |
| 6.2 | 启动前端服务 | 运行 Vue 开发服务器 |
| 6.3 | 计算功能联调 | 测试等额本息/等额本金计算 |
| 6.4 | 对比功能联调 | 测试方案对比功能 |
| 6.5 | 提前还款联调 | 测试提前还款模拟功能 |
| 6.6 | 导出功能联调 | 测试 PDF 导出功能 |
| 6.7 | 图表功能联调 | 测试图表展示功能 |
| 6.8 | 错误处理联调 | 测试异常场景处理 |

---

## 八、Phase 7: 测试与优化（预计 1 天）

| 步骤 | 任务 | 说明 |
|------|------|------|
| 7.1 | 后端单元测试 | 核心计算逻辑单元测试 |
| 7.2 | 边界值测试 | 测试极端参数场景 |
| 7.3 | 性能测试 | 测试计算响应时间 |
| 7.4 | 兼容性测试 | 测试多浏览器兼容 |
| 7.5 | 代码审查 | 检查代码质量 |
| 7.6 | 优化调整 | 优化性能和体验 |

---

## 九、开发执行顺序

```
Day 1: Phase 1 + Phase 2 开始
├── 数据库初始化（完成）
├── 后端项目结构创建
├── pom.xml 配置
├── 启动类 + 配置文件
└── 配置类开发

Day 2: Phase 2 完成 + Phase 3 开始
├── 异常处理 + 公共类
├── 枚举类开发
├── Entity 实体类开发
└── DTO 数据传输对象开发

Day 3: Phase 3 继续
├── Mapper 数据访问层开发
├── Util 工具类开发（核心计算逻辑）
└── CalculatorUtil 重点开发

Day 4: Phase 3 继续
├── Service 服务层开发
├── Controller 控制器层开发
└── 后端基础功能验证

Day 5: Phase 4 开始
├── 前端项目结构创建
├── package.json + vue.config.js
├── 路由 + Vuex + Axios 配置
└── 前端基础框架验证

Day 6: Phase 5 开始
├── API 接口封装
├── 通用组件开发
└── 贷款计算页面开发

Day 7: Phase 5 继续
├── 方案对比页面开发
├── 提前还款页面开发
├── 图表组件开发
└── PDF 导出功能开发

Day 8: Phase 6 联调
├── 前后端联调
├── 功能验证
└── 问题修复

Day 9: Phase 7 测试优化
├── 单元测试
├── 边界测试
├── 优化调整
└── 项目交付
```

---

## 十、关键文件优先级

### P0 - 最高优先级（必须先完成）

| 文件 | 说明 |
|------|------|
| docs/db/init.sql | 数据库初始化脚本 |
| backend/pom.xml | Maven 依赖配置 |
| application.yml | 后端配置文件 |
| CalculatorUtil.java | 核心计算逻辑 |
| CalculateService.java | 计算服务 |
| CalculateController.java | 计算接口 |
| frontend/package.json | 前端依赖配置 |
| Calculate.vue | 贷款计算主页面 |

### P1 - 高优先级

| 文件 | 说明 |
|------|------|
| Entity 实体类 | 数据库实体映射 |
| Mapper 接口 | 数据访问 |
| CompareService.java | 对比服务 |
| PrepayService.java | 提前还款服务 |
| Compare.vue | 对比页面 |
| Prepay.vue | 提前还款页面 |

### P2 - 中优先级

| 文件 | 说明 |
|------|------|
| PlanService.java | 计划管理服务 |
| ExportService.java | PDF 导出服务 |
| PlanChart.vue | 图表组件 |
| PlanTable.vue | 表格组件 |

---

## 十一、技术栈确认

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| JDK | 21 | Java 开发环境 |
| Spring Boot | 3.x | 应用框架 |
| MyBatis-Plus | 3.5.x | ORM 框架 |
| Maven | 3.9+ | 项目构建 |
| MySQL | 8.0 | 关系型数据库 |
| OpenPDF | 1.3+ | PDF 生成库（iText 4.x免费分支） |
| Lombok | 1.18+ | 简化代码 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 2.6+ | 前端框架 |
| Element UI | 2.15+ | UI 组件库 |
| ECharts | 5.x | 图表库 |
| Axios | 0.27+ | HTTP 客户端 |
| Vue Router | 3.x | 路由管理 |
| Vuex | 3.x | 状态管理 |

---

## 十二、数据库连接信息

| 项目 | 信息 |
|------|------|
| Host | localhost 或 127.0.0.1 |
| Port | 3306 |
| Username | mortgage_app |
| Password | Mortgage@2024#App |
| Database | mortgage_calculator |
| Root Password | Mortgage@2024#Root |

---

## 十三、API 接口清单

| 序号 | 接口路径 | HTTP 方法 | 功能说明 |
|------|----------|-----------|----------|
| 1 | /api/calculate | POST | 计算还款计划 |
| 2 | /api/calculate/equal-principal-interest | POST | 等额本息计算 |
| 3 | /api/calculate/equal-principal | POST | 等额本金计算 |
| 4 | /api/compare | POST | 方案对比 |
| 5 | /api/prepay | POST | 提前还款计算 |
| 6 | /api/prepay/compare | POST | 提前还款方式对比 |
| 7 | /api/plan | POST | 保存还款计划 |
| 8 | /api/plan/{planId} | GET | 查询还款计划 |
| 9 | /api/plan/{planId}/details | GET | 查询还款明细 |
| 10 | /api/plan/{planId}/export | GET | 导出 PDF |
| 11 | /api/history | GET | 查询历史记录 |

---

## 十四、验收标准

### 功能验收

| 功能 | 验收标准 |
|------|----------|
| 等额本息计算 | 计算结果误差 < 0.01元 |
| 等额本金计算 | 计算结果误差 < 0.01元 |
| 还款明细 | 最后期剩余本金为0 |
| 方案对比 | 对比数据计算正确 |
| 提前还款 | 新计划首期本金+利息 = 月供 |
| PDF导出 | 导出内容完整，格式正确 |

### 性能验收

| 指标 | 验收标准 |
|------|----------|
| 计算响应 | 360期计算 < 500ms |
| 明细加载 | 360期列表渲染 < 1s |
| PDF导出 | 生成时间 < 3s |

---

**文档结束**