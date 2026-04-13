<template>
  <div class="history-page">
    <div class="card-container">
      <div class="section-title">历史记录</div>
      <el-table :data="plans" border stripe v-loading="loading">
        <el-table-column prop="planNo" label="计划编号" width="180" />
        <el-table-column prop="loanAmount" label="贷款金额" width="120">
          <template slot-scope="scope">{{ formatAmount(scope.row.loanAmount) }}元</template>
        </el-table-column>
        <el-table-column prop="loanTerm" label="期限" width="100">
          <template slot-scope="scope">{{ scope.row.loanTerm }}月</template>
        </el-table-column>
        <el-table-column prop="annualRate" label="年利率" width="100">
          <template slot-scope="scope">{{ formatRate(scope.row.annualRate) }}</template>
        </el-table-column>
        <el-table-column prop="repaymentMethodName" label="还款方式" width="120" />
        <el-table-column prop="totalPayment" label="总还款" width="120">
          <template slot-scope="scope">{{ formatAmount(scope.row.totalPayment) }}元</template>
        </el-table-column>
        <el-table-column prop="totalInterest" label="总利息" width="120">
          <template slot-scope="scope">{{ formatAmount(scope.row.totalInterest) }}元</template>
        </el-table-column>
        <el-table-column prop="firstPaymentDate" label="首次还款日" width="120" />
        <el-table-column label="操作" width="150">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleExport(scope.row)">导出</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        style="margin-top: 20px"
        background
        layout="prev, pager, next"
        :total="total"
        :page-size="pageSize"
        :current-page="currentPage"
        @current-change="handlePageChange"
      />
    </div>
    <el-dialog title="还款明细" :visible.sync="dialogVisible" width="80%">
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
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getHistory, getDetails } from '@/api/plan'
import { exportPdf } from '@/api/export'
import { formatAmount, formatRate } from '@/utils/format'

export default {
  name: 'History',
  data() {
    return {
      plans: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      dialogVisible: false,
      details: [],
      currentPlan: null
    }
  },
  created() {
    this.loadHistory()
  },
  methods: {
    formatAmount,
    formatRate,
    async loadHistory() {
      this.loading = true
      try {
        const res = await getHistory(this.currentPage, this.pageSize)
        this.plans = res.data
        this.total = this.plans.length < this.pageSize ? this.currentPage * this.pageSize : (this.currentPage + 1) * this.pageSize
      } catch (e) {
        console.error(e)
      } finally {
        this.loading = false
      }
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadHistory()
    },
    async handleView(plan) {
      this.currentPlan = plan
      try {
        const res = await getDetails(plan.id)
        this.details = res.data
        this.dialogVisible = true
      } catch (e) {
        console.error(e)
      }
    },
    handleExport(plan) {
      window.open(exportPdf(plan.id), '_blank')
    }
  }
}
</script>
