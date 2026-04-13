<template>
  <div class="prepay-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="card-container">
          <div class="section-title">第一步：贷款信息</div>
          <el-form ref="loanForm" :model="loanForm" :rules="loanRules" label-width="120px">
            <el-form-item label="未还本金" prop="remainingPrincipal">
              <el-input v-model="loanForm.remainingPrincipal" type="number" placeholder="请输入未还本金">
                <template slot="append">元</template>
              </el-input>
            </el-form-item>
            <el-form-item label="年利率" prop="annualRate">
              <el-input v-model="loanForm.annualRate" type="number" placeholder="请输入基准年利率">
                <template slot="append">%</template>
              </el-input>
            </el-form-item>
            <el-form-item label="利率浮动" prop="rateFloatBp">
              <el-input v-model="loanForm.rateFloatBp" type="number" placeholder="正数上浮，负数下浮">
                <template slot="append">bp</template>
              </el-input>
            </el-form-item>
            <el-form-item label="剩余期数" prop="remainingTerm">
              <el-input v-model="loanForm.remainingTerm" type="number" placeholder="请输入剩余期数">
                <template slot="append">期</template>
              </el-input>
            </el-form-item>
            <el-form-item label="还款方式" prop="repaymentMethod">
              <el-select v-model="loanForm.repaymentMethod" placeholder="请选择还款方式">
                <el-option label="等额本息" value="EQUAL_PRINCIPAL_INTEREST" />
                <el-option label="等额本金" value="EQUAL_PRINCIPAL" />
              </el-select>
            </el-form-item>
            <el-form-item label="下次还款日" prop="nextPaymentDate">
              <el-date-picker v-model="loanForm.nextPaymentDate" type="date" placeholder="请选择下次还款日期" value-format="yyyy-MM-dd" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleCalculateLoan" :loading="loading">计算还款明细</el-button>
              <el-button @click="handleResetLoan">重置</el-button>
            </el-form-item>
          </el-form>

          <div v-if="loanLocked" class="card-container" style="margin-top: 20px; background: #f0f9ff; border: 1px solid #409EFF">
            <div class="section-title">第二步：提前还款</div>
            <el-form ref="prepayForm" :model="prepayForm" :rules="prepayRules" label-width="120px">
              <el-form-item label="提前还款金额" prop="prepayAmount">
                <el-input v-model="prepayForm.prepayAmount" type="number" placeholder="请输入提前还款金额">
                  <template slot="append">元</template>
                </el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="handleSimulate" :loading="simulateLoading">模拟提前还款</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>

      <el-col :span="16">
        <div v-if="originalDetails.length > 0 && !compareResult" class="card-container">
          <div class="section-title">当前还款明细</div>
          <el-row :gutter="20">
            <el-col :span="12">
              <div class="summary-item"><span class="label">未还本金</span><span class="value">{{ formatAmount(loanForm.remainingPrincipal) }}元</span></div>
              <div class="summary-item"><span class="label">实际年利率</span><span class="value">{{ formatRate(actualAnnualRate) }}</span></div>
              <div class="summary-item"><span class="label">剩余期数</span><span class="value">{{ loanForm.remainingTerm }}期</span></div>
              <div class="summary-item"><span class="label">还款方式</span><span class="value">{{ repaymentMethodName }}</span></div>
            </el-col>
            <el-col :span="12">
              <div class="summary-item"><span class="label">月供</span><span class="value">{{ formatAmount(originalMonthlyPayment) }}元</span></div>
              <div class="summary-item"><span class="label">总利息</span><span class="value">{{ formatAmount(originalTotalInterest) }}元</span></div>
              <div class="summary-item"><span class="label">总还款额</span><span class="value">{{ formatAmount(originalTotalPayment) }}元</span></div>
            </el-col>
          </el-row>
          <el-divider />
          <el-table :data="originalDetails" border stripe max-height="400">
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
          </el-table>
        </div>

        <div v-if="compareResult" class="card-container">
          <div class="section-title">提前还款对比结果</div>
          
          <div class="card-container" style="background: #fff7e6; border: 1px solid #E6A23C; margin-bottom: 20px">
            <el-row :gutter="20">
              <el-col :span="8">
                <h4>原贷款信息</h4>
                <div class="summary-item"><span class="label">未还本金</span><span class="value">{{ formatAmount(compareResult.originalLoan.remainingPrincipal) }}元</span></div>
                <div class="summary-item"><span class="label">提前还款</span><span class="value" style="color: #E6A23C">{{ formatAmount(prepayForm.prepayAmount) }}元</span></div>
                <div class="summary-item"><span class="label">剩余本金</span><span class="value">{{ formatAmount(compareResult.shortenTermResult.remainingPrincipalAfter) }}元</span></div>
              </el-col>
              <el-col :span="8">
                <h4 style="color: #409EFF">缩期方式（月供不变）</h4>
                <div class="summary-item"><span class="label">新期限</span><span class="value">{{ compareResult.shortenTermResult.newTerm }}期</span></div>
                <div class="summary-item"><span class="label">新月供</span><span class="value">{{ formatAmount(compareResult.shortenTermResult.newMonthlyPayment) }}元</span></div>
                <div class="summary-item"><span class="label">新总利息</span><span class="value">{{ formatAmount(compareResult.shortenTermResult.newTotalInterest) }}元</span></div>
                <div class="summary-item"><span class="label" style="color: #67C23A">节省利息</span><span class="value" style="color: #67C23A; font-weight: bold">{{ formatAmount(compareResult.shortenTermResult.savedInterest) }}元</span></div>
              </el-col>
              <el-col :span="8">
                <h4 style="color: #67C23A">减月供方式（期限不变）</h4>
                <div class="summary-item"><span class="label">新期限</span><span class="value">{{ compareResult.reducePaymentResult.newTerm }}期</span></div>
                <div class="summary-item"><span class="label">新月供</span><span class="value">{{ formatAmount(compareResult.reducePaymentResult.newMonthlyPayment) }}元</span></div>
                <div class="summary-item"><span class="label">新总利息</span><span class="value">{{ formatAmount(compareResult.reducePaymentResult.newTotalInterest) }}元</span></div>
                <div class="summary-item"><span class="label" style="color: #67C23A">节省利息</span><span class="value" style="color: #67C23A; font-weight: bold">{{ formatAmount(compareResult.reducePaymentResult.savedInterest) }}元</span></div>
              </el-col>
            </el-row>
          </div>

          <div class="card-container" style="background: #f0f9ff; border: 1px solid #409EFF">
            <h4>对比分析</h4>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="summary-item"><span class="label">缩期减少期数</span><span class="value">{{ compareResult.compareData.termDiff }}期</span></div>
              </el-col>
              <el-col :span="8">
                <div class="summary-item"><span class="label">月供差额</span><span class="value">{{ formatAmount(compareResult.compareData.monthlyPaymentDiff) }}元</span></div>
              </el-col>
              <el-col :span="8">
                <div class="summary-item"><span class="label">节省利息差额</span><span class="value">{{ formatAmount(compareResult.compareData.savedInterestDiff) }}元</span></div>
              </el-col>
            </el-row>
            <el-alert :title="compareResult.compareData.recommendation" type="info" show-icon :closable="false" style="margin-top: 16px" />
          </div>

          <el-divider />

          <el-tabs v-model="activeTab">
            <el-tab-pane label="缩期方式明细" name="shorten">
              <el-table :data="compareResult.shortenTermResult.details" border stripe max-height="400">
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
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="减月供方式明细" name="reduce">
              <el-table :data="compareResult.reducePaymentResult.details" border stripe max-height="400">
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
              </el-table>
            </el-tab-pane>
          </el-tabs>

          <el-button type="primary" @click="handleResetAll" style="margin-top: 20px">重新计算</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { simulatePrepay } from '@/api/prepay'
import { formatAmount, formatRate } from '@/utils/format'
import { calculate } from '@/api/calculate'

export default {
  name: 'Prepay',
  data() {
    return {
      loanForm: {
        remainingPrincipal: 1263983.31,
        annualRate: 3.5,
        rateFloatBp: -30,
        remainingTerm: 275,
        repaymentMethod: 'EQUAL_PRINCIPAL_INTEREST',
        nextPaymentDate: new Date().toISOString().slice(0, 10)
      },
      loanRules: {
        remainingPrincipal: [{ required: true, message: '请输入未还本金', trigger: 'blur' }],
        annualRate: [{ required: true, message: '请输入年利率', trigger: 'blur' }],
        remainingTerm: [{ required: true, message: '请输入剩余期数', trigger: 'blur' }],
        repaymentMethod: [{ required: true, message: '请选择还款方式', trigger: 'change' }],
        nextPaymentDate: [{ required: true, message: '请选择下次还款日期', trigger: 'change' }]
      },
      prepayForm: {
        prepayAmount: 100000
      },
      prepayRules: {
        prepayAmount: [{ required: true, message: '请输入提前还款金额', trigger: 'blur' }]
      },
      loading: false,
      simulateLoading: false,
      loanLocked: false,
      originalDetails: [],
      originalMonthlyPayment: null,
      originalTotalInterest: null,
      originalTotalPayment: null,
      actualAnnualRate: null,
      repaymentMethodName: '',
      compareResult: null,
      activeTab: 'shorten'
    }
  },
  methods: {
    formatAmount,
    formatRate,
    handleCalculateLoan() {
      this.$refs.loanForm.validate(async valid => {
        if (!valid) return
        this.loading = true
        try {
          const data = {
            loanAmount: Number(this.loanForm.remainingPrincipal),
            annualRate: Number(this.loanForm.annualRate) / 100,
            rateFloatBp: Number(this.loanForm.rateFloatBp) || 0,
            loanTerm: Number(this.loanForm.remainingTerm),
            repaymentMethod: this.loanForm.repaymentMethod,
            startDate: this.loanForm.nextPaymentDate
          }
          const res = await calculate(data)
          this.originalDetails = res.data.details || []
          this.originalMonthlyPayment = res.data.monthlyPayment
          this.originalTotalInterest = res.data.totalInterest
          this.originalTotalPayment = res.data.totalPayment
          this.actualAnnualRate = res.data.actualAnnualRate
          this.repaymentMethodName = res.data.repaymentMethodName
          this.loanLocked = true
        } catch (e) {
          console.error(e)
        } finally {
          this.loading = false
        }
      })
    },
    handleResetLoan() {
      this.$refs.loanForm.resetFields()
      this.loanLocked = false
      this.originalDetails = []
      this.compareResult = null
    },
    handleSimulate() {
      this.$refs.prepayForm.validate(async valid => {
        if (!valid) return
        this.simulateLoading = true
        try {
          const data = {
            remainingPrincipal: Number(this.loanForm.remainingPrincipal),
            annualRate: Number(this.loanForm.annualRate) / 100,
            rateFloatBp: Number(this.loanForm.rateFloatBp) || 0,
            remainingTerm: Number(this.loanForm.remainingTerm),
            repaymentMethod: this.loanForm.repaymentMethod,
            nextPaymentDate: this.loanForm.nextPaymentDate,
            prepayAmount: Number(this.prepayForm.prepayAmount)
          }
          const res = await simulatePrepay(data)
          this.compareResult = res.data
        } catch (e) {
          console.error(e)
        } finally {
          this.simulateLoading = false
        }
      })
    },
    handleResetAll() {
      this.loanLocked = false
      this.originalDetails = []
      this.compareResult = null
      this.prepayForm.prepayAmount = 100000
    }
  }
}
</script>

<style lang="scss" scoped>
.prepay-page {
  .card-container {
    min-height: 200px;
  }
}
</style>