export const REPAYMENT_METHODS = [
  { value: 'EQUAL_PRINCIPAL_INTEREST', label: '等额本息' },
  { value: 'EQUAL_PRINCIPAL', label: '等额本金' }
]

export const PREPAY_TYPES = [
  { value: 'SHORTEN_TERM', label: '缩期（月供不变，期限缩短）' },
  { value: 'REDUCE_PAYMENT', label: '减月供（期限不变，月供减少）' }
]

export const LOAN_TYPES = [
  { value: 'COMMERCIAL', label: '商业贷款' },
  { value: 'PROVIDENT_FUND', label: '公积金贷款' },
  { value: 'COMBINATION', label: '组合贷款' }
]

export const PLAN_STATUS = {
  VOID: { code: 0, label: '作废' },
  VALID: { code: 1, label: '有效' },
  SETTLED: { code: 2, label: '已结清' }
}

export const DEFAULT_LOAN_AMOUNT = 1000000
export const DEFAULT_LOAN_TERM = 240
export const DEFAULT_ANNUAL_RATE = 0.043
export const DEFAULT_RATE_FLOAT_BP = 0
export const MAX_LOAN_YEARS = 30
export const MIN_LOAN_YEARS = 1
