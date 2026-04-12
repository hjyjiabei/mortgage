<template>
  <div class="prepay-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card-container">
          <div class="section-title">提前还款参数</div>
          <el-form ref="form" :model="form" :rules="rules" label-width="120px">
            <el-form-item label="还款计划ID" prop="planId">
              <el-input v-model="form.planId" type="number" placeholder="请输入计划ID" />
            </el-form-item>
            <el-form-item label="提前还款期数" prop="prepayPeriod">
              <el-input v-model="form.prepayPeriod" type="number" placeholder="请输入期数">
                <template slot="append">期</template>
              </el-input>
            </el-form-item>
            <el-form-item label="提前还款金额" prop="prepayAmount">
              <el-input v-model="form.prepayAmount" type="number" placeholder="请输入金额">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
            <el-form-item label="还款类型" prop="prepayType">
              <el-select v-model="form.prepayType" placeholder="请选择类型">
                <el-option label="缩期（月供不变）" value="SHORTEN_TERM" />
                <el-option label="减月供（期限不变）" value="REDUCE_PAYMENT" />
              </el-select>
            </el-form-item>
            <el-form-item label="提前还款日期" prop="prepayDate">
              <el-date-picker v-model="form.prepayDate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSimulate" :loading="loading">模拟计算</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <el-col :span="16">
        <div v-if="result" class="card-container">
          <div class="section-title">模拟结果</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <h4>提前还款信息</h4>
              <div class="summary-item"><span class="label">提前还款期数</span><span class="value">{{ result.prepayPeriod }}期</span></div>
              <div class="summary-item"><span class="label">提前还款金额</span><span class="value">{{ formatAmount(result.prepayAmount) }}元</span></div>
              <div class="summary-item"><span class="label">还款类型</span><span class="value">{{ result.prepayTypeName }}</span></div>
              <div class="summary-item"><span class="label">还款前剩余本金</span><span class="value">{{ formatAmount(result.remainingPrincipalBefore) }}元</span></div>
              <div class="summary-item"><span class="label">还款后剩余本金</span><span class="value">{{ formatAmount(result.remainingPrincipalAfter) }}元</span></div>
            </el-col>
            <el-col :span="12">
              <h4>新计划对比</h4>
              <div class="summary-item"><span class="label">原剩余期限</span><span class="value">{{ result.originalRemainingTerm }}月</span></div>
              <div class="summary-item"><span class="label">新剩余期限</span><span class="value">{{ result.newRemainingTerm }}月</span></div>
              <div class="summary-item"><span class="label">原月供</span><span class="value">{{ formatAmount(result.originalMonthlyPayment) }}元</span></div>
              <div class="summary-item"><span class="label">新月供</span><span class="value">{{ formatAmount(result.newMonthlyPayment) }}元</span></div>
              <div class="summary-item"><span class="label" style="color: #67C23A">节省利息</span><span class="value" style="color: #67C23A; font-weight: bold">{{ formatAmount(result.savedInterest) }}元</span></div>
            </el-col>
          </el-row>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { simulate } from '@/api/prepay'
import { formatAmount } from '@/utils/format'

export default {
  name: 'Prepay',
  data() {
    return {
      form: {
        planId: '',
        prepayPeriod: 12,
        prepayAmount: 100000,
        prepayType: 'SHORTEN_TERM',
        prepayDate: new Date().toISOString().slice(0, 10)
      },
      rules: {
        planId: [{ required: true, message: '请输入计划ID', trigger: 'blur' }],
        prepayPeriod: [{ required: true, message: '请输入提前还款期数', trigger: 'blur' }],
        prepayAmount: [{ required: true, message: '请输入提前还款金额', trigger: 'blur' }],
        prepayType: [{ required: true, message: '请选择还款类型', trigger: 'change' }]
      },
      loading: false,
      result: null
    }
  },
  methods: {
    formatAmount,
    handleSimulate() {
      this..form.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            ...this.form,
            planId: Number(this.form.planId),
            prepayPeriod: Number(this.form.prepayPeriod),
            prepayAmount: Number(this.form.prepayAmount)
          }
          const res = await simulate(data)
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
