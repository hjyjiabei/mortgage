<template>
  <div class="calculate-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card-container">
          <div class="section-title">贷款参数</div>
          <el-form ref="form" :model="form" :rules="rules" label-width="100px">
            <el-form-item label="贷款金额" prop="loanAmount">
              <el-input v-model="form.loanAmount" type="number" placeholder="请输入贷款金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
            <el-form-item label="贷款期限" prop="loanTerm">
              <el-input v-model="form.loanTerm" type="number" placeholder="请输入贷款期限">
                <template slot="append">月</template>
              </el-input>
            </el-form-item>
<el-form-item label="年利率" prop="annualRate">
              <el-input v-model="form.annualRate" type="number" placeholder="请输入年利率">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
            <el-form-item label="利率浮动" prop="rateFloatBp">
              <el-input v-model="form.rateFloatBp" type="number" placeholder="正数上浮，负数下浮">
                <template slot="append">bp</template>
              </el-input>
            </el-form-item>
            <el-form-item label="还款方式" prop="repaymentMethod">
              <el-select v-model="form.repaymentMethod" placeholder="请选择还款方式">
                <el-option label="等额本息" value="EQUAL_PRINCIPAL_INTEREST" />
                <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
              </el-select>
            </el-form-item>
            <el-form-item label="放款日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="请选择放款日期" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleCalculate" :loading="loading">计算</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="16">
        <div v-if="result" class="card-container">
          <div class="section-title">计算结果</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="summary-item"><span class="label">贷款金额</span><span class="value">{{ formatAmount(result.loanAmount) }}元</span></div>
              <div class="summary-item"><span class="label">贷款期限</span><span class="value">{{ result.loanTerm }}月</span></div>
<div class="summary-item"><span class="label">年利率</span><span class="value">{{ formatRate(result.annualRate) }}</span></div>
               <div class="summary-item"><span class="label">利率浮动</span><span class="value">{{ result.rateFloatBp }}bp</span></div>
               <div class="summary-item"><span class="label">实际年利率</span><span class="value">{{ formatRate(result.actualAnnualRate) }}</span></div>
               <div class="summary-item"><span class="label">月利率</span><span class="value">{{ formatRate(result.monthlyRate) }}</span></div>
              <div class="summary-item"><span class="label">还款方式</span><span class="value">{{ result.repaymentMethodName }}</span></div>
              <div class="summary-item" v-if="result.monthlyPayment"><span class="label">月供</span><span class="value">{{ formatAmount(result.monthlyPayment) }}元</span></div>
              <div class="summary-item"><span class="label">首月还款</span><span class="value">{{ formatAmount(result.firstPayment) }}元</span></div>
              <div class="summary-item"><span class="label">末月还款</span><span class="value">{{ formatAmount(result.lastPayment) }}元</span></div>
            </el-col>
            <el-col :span="12">
              <div class="summary-item"><span class="label">总还款额</span><span class="value">{{ formatAmount(result.totalPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总利息</span><span class="value">{{ formatAmount(result.totalInterest) }}元</span></div>
              <div class="summary-item"><span class="label">总本金</span><span class="value">{{ formatAmount(result.totalPrincipal) }}元</span></div>
              <div class="summary-item"><span class="label">利息占比</span><span class="value">{{ result.interestRatio }}%</span></div>
              <div class="summary-item"><span class="label">首次还款日</span><span class="value">{{ result.firstPaymentDate }}</span></div>
              <div class="summary-item"><span class="label">最后还款日</span><span class="value">{{ result.lastPaymentDate }}</span></div>
            </el-col>
          </el-row>
        </div>
        <div v-if="details.length > 0" class="card-container">
          <div class="section-title">还款明细</div>
          <el-table :data="details" border stripe max-height="400">
            <el-table-column prop="period" label="期数" width="80" />
            <el-table-column prop="paymentDate" label="还款日期" width="120" />
            <el-table-column prop="monthlyPayment" label="月供" width="120">
              <template slot-scope="scope">{{ formatAmount(scope.row.monthlyPayment) }}</template>
            </el-table-column>
            <el-table-column prop="principal" label="本金" width="120">
              <template slot-scope="scope">{{ formatAmount(scope.row.principal) }}</template>
            </el-table-column>
            <el-table-column prop="interest" label="利息" width="120">
              <template slot-scope="scope">{{ formatAmount(scope.row.interest) }}</template>
            </el-table-column>
            <el-table-column prop="remainingPrincipal" label="剩余本金" width="120">
              <template slot-scope="scope">{{ formatAmount(scope.row.remainingPrincipal) }}</template>
            </el-table-column>
            <el-table-column prop="cumulativePayment" label="累计还款" width="120">
              <template slot-scope="scope">{{ formatAmount(scope.row.cumulativePayment) }}</template>
            </el-table-column>
          </el-table>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { calculate } from '@/api/calculate'
import { formatAmount, formatRate } from '@/utils/format'
import { DEFAULT_LOAN_AMOUNT, DEFAULT_LOAN_TERM, DEFAULT_ANNUAL_RATE } from '@/constants'

export default {
  name: 'Calculate',
  data() {
    return {
form: {
        loanAmount: DEFAULT_LOAN_AMOUNT,
        loanTerm: DEFAULT_LOAN_TERM,
        annualRate: 4.3,
        rateFloatBp: 0,
        repaymentMethod: 'EQUAL_PRINCIPAL_INTEREST',
        startDate: new Date().toISOString().slice(0, 10)
      },
      rules: {
        loanAmount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }],
        loanTerm: [{ required: true, message: '请输入贷款期限', trigger: 'blur' }],
        annualRate: [{ required: true, message: '请输入年利率', trigger: 'blur' }],
        repaymentMethod: [{ required: true, message: '请选择还款方式', trigger: 'change' }],
        startDate: [{ required: true, message: '请选择放款日期', trigger: 'change' }]
      },
      loading: false,
      result: null,
      details: []
    }
  },
  methods: {
    formatAmount,
    formatRate,
handleCalculate() {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            ...this.form,
            annualRate: Number(this.form.annualRate) / 100,
            rateFloatBp: Number(this.form.rateFloatBp) || 0
          }
          const res = await calculate(data)
          this.result = res.data
          this.details = res.data.details || []
        } catch (e) {
          console.error(e)
        } finally {
          this.loading = false
        }
      })
    },
    handleReset() {
      this.$refs.form.resetFields()
      this.result = null
      this.details = []
    }
  }
}
</script>

<style lang="scss" scoped>
.calculate-page {
  .card-container {
    min-height: 200px;
  }
}
</style>
