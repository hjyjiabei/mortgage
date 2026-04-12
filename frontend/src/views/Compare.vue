<template>
  <div class="compare-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card-container">
          <div class="section-title">对比参数</div>
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
            <el-form-item label="放款日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" placeholder="请选择放款日期" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleCompare" :loading="loading">对比</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="16">
        <div v-if="result" class="card-container">
          <div class="section-title">对比结果</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <h4 style="color: #409EFF">等额本息</h4>
              <div class="summary-item"><span class="label">首月还款</span><span class="value">{{ formatAmount(result.planA.firstPayment) }}元</span></div>
              <div class="summary-item"><span class="label">末月还款</span><span class="value">{{ formatAmount(result.planA.lastPayment) }}元</span></div>
              <div class="summary-item"><span class="label">月供</span><span class="value">{{ formatAmount(result.planA.monthlyPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总还款额</span><span class="value">{{ formatAmount(result.planA.totalPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总利息</span><span class="value">{{ formatAmount(result.planA.totalInterest) }}元</span></div>
              <div class="summary-item"><span class="label">利息占比</span><span class="value">{{ result.planA.interestRatio }}%</span></div>
            </el-col>
            <el-col :span="12">
              <h4 style="color: #67C23A">等额本金</h4>
              <div class="summary-item"><span class="label">首月还款</span><span class="value">{{ formatAmount(result.planB.firstPayment) }}元</span></div>
              <div class="summary-item"><span class="label">末月还款</span><span class="value">{{ formatAmount(result.planB.lastPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总还款额</span><span class="value">{{ formatAmount(result.planB.totalPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总利息</span><span class="value">{{ formatAmount(result.planB.totalInterest) }}元</span></div>
              <div class="summary-item"><span class="label">利息占比</span><span class="value">{{ result.planB.interestRatio }}%</span></div>
            </el-col>
          </el-row>
          <el-divider />
          <div class="card-container" style="background: #f0f9ff; border: 1px solid #409EFF">
            <h4>差额对比（等额本息 - 等额本金）</h4>
            <div class="summary-item"><span class="label">利息差额</span><span class="value">{{ formatAmount(result.interestDiff) }}元</span></div>
            <div class="summary-item"><span class="label">首月差额</span><span class="value">{{ formatAmount(result.firstPaymentDiff) }}元</span></div>
            <div class="summary-item"><span class="label">总还款差额</span><span class="value">{{ formatAmount(result.totalPaymentDiff) }}元</span></div>
            <el-alert :title="result.recommendation" type="info" show-icon :closable="false" style="margin-top: 16px" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { compare } from '@/api/compare'
import { formatAmount } from '@/utils/format'
import { DEFAULT_LOAN_AMOUNT, DEFAULT_LOAN_TERM } from '@/constants'

export default {
  name: 'Compare',
  data() {
    return {
      form: {
        loanAmount: DEFAULT_LOAN_AMOUNT,
        loanTerm: DEFAULT_LOAN_TERM,
        annualRate: 4.3,
        startDate: new Date().toISOString().slice(0, 10)
      },
      rules: {
        loanAmount: [{ required: true, message: '请输入贷款金额', trigger: 'blur' }],
        loanTerm: [{ required: true, message: '请输入贷款期限', trigger: 'blur' }],
        annualRate: [{ required: true, message: '请输入年利率', trigger: 'blur' }],
        startDate: [{ required: true, message: '请选择放款日期', trigger: 'change' }]
      },
      loading: false,
      result: null
    }
  },
  methods: {
    formatAmount,
    handleCompare() {
      this..form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            ...this.form,
            annualRate: Number(this.form.annualRate) / 100
          }
          const res = await compare(data)
          this.result = res.data
        } catch (e) {
          console.error(e)
        } finally {
          this.loading = false
        }
      })
    }
  }
}
</script>
