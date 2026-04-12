export function validateLoanAmount(value) {
  if (!value) return '请输入贷款金额'
  const num = Number(value)
  if (num < 10000) return '贷款金额不能低于10000元'
  if (num > 100000000) return '贷款金额不能超过1亿元'
  return true
}

export function validateLoanTerm(value) {
  if (!value) return '请输入贷款期限'
  const num = Number(value)
  if (num < 1) return '贷款期限不能低于1个月'
  if (num > 360) return '贷款期限不能超过360个月'
  return true
}

export function validateAnnualRate(value) {
  if (!value) return '请输入年利率'
  const num = Number(value)
  if (num < 0.001) return '年利率不能低于0.1%'
  if (num > 1) return '年利率不能超过100%'
  return true
}

export function validatePrepayAmount(value, maxAmount) {
  if (!value) return '请输入提前还款金额'
  const num = Number(value)
  if (num < 1000) return '提前还款金额不能低于1000元'
  if (maxAmount && num > Number(maxAmount)) return '提前还款金额不能超过剩余本金'
  return true
}
